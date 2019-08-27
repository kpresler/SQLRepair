package edu.ncsu.sqlrepair.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Status;
import com.microsoft.z3.Symbol;

import edu.ncsu.sqlrepair.SQLParser;
import edu.ncsu.sqlrepair.SQLTableExample;
import edu.ncsu.sqlrepair.SQLTableExamples;
import edu.ncsu.sqlrepair.datatypes.Z3Type;
import edu.ncsu.sqlrepair.models.SQLStatement;
import edu.ncsu.sqlrepair.statementvisitors.SelectStatementVisitor;
import edu.ncsu.sqlrepair.z3components.SQLRewrite;
import edu.ncsu.sqlrepair.z3components.Z3Components;
import edu.ncsu.sqlrepair.z3components.Z3Utils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * Main controller class for the SQLRepair application. Handles receiving a
 * broken query and examples from a user and returning a corrected version
 *
 * @author Kai Presler-Marshall
 *
 */
@RestController
@SuppressWarnings ( "rawtypes" )
public class APIZ3Controller extends APIController {

    /**
     * Main API endpoint. Receives a SQLTableExamples class that consists of one
     * or more examples to use for the repair process together with the query to
     * attempt repair on.
     *
     * @param examples
     *            Query and examples to perform repair with
     * @return
     */
    @SuppressWarnings ( "unchecked" )
    @PostMapping ( BASE_PATH + "submitExample" )
    public ResponseEntity submitExample (
            final @RequestBody SQLTableExamples examples ) {

        if ( debug ) {
            System.out.println( "Received " + examples );
        }

        /*
         * Parse each of the examples from the user, preemptively returning an
         * error if any of them fail the parsing (save resources with the
         * solver)
         */
        for ( final SQLTableExample example : examples ) {
            try {
                example.parse();

            }
            catch ( final Exception e ) {
                return new ResponseEntity( errorResponse(
                        "There was a fatal error in one or more of the examples provided" ),
                        HttpStatus.BAD_REQUEST );
            }

        }

        /* Convert into our version that supports rewriting and diffs */
        final SQLRewrite possibleMatch = new SQLRewrite(
                examples.getQueryToRepair() );
        Boolean found = true;

        try {
            found = findSQLMatch( examples, possibleMatch );

            if ( !found ) {
                return new ResponseEntity( possibleMatch,
                        HttpStatus.NOT_FOUND );
            }
            return new ResponseEntity( possibleMatch, HttpStatus.OK );

        }
        /* Thrown by the parser if there was an uncorrectable error */
        catch ( final JSQLParserException e ) {
            return new ResponseEntity(
                    errorResponse( "Uncorrectable syntax error" ),
                    HttpStatus.BAD_REQUEST );

        }
        /* Other issues */
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Unknown error occurred" ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * Attempt syntax repairs on the query provided.
     *
     * @param sql
     *            The query to fix.
     */
    private final void fixSyntax ( final SQLRewrite sql ) {
        String currentStatement = sql.getCurrentStatement();
        Boolean wasChanged = false;
        if ( currentStatement.contains( "&" ) ) {
            /*
             * A&&B is unambiguous to the parser, but AANDB is not, hence
             * spaces. Parser will strip out spares later anyways
             */
            currentStatement = currentStatement.replaceAll( "&&", " AND " );
            currentStatement = currentStatement.replace( "&", " AND " );
            wasChanged |= true;
        }
        if ( currentStatement.contains( "|" ) ) {
            /* Likewise */
            currentStatement = currentStatement.replaceAll( "\\|\\|", " OR " );
            currentStatement = currentStatement.replaceAll( "\\|", " OR " );
            wasChanged |= true;
        }
        if ( currentStatement.contains( "==" ) ) {
            /* double equals is not used */
            currentStatement = currentStatement.replace( "==", "=" );
            wasChanged |= true;
        }
        if ( wasChanged ) {
            sql.update( currentStatement, false );
        }

    }

    /**
     * Parse query to ensure there are no syntax errors in it
     *
     * @param sql
     */
    private final void parse ( final SQLRewrite sql ) {
        try {
            sql.setNewStatement( new SQLParser( sql.getCurrentStatement() )
                    .getSQLStatements().getStatements().get( 0 ).toString() );
        }
        catch ( final JSQLParserException e ) {
            throw new RuntimeException( e );
        }

    }

    /**
     * Attempt to find a match by repairing the query provided.
     *
     * @param examples
     *            Examples
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    private boolean findSQLMatch ( final SQLTableExamples examples,
            final SQLRewrite sql ) throws JSQLParserException {
        final Context ctx = new Context();
        final Solver s = ctx.mkSolver();

        /*
         * Store our solver and context so that our parser code can access it
         * too
         */
        Z3Components.initialize( ctx, s );

        final Z3Components z3 = Z3Components.getInstance();

        // Start by fixing syntactic mistakes
        fixSyntax( sql );

        // Then make sure the syntax is fine and clean up spacing and the like
        parse( sql );

        /*
         * Handle one (source, destination) example pair from the user at a
         * time. This is used to encode our constraints separately for each
         */
        Integer exampleIndex = 0;

        /* For each example, encode constraints for it */
        for ( final SQLTableExample example : examples ) {

            /*
             * Assuming that we weren't stupid enough to create a SQL example
             * that was really multiple jammed together, this is OK. If we were,
             * we get what we deserve.
             */
            SQLParser parser = new SQLParser( sql.getCurrentStatement() );
            Select statement = (Select) parser.getSQLStatements()
                    .getStatements().get( 0 );

            if ( modifyColumns( statement, example, sql ) ) {
                parser = new SQLParser( sql.getCurrentStatement() );
                statement = (Select) parser.getSQLStatements().getStatements()
                        .get( 0 );
            }

            /*
             * Handle the DISTINCT operator, if provided, by removing subsequent
             * occurrences of each row. We want to remove _subsequent_ instances
             * to ensure that ordering is OK.
             */
            final Boolean isDistinct = null != ( (PlainSelect) statement
                    .getSelectBody() ).getDistinct();

            if ( isDistinct ) {
                int index = 0;
                for ( final Iterator<List<Z3Type>> it = example
                        .getSourceValues().iterator(); it.hasNext(); ) {
                    final List aRow = it.next();
                    for ( int i = 0; i < index; i++ ) {
                        final List possibleMatchRow = example.getSourceValues()
                                .get( i );
                        if ( possibleMatchRow.equals( aRow ) ) {
                            it.remove();
                            break;
                        }
                    }

                    index++;
                }

            }

            final SelectStatementVisitor visitor = new SelectStatementVisitor();

            final List<String> headers = example.getSourceHeaders();

            final Integer numColumns = headers.size();

            final List<List<Z3Type>> srcVals = example.getSourceValues();

            final List<List<Z3Type>> destVals = example.getDestValues();

            /*
             * Assuming that we'll have the same number of values in each
             * column. nulls get stringified so that's fine.
             */
            final Integer numRows = srcVals.size();

            /* Figure out if the query contains an ORDER BY statement */
            final List<OrderByElement> order = ( (PlainSelect) statement
                    .getSelectBody() ).getOrderByElements();
            if ( null != order ) {

                /*
                 * For each ORDER BY statement provided, sort the table
                 * according to that. Since Java's sort implementation is stable
                 * this should be fine to do one pass at a time
                 */
                for ( final OrderByElement obe : order ) {

                    final int columnIndex = example.getSourceHeaders()
                            .indexOf( obe.getExpression().toString() );

                    srcVals.sort( ( a, b ) -> obe.isAsc()
                            ? a.get( columnIndex )
                                    .compareTo( b.get( columnIndex ) )
                            : b.get( columnIndex )
                                    .compareTo( a.get( columnIndex ) ) );

                }

            }

            Z3Utils.put( "exampleIndex", exampleIndex );

            /*
             * Until proven otherwise, go say that each of the columns match our
             * expression by initializing it to true. We'll `and` the result of
             * each row with the running match to make sure the whole thing
             * matches correctly
             */
            final Map<Integer, BoolExpr> columnMatches = new HashMap<Integer, BoolExpr>();
            for ( Integer i = 0; i < numColumns; ++i ) {
                columnMatches.put( i, ctx.mkTrue() );
            }

            for ( Integer rowNumber = 0; rowNumber < numRows; ++rowNumber ) {

                /*
                 * For each row, we first want to make sure that the condition
                 * (`AND` or `OR` as appropriate) is satisfied
                 */

                Z3Utils.put( "row", rowNumber );

                for ( Integer columnNumber = 0; columnNumber < numColumns; ++columnNumber ) {

                    Z3Utils.put( "columnNum", columnNumber );

                    final String columnHeader = headers.get( columnNumber );

                    final Z3Type columnValue = srcVals.get( rowNumber )
                            .get( columnNumber );

                    final Class typeOfColumn = columnValue.getDataClass();

                    Z3Utils.put( "columnHeader", columnHeader );

                    /* Source values */
                    final FuncDecl sourceValuesFunc = z3.getSrcColumn(
                            exampleIndex, columnNumber, typeOfColumn );

                    BoolExpr eq = columnMatches.get( columnNumber );

                    final Expr domainValue = sourceValuesFunc
                            .apply( ctx.mkInt( rowNumber ) );

                    eq = ctx.mkAnd( eq, ctx.mkEq( domainValue,
                            makeZ3Expr( ctx, columnValue ) ) );

                    /* Update our running match */
                    columnMatches.put( columnNumber, eq );

                    visitor.visit( statement );
                }

            }

            /*
             * Go initialize a forall quantifier for each column that's in the
             * table that we'll use to then set values on
             */
            for ( Integer columnNumber = 0; columnNumber < numColumns; ++columnNumber ) {
                final String columnHeader = headers.get( columnNumber );

                final BoolExpr sourceValuesMatch = ctx.mkForall(
                        new Sort[] { ctx.getIntSort() },
                        new Symbol[] { ctx.mkSymbol( columnHeader ) },
                        columnMatches.get( columnNumber ), 0, null, null, null,
                        null );

                s.add( sourceValuesMatch );

            }

            /*
             * Set the desired values (the example from the user of what they
             * want to end up with)
             */

            final Integer numDestRows = example.getDestValues().size();

            final Integer numDestColumns = example.getDestHeaders().size();

            for ( Integer columnNumber = 0; columnNumber < numDestColumns; ++columnNumber ) {

                final String columnHeader = headers.get( columnNumber );

                final Class typeOfColumn = destVals.get( 0 ).get( columnNumber )
                        .getDataClass();

                final FuncDecl desiredValuesFunc = z3.getDestColumn(
                        exampleIndex, columnNumber, typeOfColumn );

                BoolExpr eq2 = ctx.mkTrue();

                for ( int i = 0; i < numDestRows; i++ ) {
                    final Expr domainValue = desiredValuesFunc
                            .apply( ctx.mkInt( i ) );
                    eq2 = ctx.mkAnd( eq2,
                            ctx.mkEq( domainValue, makeZ3Expr( ctx,
                                    destVals.get( i ).get( columnNumber ) ) ) );

                }

                final BoolExpr destArray = ctx.mkForall(
                        new Sort[] { ctx.getIntSort() },
                        new Symbol[] { ctx.mkSymbol( columnHeader ) }, eq2, 0,
                        null, null, null, null );

                s.add( destArray );

            }

            /* Check if we're working with a COUNT(*) statement */
            final PlainSelect sel = (PlainSelect) statement.getSelectBody();

            Boolean count = false;

            if ( !sel.getSelectItems().isEmpty() && sel.getSelectItems()
                    .get( 0 ) instanceof SelectExpressionItem ) {
                final SelectExpressionItem selItem = (SelectExpressionItem) sel
                        .getSelectItems().get( 0 );

                if ( selItem.getExpression() instanceof Function ) {

                    count = true;
                }

            }

            /*
             * Then once we know it's satisfied across all columns in the row,
             * we'll go copy over values and make sure they match
             */
            System.out.println(
                    "Comparing against " + numRows + " that might match" );
            System.out.println(
                    "Looking for " + numDestRows + " in destination " );

            Integer rowsThatShouldBeFound = null;
            if ( count ) {
                /*
                 * If we're trying to match a count statement, we should have
                 * just one value: the number of rows that match
                 */
                rowsThatShouldBeFound = (int) example.getDestValues().get( 0 )
                        .get( 0 ).getValue();
            }

            else {
                /*
                 * Use the same approach as before to ensure that the solver
                 * doesn't try to sneak in any additional rows that it shouldn't
                 */
                rowsThatShouldBeFound = example.getDestValues().size();
            }

            /*
             * Approach comes from here:
             * https://github.com/Z3Prover/z3/issues/694
             */
            try {
                final List<BoolExpr> rowsThatMatch = new ArrayList<BoolExpr>();
                final int[] coefficients = new int[numRows];
                for ( Integer rowNumber = 0; rowNumber < numRows; rowNumber++ ) {
                    rowsThatMatch.add( z3.getRow( exampleIndex, rowNumber ) );
                    coefficients[rowNumber] = 1;
                }

                s.add( ctx.mkEq( ctx.mkPBEq( coefficients,
                        rowsThatMatch.toArray( new BoolExpr[] {} ),
                        rowsThatShouldBeFound ), ctx.mkTrue() ) );

            }
            catch ( final IndexOutOfBoundsException ioobe ) {
                ioobe.printStackTrace( System.err );
                sql.update( null, false );
                return false;
            }

            for ( Integer rowNumber = 0; rowNumber < numRows; rowNumber++ ) {
                final BoolExpr rowMatches = z3.getRow( exampleIndex,
                        rowNumber );

                for ( Integer columnNumber = 0; columnNumber < numDestColumns; ++columnNumber ) {

                    final FuncDecl desiredValuesFunc = z3
                            .getDestColumn( exampleIndex, columnNumber, null );

                    final FuncDecl intermediateValuesFunction = z3.getIntColumn(
                            exampleIndex, columnNumber, srcVals.get( rowNumber )
                                    .get( columnNumber ).getDataClass() );

                    final Expr domainValue = intermediateValuesFunction
                            .apply( ctx.mkInt( rowNumber ) );

                    final Expr destValue = desiredValuesFunc
                            .apply( ctx.mkInt( rowNumber ) );
                    final BoolExpr columnValuesMatch = ctx.mkEq( destValue,
                            domainValue );

                    s.add( ctx.mkIff( columnValuesMatch, rowMatches ) );
                }
            }

            for ( Integer column = 0; column < numDestColumns; ++column ) {
                /*
                 * Go retrieve the "table" of the values that we want to have
                 * seen
                 */

                /*
                 * Check if things match between the values that we think
                 * matched and the valued the user wants to see
                 */
                for ( Integer i = 0; i < numDestRows; i++ ) {
                    s.add( ctx.mkEq(
                            z3.getIntColumn( exampleIndex, column, null )
                                    .apply( ctx.mkInt( i ) ),
                            z3.getDestColumn( exampleIndex, column, null )
                                    .apply( ctx.mkInt( i ) ) ) );
                }
            }

            /*
             * If the SQLStatement contains a list of columns to select (as
             * opposed to a `SELCT *` make sure it matches the columns that the
             * user wants to receive
             */
            final List<SelectItem> itemsToSelect = ( (PlainSelect) statement
                    .getSelectBody() ).getSelectItems();
            if ( itemsToSelect != null && !itemsToSelect.isEmpty()
                    && ! ( itemsToSelect.get( 0 ) instanceof AllColumns ) ) {

                // https://stackoverflow.com/a/933600
                @SuppressWarnings ( "unchecked" )
                final List<SelectExpressionItem> items = (List<SelectExpressionItem>) (List< ? >) itemsToSelect;

                final List<String> columnsToFind = example.getDestHeaders();

                /*
                 * If we're renaming a column using an AS statement, take that
                 * into consideration
                 */
                final List<String> foundColumnNames = items.stream().map( e -> {
                    if ( e.getAlias() != null
                            && e.getAlias().getName() != null ) {
                        return e.getAlias().getName();
                    }
                    else {
                        return e;
                    }
                } ).map( e -> e.toString() ).collect( Collectors.toList() );
                s.add( ctx.mkBool( foundColumnNames.equals( columnsToFind ) ) );
            }

            /*
             * Try to make sure that the solver doesn't sneak any extra values
             * into the intermediateValues not found in Source. Approach: For
             * all values in the intermediate function, ensure that they also
             * exist in the source function
             */

            for ( int columnNumber = 0; columnNumber < numDestColumns; columnNumber++ ) {
                for ( Integer intermediateValuesIndex = 0; intermediateValuesIndex < numDestRows; intermediateValuesIndex++ ) {

                    final FuncDecl intermediateValues = z3
                            .getIntColumn( exampleIndex, columnNumber, null );

                    final Expr intermediateValue = intermediateValues
                            .apply( ctx.mkInt( intermediateValuesIndex ) );

                    BoolExpr foundValue = ctx.mkFalse();
                    for ( Integer sourceValuesIndex = 0; sourceValuesIndex < numRows; sourceValuesIndex++ ) {

                        final FuncDecl sourceValues = z3.getSrcColumn(
                                exampleIndex, columnNumber, null );

                        final Expr sourceValue = sourceValues
                                .apply( ctx.mkInt( sourceValuesIndex ) );

                        foundValue = ctx.mkOr( foundValue,
                                ctx.mkEq( intermediateValue, sourceValue ) );

                    }

                    s.add( foundValue );
                }
            }

            exampleIndex++;

        }

        /*
         * Check if all of the constraints are solvable. This applies to _every_
         * example. Print out extra information to the console if prompted.
         */
        System.out.println( "Solving...." );
        if ( s.check().equals( Status.SATISFIABLE ) ) {
            System.out.println( "Solved...successfully!" );

            final Model model = s.getModel();

            if ( debug ) {
                System.out.println(
                        "-------------------CONSTRAINTS-------------------" );
                for ( final BoolExpr expr : s.getAssertions() ) {
                    System.out.println( expr );
                }

                System.out.println(
                        "-------------------MODEL-------------------" );

                System.out.println( "Full model ::" );
                System.out.println( model );

            }
            try {
                // use the model to go update our statement
                updateStatement( model, sql );
            }
            catch ( final Exception e ) {
                // some statements can't be gracefully updated, like
                // dates...return what we have and hope it's OK. _sigh_.
            }

            return true;

        }
        else {
            System.out.println( "Could not satisfy constraints!" );

            if ( debug ) {
                System.out.println(
                        "-------------------CONSTRAINTS-------------------" );
                for ( final BoolExpr expr : s.getAssertions() ) {
                    System.out.println( expr );
                }
            }

            // Can't satisfy constraints
            sql.update( sql.getCurrentStatement(), false );
            return false;
        }

    }

    /**
     * Update our SQL query using the information from the model that Z3
     * returns.
     *
     * @param model
     *            Model from Z3 representing what it figured out
     * @param sql
     *            SQL query to update
     */
    private void updateStatement ( final Model model, final SQLRewrite sql ) {

        final String originalStatement = sql.getCurrentStatement();

        /*
         * From the model, extract out each constraint it came up with (ie,
         * which numbers to substitute to make something work)
         */
        final List<Integer> numericalConstraints = new ArrayList<Integer>();

        String modifiedStatement = originalStatement.replaceAll( "%", "%%" );

        /*
         * Now comes the time of actually retrieving the model that was produced
         * to see how the constraints could be satisfied
         */
        for ( final FuncDecl o : model.getDecls() ) {

            final String funcName = o.getName().toString();

            /*
             * Keep track of all constants that the solver came up with so that
             * we can insert them in later
             */
            if ( funcName.contains( Z3Utils.S_CONST_VAL ) ) {
                numericalConstraints.add( Integer.valueOf(
                        String.valueOf( model.getConstInterp( o ) ) ) );
            }

            /*
             * For each operator that could work, we have six booleans,
             * representing whether it is true or not. Go find the one that _is_
             * true, and that's the one that we want to rewrite the statement
             * with
             */
            else if ( funcName.contains( Z3Utils.S_OP_VAL ) ) {

                final Boolean val = Boolean
                        .valueOf( String.valueOf( model.getConstInterp( o ) ) );
                if ( val ) {

                    final String operatorReplaced = funcName.split( "::" )[0];

                    final String[] operatorParts = operatorReplaced
                            .split( " " );

                    try {
                        Integer.parseInt( operatorParts[0] );
                        Integer.parseInt( operatorParts[2] );

                    }
                    /*
                     * We should only be replacing an operator if it wasn't part
                     * of a `1=1`-style test
                     */
                    catch ( final Exception e ) {
                        final String newOperator = String.format( "%s %s %s",
                                operatorParts[0], findOperator( funcName ),
                                operatorParts[2] );

                        modifiedStatement = modifiedStatement
                                .replace( operatorReplaced, newOperator )
                                .replaceFirst( "(\\+|-)?\\d+", "%d" );
                    }

                }

            }

        }

        modifiedStatement = String.format( modifiedStatement,
                numericalConstraints.toArray() );

        /*
         * Finally make sure we didn't screw up and generate invalid SQL by
         * rerunning it through our parser
         */
        modifiedStatement = new SQLStatement( modifiedStatement )
                .getStatement();

        sql.update( modifiedStatement, true );

    }

    /**
     * Figure out the appropriate SQL operator to insert based on the model that
     * was generated
     *
     * @param z3FuncDecl
     *            The function declaration that we told the model to figure out
     *            truthyness of
     * @return Corresponding SQL operator
     */
    private String findOperator ( final String z3FuncDecl ) {
        if ( z3FuncDecl.contains( "GTE" ) ) {
            return ">=";
        }
        if ( z3FuncDecl.contains( "GT" ) ) {
            return ">";
        }
        if ( z3FuncDecl.contains( "LTE" ) ) {
            return "<=";
        }
        if ( z3FuncDecl.contains( "LT" ) ) {
            return "<";
        }
        if ( z3FuncDecl.contains( "NEq" ) ) {
            return "!=";
        }
        if ( z3FuncDecl.contains( "Eq" ) ) {
            return "=";
        }
        throw new UnsupportedOperationException(
                "Unable to find a matching operator" );

    }

    /**
     * Create an Expression for the value provided
     *
     * @param ctx
     *            Context to create the Expression in
     * @param value
     *            Value to use
     * @return
     */
    private Expr makeZ3Expr ( final Context ctx, final Z3Type value ) {
        if ( value.getDataClass().equals( Integer.class ) ) {
            return ctx.mkInt( (Integer) value.getValue() );
        }
        if ( value.getDataClass().equals( Boolean.class ) ) {
            return ctx.mkBool( (Boolean) value.getValue() );
        }
        if ( value.getDataClass().equals( String.class ) ) {
            return ctx.mkString( (String) value.getValue() );
        }
        if ( value.getDataClass().equals( Long.class ) ) {
            return ctx.mkInt( (Long) value.getValue() );
        }
        else {
            throw new UnsupportedOperationException(
                    "I don't know how to parse this!" );
        }
    }

    /**
     * Fix the columns to be returned by the SQL query based on the examples
     * that were provided of what the user wishes to achieve.
     *
     * @param statement
     *            The original query to repair
     * @param example
     *            Example from the user of the transformation to perform
     * @param sql
     *            Updated version of the query
     * @return Did we change anything or not? True iff so.
     */
    private boolean modifyColumns ( final Select statement,
            final SQLTableExample example, final SQLRewrite sql ) {
        // if (headers in source!=headers in dest && SELECT *) replace
        // SELECT * with SELECT headersInDest
        if ( !example.getSourceHeaders().equals( example.getDestHeaders() )
                && ( (PlainSelect) statement.getSelectBody() ).getSelectItems()
                        .toString().contains( "*" ) ) {
            sql.update(
                    sql.getCurrentStatement().replaceAll( "\\*",
                            String.join( ",", example.getDestHeaders() ) ),
                    false );
            return true;

        }

        // Otherwise, process for figuring out how to replace the column
        // headers is more difficult. Basically, take each column header
        // from the source, and rename it to the one from the dest using an
        // AS if it doesn't match.
        else if ( ( !example.getSourceHeaders()
                .equals( example.getDestHeaders() )
                && example.getSourceHeaders().size() == example.getDestHeaders()
                        .size() ) ) {

            final List<String> columns = new ArrayList<String>();

            for ( int i = 0; i < example.getSourceHeaders().size(); i++ ) {
                final String columnFromSource = example.getSourceHeaders()
                        .get( i );
                final String columnFromDest = example.getDestHeaders().get( i );

                if ( !columnFromSource.equals( columnFromDest ) ) {
                    columns.add( columnFromSource + " AS " + columnFromDest );
                }
                else {
                    columns.add( columnFromSource );
                }

            }

            columns.removeIf( e -> e.trim().isEmpty() );
            sql.update( sql.getCurrentStatement().replaceFirst(
                    trimString( ( (PlainSelect) statement.getSelectBody() )
                            .getSelectItems().toString() ),
                    String.join( ",", columns ) ), false );

            return true;

        }

        // If we get a statement that's something like SELECT a,b,c but both
        // examples are d,e,f
        else if ( example.getSourceHeaders().size() == example.getDestHeaders()
                .size()
                && !example.getSourceHeaders()
                        .equals( ( (PlainSelect) statement.getSelectBody() )
                                .getSelectItems().stream()
                                .map( e -> e.toString() )
                                .collect( Collectors.toList() ) )
                && sql.getCurrentStatement().indexOf( '*' ) == -1 ) {

            final List<String> columns = example.getSourceHeaders();

            columns.removeIf( e -> e.trim().isEmpty() );
            sql.update( sql.getCurrentStatement().replaceFirst(
                    trimString( ( (PlainSelect) statement.getSelectBody() )
                            .getSelectItems().toString() ),
                    String.join( ",", columns ) ), false );

            return true;

        }

        return false;
    }

    /**
     * Trim first and last characters from string
     * 
     * @param str
     * @return
     */
    final private String trimString ( final String str ) {
        return str.substring( 1, str.length() - 1 );
    }

}
