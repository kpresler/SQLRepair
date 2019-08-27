package edu.ncsu.sqlrepair.statementvisitors;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.FuncDecl;

import edu.ncsu.sqlrepair.z3components.Z3Components;
import edu.ncsu.sqlrepair.z3components.Z3Utils;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

/**
 * Visitor for the JSQLParser. Called when we traverse the SQL statement.
 *
 * @author Kai Presler-Marshall
 *
 */
public class SelectStatementVisitor extends StatementVisitorAdapter {

    @Override
    public void visit ( final Select select ) {

        /* Go retrieve the Z3 context and solver that have been defined */
        final Z3Components z3 = Z3Components.getInstance();

        final Context ctx = z3.getContext();

        /* Figure out what we're working with here */
        final PlainSelect selectBody = (PlainSelect) select.getSelectBody();

        final Integer exampleNumber = Integer
                .valueOf( Z3Utils.get( "exampleIndex" ).toString() );

        /* The column we're trying to match on */
        final String columnHeader = Z3Utils.get( "columnHeader" ).toString();

        /* Retrieve the source values (input to expression) */
        final FuncDecl sourceValues = z3.getSrcColumn( exampleNumber,
                (Integer) Z3Utils.get( "columnNum" ), null );

        final Integer row = (Integer) Z3Utils.get( "row" );

        final BoolExpr rowMatches = z3.getRow( exampleNumber, row );

        final Expr domainValue = sourceValues.apply( ctx.mkInt( row ) );

        /*
         * Figure out whether or not we need to change our expression from the
         * current statement and column combination we are visiting
         */
        final BoolExpr updatedRowMatches = Z3Utils.createBoolFromArithmetic(
                rowMatches, columnHeader, domainValue, selectBody.getWhere() );

        if ( null != updatedRowMatches ) {
            z3.storeRow( exampleNumber, row, updatedRowMatches );
        }

    }

}
