package edu.ncsu.sqlrepair.z3components;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Sort;
import com.microsoft.z3.Symbol;

/**
 * Singleton class used to maintain a reference to our Z3 solver and context
 * over which all objects are created. Also used to store objects that we'll
 * need to have access to in both the SQL visitor and our own methods.
 *
 * @author Kai Presler-Marshall
 *
 */
@SuppressWarnings ( "rawtypes" )
public class Z3Components {

    private Solver              s                  = null;

    private Context             ctx                = null;

    private static Z3Components instance           = null;

    /*
     * Various different sets of mappings between Z3 objects that we need and
     * names that makes it easy to reference them. We have enhanced getter
     * methods for accessing the objects that will return them if they exist and
     * create and store them if not already present
     */

    /**
     * Mapping between a row in the table and whether the SQL statement matches
     * across all its columns
     */
    Map<String, BoolExpr>       z3TableRows        = new HashMap<String, BoolExpr>();

    /**
     * Mapping between a column in the table and a representation of it. Source
     * values from the user
     */
    Map<String, FuncDecl>       srcZ3TableColumns  = new HashMap<String, FuncDecl>();

    /**
     * Mapping between a column in the table and a representation of it.
     * Intermediate function used for mappings
     */
    Map<String, FuncDecl>       intZ3TableColumns  = new HashMap<String, FuncDecl>();

    /**
     * Mapping between a column in the table and a representation of it.
     * Destination (desired) values from the user
     */
    Map<String, FuncDecl>       destZ3TableColumns = new HashMap<String, FuncDecl>();

    /*
     * Functions for accessing the maps above, with getters to automatically
     * create what we need if it doesn't exist already
     */

    public FuncDecl getSrcColumn ( final Integer exampleNumber,
            final Integer column, final Class typeOfColumn ) {
        final String exampleAndColumn = String.format( "%d_%d", exampleNumber,
                column );

        FuncDecl columnFunc = srcZ3TableColumns.get( exampleAndColumn );

        if ( null == columnFunc ) {
            final Symbol srcValues = ctx.mkSymbol(
                    exampleNumber.toString() + "_srcValues_" + column );

            columnFunc = ctx.mkFuncDecl( srcValues, ctx.getIntSort(),
                    getSort( typeOfColumn ) );

            srcZ3TableColumns.put( exampleAndColumn, columnFunc );
        }

        return columnFunc;

    }

    public void putSrcColumn ( final Integer exampleNumber,
            final Integer column, final FuncDecl func ) {
        final String exampleAndColumn = String.format( "%d_%d", exampleNumber,
                column );
        srcZ3TableColumns.put( exampleAndColumn, func );
    }

    public FuncDecl getIntColumn ( final Integer exampleNumber,
            final Integer column, final Class typeOfColumn ) {

        final String exampleAndColumn = String.format( "%d_%d", exampleNumber,
                column );
        FuncDecl columnFunc = intZ3TableColumns.get( exampleAndColumn );

        if ( null == columnFunc ) {
            final Symbol intermediateValues = ctx
                    .mkSymbol( exampleNumber.toString() + "_intermediateValues_"
                            + column );

            columnFunc = ctx.mkFuncDecl( intermediateValues, ctx.getIntSort(),
                    getSort( typeOfColumn ) );

            intZ3TableColumns.put( exampleAndColumn, columnFunc );
        }

        return columnFunc;

    }

    public void putIntColumn ( final Integer exampleNumber,
            final Integer column, final FuncDecl func ) {
        final String exampleAndColumn = String.format( "%d_%d", exampleNumber,
                column );
        intZ3TableColumns.put( exampleAndColumn, func );
    }

    public FuncDecl getDestColumn ( final Integer exampleNumber,
            final Integer column, final Class typeOfColumn ) {
        final String exampleAndColumn = String.format( "%d_%d", exampleNumber,
                column );
        FuncDecl columnFunc = destZ3TableColumns.get( exampleAndColumn );

        if ( null == columnFunc ) {
            final Symbol destinationValues = ctx.mkSymbol(
                    exampleNumber.toString() + "_destinationValues_" + column );

            columnFunc = ctx.mkFuncDecl( destinationValues, ctx.getIntSort(),
                    getSort( typeOfColumn ) );

            destZ3TableColumns.put( exampleAndColumn, columnFunc );
        }

        return columnFunc;

    }

    public void putDestColumn ( final Integer exampleNumber,
            final Integer column, final FuncDecl func ) {
        final String exampleAndColumn = String.format( "%d_%d", exampleNumber,
                column );
        destZ3TableColumns.put( exampleAndColumn, func );
    }

    public BoolExpr getRow ( final Integer exampleNumber, final Integer row ) {
        final String modifiedRow = String.format( "%d_%d", exampleNumber, row );
        BoolExpr rowExpr = z3TableRows.get( modifiedRow );
        if ( null == rowExpr ) {
            rowExpr = ctx.mkTrue();
            z3TableRows.put( modifiedRow, rowExpr );
        }

        return rowExpr;
    }

    public void storeRow ( final Integer exampleNumber, final Integer row,
            final BoolExpr rowExpr ) {
        final String modifiedRow = String.format( "%d_%d", exampleNumber, row );
        z3TableRows.put( modifiedRow, rowExpr );
    }

    private Z3Components ( final Context ctx, final Solver s ) {
        this.ctx = ctx;
        this.s = s;
    }

    /**
     * Initialize our singleton object with new parameters. Useful for
     * recreating it for each SQL statement that we consider to recreate the
     * solver with a new set of constraints. Basically a getInstance that forces
     * recreation.
     *
     * @param ctx
     * @param s
     * @return
     */
    static public Z3Components initialize ( final Context ctx,
            final Solver s ) {
        instance = new Z3Components( ctx, s );
        return instance;

    }

    static public Z3Components getInstance () {
        return instance;
    }

    public Solver getSolver () {
        return s;
    }

    public Context getContext () {
        return ctx;
    }

    private Sort getSort ( final Class cls ) {
        if ( null == cls ) {
            return null;
        }
        if ( cls.equals( Boolean.class ) ) {
            return ctx.getBoolSort();
        }
        if ( cls.equals( Integer.class ) || cls.equals( Long.class ) ) {
            return ctx.getIntSort();
        }
        if ( cls.equals( String.class ) ) {
            return ctx.getStringSort();
        }
        throw new UnsupportedOperationException( "Invalid class provided" );
    }

}
