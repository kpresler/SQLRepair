package edu.ncsu.sqlrepair.z3components;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.ReExpr;
import com.microsoft.z3.SeqExpr;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.schema.Column;

public class Z3Utils {

    /**
     * Used to represent the name of a constant value we've had the solver
     * replace. Public to be accessible by the API controller for performing
     * rewriting later
     */
    static public final String                S_CONST_VAL     = "statementConstValue";

    /**
     * Used to represent the name of an operator we've had the solver replace.
     * Public to be accessible by the API controller for performing rewriting
     * later
     */
    static public final String                S_OP_VAL        = "statementOperatorValue";

    static private final Map<String, Integer> numbersReplaced = new HashMap<String, Integer>();

    static private String getStatementOperator ( final String forWhat ) {
        return String.format( "%s::%s", forWhat, S_OP_VAL );
    }

    static private String getStatementConstValue ( final String forWhat ) {
        if ( !numbersReplaced.containsKey( forWhat ) ) {
            numbersReplaced.put( forWhat, (int) ( Math.random() * 100000 ) );
        }

        return S_CONST_VAL + numbersReplaced.get( forWhat );
    }

    /**
     * Recursively handle a nested/compound expression a SQL where clause
     *
     * @param ctx
     *            Context to use
     * @param rowMatches
     *            Representation as to whether the current row matches or not
     * @param columnHeader
     *            The header of the column to work with
     * @param sourceValue
     *            Expression representing the value in the column
     * @param operator
     *            Operator to work with from the WHERE clause
     * @return Updated row matching expression
     */
    static private BoolExpr nested ( final Context ctx,
            final BoolExpr rowMatches, final String columnHeader,
            final Expr sourceValue, final Expression operator ) {
        final BinaryExpression op = (BinaryExpression) operator;

        final BoolExpr left = createBoolFromArithmetic( rowMatches,
                columnHeader, sourceValue, op.getLeftExpression() );

        final BoolExpr right = createBoolFromArithmetic( rowMatches,
                columnHeader, sourceValue, op.getRightExpression() );

        if ( null == left && null == right ) {
            return rowMatches;
        }
        if ( null == left ) {
            if ( operator instanceof OrExpression ) {
                return ctx.mkOr( rowMatches, right );
            }
            if ( operator instanceof AndExpression ) {
                return ctx.mkAnd( rowMatches, right );
            }
        }
        if ( null == right ) {
            if ( operator instanceof OrExpression ) {
                return ctx.mkOr( rowMatches, left );
            }
            if ( operator instanceof AndExpression ) {
                return ctx.mkAnd( rowMatches, left );
            }
        }

        if ( operator instanceof OrExpression ) {
            return ctx.mkOr( left, right );
        }

        else if ( operator instanceof AndExpression ) {
            return ctx.mkAnd( left, right );
        }
        throw new IllegalArgumentException(
                "Nested expression type but no match could be found" );
    }

    /**
     * Attempt to perform a match between the current column and a boolean type.
     *
     * @param ctx
     *            Context to work with
     * @param cOperator
     *            The binary comparison operator (= or !=) to work with
     * @param rightExpression
     *            The expression on the right side of the operator
     * @param leftExpression
     *            The expression on the left side of the operator
     * @param sourceValue
     *            Any string value from the table to match on
     * @param columnHeader
     *            The header of the column we are considering
     * @return
     */
    private static BoolExpr tryBoolean ( final Context ctx,
            final BinaryExpression cOperator, final Expression rightExpression,
            final Expression leftExpression, final Expr sourceValue,
            final String columnHeader ) {
        /*
         * Booleans are actually represented as a special type of string, which
         * is why we need the call to `boolExpr(Obj,Obj)` to see if the string
         * is our special type or not
         */

        /* If we have a statement like `WHERE 'true' = 'false'` */
        if ( boolExpr( leftExpression, rightExpression )
                && leftExpression instanceof net.sf.jsqlparser.expression.StringValue
                && rightExpression instanceof net.sf.jsqlparser.expression.StringValue ) {

            final Expr left = ctx.mkBool( Boolean.valueOf(
                    ( (net.sf.jsqlparser.expression.StringValue) leftExpression )
                            .getValue() ) );

            final Expr right = ctx.mkBool( Boolean.valueOf(
                    ( (net.sf.jsqlparser.expression.StringValue) rightExpression )
                            .getValue() ) );

            return parseSimple( cOperator, left, right );

        }

        /* If we have a statement like `WHERE 'true' = x` */
        if ( boolExpr( leftExpression, rightExpression )
                && leftExpression instanceof net.sf.jsqlparser.expression.StringValue
                && rightExpression instanceof Column ) {

            final Expr left = ctx.mkBool( Boolean.valueOf(
                    ( (net.sf.jsqlparser.expression.StringValue) leftExpression )
                            .getValue() ) );

            if ( ( (Column) rightExpression ).getColumnName()
                    .equals( columnHeader ) ) {
                return parseSimple( cOperator, left, sourceValue );
            }
            /*
             * If the column header doesn't match this part of the statement,
             * continue on and let the right column match happen later
             */
            else {
                return null;
            }

        }

        /* If we have a statement like `WHERE x = 'true' */

        if ( boolExpr( leftExpression, rightExpression )
                && leftExpression instanceof Column
                && rightExpression instanceof net.sf.jsqlparser.expression.StringValue ) {

            final Expr right = ctx.mkBool( Boolean.valueOf(
                    ( (net.sf.jsqlparser.expression.StringValue) rightExpression )
                            .getValue() ) );

            /* If the column header matches up to what we want */
            if ( ( (Column) leftExpression ).getColumnName()
                    .equals( columnHeader ) ) {
                return parseSimple( cOperator, sourceValue, right );
            }
            /*
             * If the column header doesn't match this part of the statement,
             * continue on and let the right column match happen later
             */
            else {
                return null;
            }

        }
        return null;
    }

    /**
     * Attempt to perform a match between the current column and an integer
     * type.
     *
     * @param ctx
     *            Context to work with
     * @param cOperator
     *            The binary comparison operator (= or !=) to work with
     * @param rightExpression
     *            The expression on the right side of the operator
     * @param leftExpression
     *            The expression on the left side of the operator
     * @param sourceValue
     *            Any string value from the table to match on
     * @param columnHeader
     *            The header of the column we are considering
     * @return
     */
    private static BoolExpr tryInteger ( final Context ctx,
            final BinaryExpression cOperator, final Expression rightExpression,
            final Expression leftExpression, final Expr sourceValue,
            final String columnHeader ) {
        /* If we have a statement like `WHERE 1=1` */
        if ( leftExpression instanceof LongValue
                && rightExpression instanceof LongValue ) {
            final IntNum left = ctx
                    .mkInt( ( (LongValue) leftExpression ).getValue() );

            final IntNum right = ctx
                    .mkInt( ( (LongValue) rightExpression ).getValue() );

            return parseSimple( cOperator, left, right );
        }

        /*
         * Abstract away the actual number from the SQL statement, let the
         * solver come up with one
         */
        if ( leftExpression instanceof LongValue
                || rightExpression instanceof LongValue ) {
            final IntExpr stmtCstVal = ctx.mkIntConst(
                    getStatementConstValue( String.valueOf( cOperator ) ) );

            /* If we have a statement like `WHERE a > 6` */
            if ( leftExpression instanceof Column
                    && rightExpression instanceof LongValue ) {

                /* If the column header matches up to what we want */
                if ( ( (Column) leftExpression ).getColumnName()
                        .equals( columnHeader ) ) {

                    return parseSimple( cOperator, sourceValue, stmtCstVal );
                }
                /*
                 * If the column header doesn't match this part of the
                 * statement, continue on and let the right column match happen
                 * later
                 */
                else {
                    return null;
                }

            }

            /* If we have a statement like `WHERE 6 > a` */
            if ( leftExpression instanceof LongValue
                    && rightExpression instanceof Column ) {

                if ( ( (Column) rightExpression ).getColumnName()
                        .equals( columnHeader ) ) {
                    return parseSimple( cOperator, stmtCstVal, sourceValue );
                }
                /*
                 * If the column header doesn't match this part of the
                 * statement, continue on and let the right column match happen
                 * later
                 */
                else {
                    return null;
                }
            }

        }
        return null;
    }

    /**
     * Attempt to perform a match between the current column and a date type.
     *
     * @param ctx
     *            Context to work with
     * @param cOperator
     *            The binary comparison operator (= or !=) to work with
     * @param rightExpression
     *            The expression on the right side of the operator
     * @param leftExpression
     *            The expression on the left side of the operator
     * @param sourceValue
     *            Any string value from the table to match on
     * @param columnHeader
     *            The header of the column we are considering
     * @param isLikeExpression
     *            Dates can be compared either for strict equality or with like
     *            checks. This indicates which is which.
     * @return
     */
    private static BoolExpr tryDate ( final Context ctx,
            final BinaryExpression cOperator, final Expression rightExpression,
            final Expression leftExpression, final Expr sourceValue,
            final String columnHeader, final Boolean isLikeExpression ) {

        /* If we have a statement like `WHERE '2008-11-11' = '2008-11-12'` */
        if ( dateExpr( leftExpression, rightExpression )
                && leftExpression instanceof net.sf.jsqlparser.expression.StringValue
                && rightExpression instanceof net.sf.jsqlparser.expression.StringValue ) {

            final Expr left = ctx.mkString(
                    ( (net.sf.jsqlparser.expression.StringValue) leftExpression )
                            .getValue() );

            final Expr right = ctx.mkString(
                    ( (net.sf.jsqlparser.expression.StringValue) rightExpression )
                            .getValue() );

            return parseSimple( cOperator, left, right );

        }

        /* If we have a statement like `WHERE '2008-11-11' = x` */
        if ( dateExpr( leftExpression, rightExpression )
                && leftExpression instanceof net.sf.jsqlparser.expression.StringValue
                && rightExpression instanceof Column ) {

            final Expr left = ctx.mkInt( parseDate(
                    ( (net.sf.jsqlparser.expression.StringValue) leftExpression )
                            .getValue() ) );

            if ( ( (Column) rightExpression ).getColumnName()
                    .equals( columnHeader ) ) {
                return parseSimple( cOperator, left, sourceValue );
            }
            /*
             * If the column header doesn't match this part of the statement,
             * continue on and let the right column match happen later
             */
            else {
                return null;
            }

        }

        /* If we have a statement like `WHERE x = '2008-11-11' */
        if ( dateExpr( leftExpression, rightExpression )
                && leftExpression instanceof Column
                && rightExpression instanceof net.sf.jsqlparser.expression.StringValue ) {

            final Expr right = ctx.mkInt( parseDate(
                    ( (net.sf.jsqlparser.expression.StringValue) rightExpression )
                            .getValue() ) );

            /* If the column header matches up to what we want */
            if ( ( (Column) leftExpression ).getColumnName()
                    .equals( columnHeader ) ) {
                return parseSimple( cOperator, sourceValue, right );
            }
            /*
             * If the column header doesn't match this part of the statement,
             * continue on and let the right column match happen later
             */
            else {
                return null;
            }

        }

        /* If we have a statement like `WHERE x = curDate() */
        if ( leftExpression instanceof Column
                && rightExpression instanceof Function ) {
            final String funcValue = parseDateFunction(
                    (Function) rightExpression );

            final Expr right = ctx.mkInt( parseDate( funcValue ) );

            /* If the column header matches up to what we want */
            if ( ( (Column) leftExpression ).getColumnName()
                    .equals( columnHeader ) ) {
                return parseSimple( cOperator, sourceValue, right );
            }
            /*
             * If the column header doesn't match this part of the statement,
             * continue on and let the right column match happen later
             */
            else {
                return null;
            }

        }

        /* If we have a statement like `WHERE curdate() = x` */
        if ( leftExpression instanceof Function
                && rightExpression instanceof Column ) {
            final String funcValue = parseDateFunction(
                    (Function) leftExpression );

            final Expr left = ctx.mkInt( parseDate( funcValue ) );

            /* If the column header matches up to what we want */
            if ( ( (Column) rightExpression ).getColumnName()
                    .equals( columnHeader ) ) {
                return parseSimple( cOperator, sourceValue, left );
            }
            /*
             * If the column header doesn't match this part of the statement,
             * continue on and let the right column match happen later
             */
            else {
                return null;
            }

        }

        /* If we have a statement like `WHERE '2008-11-%' LIKE x` */
        if ( isLikeExpression
                && leftExpression instanceof net.sf.jsqlparser.expression.StringValue
                && rightExpression instanceof Column ) {

            if ( ( (Column) rightExpression ).getColumnName()
                    .equals( columnHeader ) ) {
                return dateInRange( ctx, sourceValue, leftExpression );
            }

        }

        /* If we have a statement like `WHERE x LIKE '2008-11-%' */
        if ( isLikeExpression && leftExpression instanceof Column
                && rightExpression instanceof net.sf.jsqlparser.expression.StringValue ) {

            if ( ( (Column) leftExpression ).getColumnName()
                    .equals( columnHeader ) ) {
                return dateInRange( ctx, sourceValue, rightExpression );
            }

        }

        return null;

    }

    /**
     * Checks to see if a date is within the range provided.
     *
     * @param ctx
     *            Context
     * @param date
     *            Date we are checking
     * @param rangeWithWildcard
     *            Expression to check if it's in or not
     * @return
     */
    static private BoolExpr dateInRange ( final Context ctx, final Expr date,
            final Expression rangeWithWildcard ) {

        final String wildCardDate = trimQuotes(
                String.valueOf( rangeWithWildcard ) );

        final Integer pos = wildCardDate.indexOf( "%" );

        String lBound = null, uBound = null;

        switch ( pos ) {
            case 9:
                lBound = wildCardDate.replace( "%", "0" );
                uBound = wildCardDate.replace( "%", "9" );
                break;
            case 8:
                lBound = wildCardDate.replace( "%", "01" );
                uBound = wildCardDate.replace( "%", "dd" );
                break;
            case 7:
                lBound = wildCardDate.replace( "%", "-01" );
                uBound = wildCardDate.replace( "%", "-dd" );
                break;
            case 5:
                lBound = wildCardDate.replace( "%", "01-01" );
                uBound = wildCardDate.replace( "%", "12-dd" );
                break;
            case 4:
                lBound = wildCardDate.replace( "%", "-01-01" );
                uBound = wildCardDate.replace( "%", "-12-dd" );
                break;
            case 3:
                lBound = wildCardDate.replace( "%", "0-01-01" );
                uBound = wildCardDate.replace( "%", "0-12-dd" );
                break;
            case 2:
                lBound = wildCardDate.replace( "%", "00-01-01" );
                uBound = wildCardDate.replace( "%", "00-12-dd" );
                break;
            case 1:
                lBound = wildCardDate.replace( "%", "000-01-01" );
                uBound = wildCardDate.replace( "%", "000-12-dd" );
                break;
            default:
                break;

        }

        final Integer uBoundMonth = Integer.valueOf( uBound.split( "-" )[1] );
        /* These magic numbers are the months that contain 31 days */
        final List<Integer> lMonths = Arrays
                .asList( new Integer[] { 1, 3, 5, 7, 8, 10, 12 } );
        uBound = uBound.replaceAll( "dd", uBoundMonth.equals( 2 ) ? "28"
                : lMonths.contains( uBoundMonth ) ? "31" : "30" );

        final Long parsedlBound = parseDateZDT( lBound ).minusDays( 1 )
                .toEpochSecond();
        final Long parseduBound = parseDateZDT( uBound ).plusDays( 1 )
                .toEpochSecond();

        return ctx.mkAnd(
                ctx.mkGt( (ArithExpr) date, ctx.mkInt( parsedlBound ) ),
                ctx.mkLt( (ArithExpr) date, ctx.mkInt( parseduBound ) ) );

    }

    /**
     * Attempt to perform a match between the current column and a string type.
     *
     * @param ctx
     *            Context to work with
     * @param cOperator
     *            The binary comparison operator (= or !=) to work with
     * @param rightExpression
     *            The expression on the right side of the operator
     * @param leftExpression
     *            The expression on the left side of the operator
     * @param sourceValue
     *            Any string value from the table to match on
     * @param columnHeader
     *            The header of the column we are considering
     * @param isLikeExpression
     *            Strings can be compared either for strict equality or with
     *            like checks. This indicates which is which.
     * @return
     */
    private static BoolExpr tryString ( final Context ctx,
            final BinaryExpression cOperator, final Expression rightExpression,
            final Expression leftExpression, final Expr sourceValue,
            final String columnHeader, final Boolean isLikeStatement ) {

        /* If we have a statement like `WHERE 'abc' = 'xyz'` */
        if ( leftExpression instanceof net.sf.jsqlparser.expression.StringValue
                && rightExpression instanceof net.sf.jsqlparser.expression.StringValue ) {

            final Expr left = ctx.mkString(
                    ( (net.sf.jsqlparser.expression.StringValue) leftExpression )
                            .getValue() );

            final Expr right = ctx.mkString(
                    ( (net.sf.jsqlparser.expression.StringValue) rightExpression )
                            .getValue() );

            return parseSimple( cOperator, left, right );

        }

        /* If we have a statement like `WHERE 'abc' = x` */
        if ( leftExpression instanceof net.sf.jsqlparser.expression.StringValue
                && rightExpression instanceof Column ) {

            /*
             * `LIKE` statements can have wildcards so we parse them separately
             */
            if ( isLikeStatement ) {
                final String strFromLike = ( (net.sf.jsqlparser.expression.StringValue) leftExpression )
                        .getValue();

                /* If the column header matches up to what we want */
                if ( ( (Column) rightExpression ).getColumnName()
                        .equals( columnHeader ) ) {
                    return createBoolFromLike( ctx, strFromLike,
                            (SeqExpr) sourceValue );
                }

            }
            else {
                final Expr left = ctx.mkString(
                        ( (net.sf.jsqlparser.expression.StringValue) leftExpression )
                                .getValue() );

                if ( ( (Column) rightExpression ).getColumnName()
                        .equals( columnHeader ) ) {
                    return parseSimple( cOperator, left, sourceValue );
                }
            }

        }

        /* If we have a statement like `WHERE x = 'abc' */

        if ( leftExpression instanceof Column
                && rightExpression instanceof net.sf.jsqlparser.expression.StringValue ) {

            /*
             * `LIKE` statements can have wildcards so we parse them separately
             */
            if ( isLikeStatement ) {

                final String strFromLike = ( (net.sf.jsqlparser.expression.StringValue) rightExpression )
                        .getValue();

                /* If the column header matches up to what we want */
                if ( ( (Column) leftExpression ).getColumnName()
                        .equals( columnHeader ) ) {
                    return createBoolFromLike( ctx, strFromLike,
                            (SeqExpr) sourceValue );
                }

            }

            else {
                final Expr right = ctx.mkString(
                        ( (net.sf.jsqlparser.expression.StringValue) rightExpression )
                                .getValue() );

                /* If the column header matches up to what we want */
                if ( ( (Column) leftExpression ).getColumnName()
                        .equals( columnHeader ) ) {
                    return parseSimple( cOperator, sourceValue, right );
                }
                /*
                 * If the column header doesn't match this part of the
                 * statement, continue on and let the right column match happen
                 * later
                 */
            }

        }

        return null;

    }

    /**
     * Wildcard string matching.
     *
     * @param ctx
     *            Z3 Context to use
     * @param strFromLike
     *            User-provided string in their WHERE clause we're trying to
     *            match against
     * @param sourceValue
     *            The expression from a table we're comparing against
     * @return
     */
    private static BoolExpr createBoolFromLike ( final Context ctx,
            final String strFromLike, final SeqExpr sourceValue ) {

        /*
         * Wherever we find a `%`, make a regex that is the kleene star of a
         * regex accepting anything, and then wrap the oter parts of the string
         * around that.
         */
        final String[] parts = strFromLike.split( "%" );

        /* Regex we're building up to recognize the input string */
        ReExpr regex = null;

        /*
         * A regular expression that will accept anything. Z3 has no nice way to
         * do this, so we're basing it on the ASCII character tables. Hopefully
         * this is fine
         */
        final ReExpr anything = ctx.mkStar(
                ctx.mkRange( ctx.mkString( "!" ), ctx.mkString( "~" ) ) );

        if ( strFromLike.startsWith( "%" ) ) {
            regex = anything;
        }

        else {
            regex = ctx.mkToRe( ctx.mkString( "" ) );
        }

        /*
         * Multiple elements means there had to be a wildcard somewhere in the
         * middle
         */
        if ( 1 != parts.length ) {

            for ( final String partOfStatement : parts ) {

                regex = ctx.mkConcat( regex, anything,
                        ctx.mkToRe( ctx.mkString( partOfStatement ) ) );
            }
        }

        /*
         * If we only have a single element, that means that there were no
         * intermediate wildcards that we need to join around
         */
        else {
            regex = ctx.mkConcat( regex,
                    ctx.mkToRe( ctx.mkString( parts[0] ) ) );
        }

        if ( strFromLike.endsWith( "%" ) ) {
            regex = ctx.mkConcat( regex, anything );
        }

        return ctx.mkInRe( sourceValue, regex );
    }

    /**
     * Method to create a Z3 BoolExpr that represents a SQL WHERE clause.
     *
     * @param sourceValue
     *            The source value, presumably from a user's example, to be
     *            integrated with the SQL clause
     * @param operator
     *            The SQL Expression that represents the WHERE clause. Compound
     *            clauses supported
     * @return The created BoolExpr.
     */
    public static BoolExpr createBoolFromArithmetic ( final BoolExpr rowMatches,
            final String columnHeader, final Expr sourceValue,
            final Expression operator ) {

        final Context ctx = Z3Components.getInstance().getContext();

        if ( null == ctx ) {
            throw new NullPointerException( "Context cannot be null!" );
        }

        /*
         * Recursively handle compound operators by breaking them apart into the
         * left-and-right components and then handling each independently
         */
        if ( operator instanceof OrExpression
                || operator instanceof AndExpression ) {
            return nested( ctx, rowMatches, columnHeader, sourceValue,
                    operator );

        }

        BoolExpr returnValue = null;

        if ( operator instanceof BinaryExpression ) {
            final BinaryExpression bOperator = (BinaryExpression) operator;

            final Expression rightExpression = bOperator.getRightExpression();

            final Expression leftExpression = bOperator.getLeftExpression();

            if ( operator instanceof ComparisonOperator ) {

                /*
                 * We'll try parsing the expression given as each of the
                 * supported types, one at a time
                 */
                if ( null == returnValue ) {
                    returnValue = tryBoolean( ctx, bOperator, rightExpression,
                            leftExpression, sourceValue, columnHeader );
                }

                if ( null == returnValue ) {
                    returnValue = tryInteger( ctx, bOperator, rightExpression,
                            leftExpression, sourceValue, columnHeader );
                }

                if ( null == returnValue ) {
                    returnValue = tryDate( ctx, bOperator, rightExpression,
                            leftExpression, sourceValue, columnHeader, false );
                }

                if ( null == returnValue ) {
                    returnValue = tryString( ctx, bOperator, rightExpression,
                            leftExpression, sourceValue, columnHeader, false );
                }

                return returnValue;

            }

            if ( operator instanceof LikeExpression ) {
                if ( null == returnValue
                        && ! ( sourceValue instanceof SeqExpr ) ) {
                    returnValue = tryDate( ctx, bOperator, rightExpression,
                            leftExpression, sourceValue, columnHeader, true );
                }
                if ( null == returnValue ) {
                    returnValue = tryString( ctx, bOperator, rightExpression,
                            leftExpression, sourceValue, columnHeader, true );
                }
                return returnValue;

            }

        }

        /*
         * Handle expressions like `SELECT * from TABLE where column in (value1,
         * value2, value3)`
         */
        if ( operator instanceof InExpression ) {
            /*
             * The way that an IN expression works is that our value must be
             * equal to one of the ones from the list provided
             */

            final InExpression iOperator = (InExpression) operator;

            return parseInStmt( iOperator, sourceValue, ctx );

        }

        /*
         * If no operator was provided, then we have no `WHERE` clause, so
         * return everything
         */
        if ( null == operator ) {
            return ctx.mkTrue();
        }

        /* Default in case user tries something we don't understand */
        throw new IllegalArgumentException(
                "Cannot find matching operator for " + operator );

    }

    /**
     * Create a boolean expression representing whether a column value was found
     * within a list of potential matches
     *
     * @param iOperator
     *            The list of values from the SQL statement
     * @param sourceValue
     *            Expression representing the column value that might be in the
     *            list provided
     * @param ctx
     *            Context to use
     * @return
     */
    private static BoolExpr parseInStmt ( final InExpression iOperator,
            final Expr sourceValue, final Context ctx ) {

        final List<Expression> inItems = ( (ExpressionList) iOperator
                .getRightItemsList() ).getExpressions();

        /*
         * This should be impossible, as `IN ()` is invalid SQL, but in case it
         * gets through somehow, handle it properly
         */
        if ( inItems.isEmpty() ) {
            return ctx.mkFalse();
        }

        Expression value = inItems.get( 0 );
        BoolExpr eqsValue = ctx.mkEq( sourceValue,
                value instanceof LongValue
                        ? ctx.mkInt( ( (LongValue) value ).getValue() )
                        : ctx.mkString( ( (StringValue) value ).getValue() ) );

        /*
         * If there's only one item, then our provided value must be equal to it
         */
        if ( 1 == inItems.size() ) {

        }
        else {
            for ( int i = 1; i < inItems.size(); ++i ) {

                value = inItems.get( i );
                eqsValue = ctx.mkOr( eqsValue,
                        ctx.mkEq( sourceValue, value instanceof LongValue
                                ? ctx.mkInt( ( (LongValue) value ).getValue() )
                                : ctx.mkString( ( (StringValue) value )
                                        .getValue() ) ) );

            }
        }

        return iOperator.isNot() ? ctx.mkNot( eqsValue ) : eqsValue;

    }

    /**
     * Creates a BoolExpr from a simple, non-compound SQL where clause (such as
     * `WHERE x > 5`)
     *
     * @param operator
     *            The simple SQL operand
     * @param leftValue
     *            The left operator to the SQL operand
     * @param rightValue
     *            The right operator to the SQL operand
     * @return
     */
    static public BoolExpr parseSimple ( final BinaryExpression operator,
            final Expr leftValue, final Expr rightValue ) {
        final Context ctx = Z3Components.getInstance().getContext();
        if ( operator instanceof LikeExpression
                || ( leftValue instanceof SeqExpr
                        || rightValue instanceof SeqExpr )
                || ( leftValue instanceof BoolExpr
                        || rightValue instanceof BoolExpr ) ) {
            return ctx.mkEq( leftValue, rightValue );
        }

        return parseSimpleAll( operator, leftValue, rightValue );
    }

    /**
     * This handles the magic of repair for incorrect operators in a query by
     * building up constraints indicating all possible operators and having the
     * solver figure out which one works.
     *
     * @param operator
     *            Base operator we are replacing, used to create a name so that
     *            we can refer to things later
     * @param leftValue
     *            Left value of the operator
     * @param rightValue
     *            Right value of the operator
     * @return
     */
    static private BoolExpr parseSimpleAll ( final BinaryExpression operator,
            final Expr leftValue, final Expr rightValue ) {

        final String baseOpName = getStatementOperator(
                String.format( "%s", operator ) );

        final Context ctx = Z3Components.getInstance().getContext();

        final List<BoolExpr> possibilities = new ArrayList<BoolExpr>();

        final BoolExpr isEq = ctx.mkBoolConst( baseOpName + "isEq" );

        possibilities
                .add( ctx.mkAnd( isEq, ctx.mkEq( leftValue, rightValue ) ) );

        final BoolExpr isNEq = ctx.mkBoolConst( baseOpName + "isNEq" );

        possibilities.add( ctx.mkAnd( isNEq,
                ctx.mkNot( ctx.mkEq( leftValue, rightValue ) ) ) );

        final BoolExpr isGTE = ctx.mkBoolConst( baseOpName + "isGTE" );

        possibilities.add( ctx.mkAnd( isGTE,
                ctx.mkGe( (ArithExpr) leftValue, (ArithExpr) rightValue ) ) );

        final BoolExpr isGT = ctx.mkBoolConst( baseOpName + "isGT" );

        possibilities.add( ctx.mkAnd( isGT,
                ctx.mkGt( (ArithExpr) leftValue, (ArithExpr) rightValue ) ) );

        final BoolExpr isLT = ctx.mkBoolConst( baseOpName + "isLT" );

        possibilities.add( ctx.mkAnd( isLT,
                ctx.mkLt( (ArithExpr) leftValue, (ArithExpr) rightValue ) ) );

        final BoolExpr isLTE = ctx.mkBoolConst( baseOpName + "isLTE" );

        possibilities.add( ctx.mkAnd( isLTE,
                ctx.mkLe( (ArithExpr) leftValue, (ArithExpr) rightValue ) ) );

        /*
         * XOR doesn't work properly and will happily announce that
         * XOR(1,1,1)==true, which isn't what we want. So we'll use the same
         * pbEq approach to enforce that only one can be true
         */
        final int[] coefficients = new int[] { 1, 1, 1, 1, 1, 1 };

        Z3Components
                .getInstance().getSolver().add(
                        ctx.mkEq(
                                ctx.mkPBEq( coefficients,
                                        new BoolExpr[] { isEq, isNEq, isGTE,
                                                isGT, isLT, isLTE },
                                        1 ),
                                ctx.mkTrue() ) );

        return ctx.mkOr( possibilities.toArray( new BoolExpr[] {} ) );

    }

    static private Map<String, Object> z3Objects = new HashMap<String, Object>();

    static public void put ( final String key, final Object value ) {
        z3Objects.put( key, value );
    }

    static public Object get ( final String key ) {
        return z3Objects.get( key );
    }

    /**
     * Check to see if the expressions provided actually are booleans or are a
     * more general type of String.
     *
     * @param left
     * @param right
     * @return
     */
    static private Boolean boolExpr ( final Object left, final Object right ) {
        if ( null != left && ( left.toString().contains( "true" )
                || left.toString().contains( "false" ) ) ) {
            return true;
        }
        if ( null != right && ( right.toString().contains( "true" )
                || right.toString().contains( "false" ) ) ) {
            return true;
        }

        return false;
    }

    /* Helper functions for dates */

    /**
     * Check to see if either of the expressions we've been given is a Date as
     * SQL understands it.
     *
     * @param left
     * @param right
     * @return
     */
    static private Boolean dateExpr ( final Object left, final Object right ) {
        // the call to `subString()` is because our date comes wrapped in single
        // quotes.
        try {
            if ( null != parseDate( left.toString().substring( 1,
                    left.toString().length() - 1 ) ) ) {
                return true;
            }
            return false;
        }
        catch ( final Exception e ) {
            try {
                if ( null != parseDate( right.toString().substring( 1,
                        right.toString().length() - 1 ) ) ) {
                    return true;
                }
                return false;
            }
            catch ( final Exception e2 ) {
                return false;
            }
        }
    }

    static private Long parseDate ( final String date ) {
        return parseDateZDT( date ).toEpochSecond();
    }

    static private String trimQuotes ( final String date ) {
        return date.substring( 1, date.length() - 1 );
    }

    static private ZonedDateTime parseDateZDT ( final String date ) {
        try {
            return LocalDate.parse( date )
                    .atStartOfDay( ZoneId.systemDefault() );
        }
        catch ( final DateTimeParseException e ) {
            return null;
        }
    }

    private static String parseDateFunction ( final Function function ) {

        switch ( function.toString() ) {
            case "curdate()":
                return LocalDateTime.now().toLocalDate().toString();
            default:
                return null;
        }

    }

}
