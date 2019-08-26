package edu.ncsu.sqlrepair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.ncsu.sqlrepair.datatypes.Z3Type;
import edu.ncsu.sqlrepair.z3components.Z3Utils;

/**
 * Represents an example of a (source, destination) tuple from a user of what
 * transformation they want to accomplish.
 *
 * Source table stored as an entire string, newlines (`\n`) represent subsequent
 * rows, spaces (` `) are used to separate elements on a row.
 *
 * Class gets constructed automagically by reflection from Spring's API, so you
 * need to call `parse()` on it to actually split things apart and fill in the
 * other variables
 *
 * @author Kai Presler-Marshall
 *
 */
public class SQLTableExample {

    /** Entire source table */
    private String             source;

    /** Entire destination table */
    private String             dest;

    /** Just the column headers from the source table */
    private List<String>       sourceHeaders;

    /**
     * All of the values (- the headers) from the source table. Inner list
     * represents a column, outer list represents all columns
     */
    private List<List<Z3Type>> sourceValues;

    /** Just the headers from the destination table */
    private List<String>       destHeaders;

    /**
     * All of the values (- the headers) from the destination table. Inner list
     * represents a column, outer list represents all columns
     */
    private List<List<Z3Type>> destValues;

    public String getDest () {
        return dest;
    }

    public SQLTableExample setDest ( final String dest ) {
        this.dest = dest;
        return this;
    }

    public String getSource () {
        return source;
    }

    public SQLTableExample setSource ( final String source ) {
        this.source = source;
        return this;
    }

    @Override
    public String toString () {
        return "SQLExample [source=" + source + ", dest=" + dest + "]";
    }

    /**
     * Parses the source and destination tables into something that we can do a
     * bit more with. Doesn't get called by Spring's reflection, so must be done
     * manually. Fills out all of the other fields above
     */
    public void parse () {

        final String[] sourceLines = source.split( "\n" );

        /*
         * Trim and filter out empty string to be a bit more forgiving of bad
         * formating of the input data
         */
        sourceHeaders = Arrays.asList( sourceLines[0].split( " " ) ).stream()
                .map( e -> e.trim() ).filter( e -> !e.isEmpty() )
                .collect( Collectors.toList() );

        sourceValues = new ArrayList<>();

        final List<List<String>> cleanedRows = new ArrayList<List<String>>();

        /*
         * Clean each row in the incoming source table by trimming out
         * whitepsace and empty elements
         */
        for ( int i = 1; i < sourceLines.length; i++ ) {

            // Clean the row
            final List<String> cleanedRow = Arrays
                    .asList( sourceLines[i].split( "\\s+" ) ).stream()
                    .map( e -> e.trim() ).filter( e -> !e.isEmpty() )
                    .collect( Collectors.toList() );
            cleanedRows.add( cleanedRow );

        }

        /*
         * For each column, figure out type we'll need for it. This is relevant
         * because although SQL has types, the tables provided by the user
         * probably will not, so we'll need to figure out the type capable of
         * storing everything
         */
        final List<Class<Z3Type>> typeForColumn = new ArrayList<Class<Z3Type>>();

        for ( int i = 0; i < sourceHeaders.size(); i++ ) {

            final List<String> valuesFromColumn = new ArrayList<String>();
            for ( int j = 0; j < cleanedRows.size(); j++ ) {
                valuesFromColumn.add( cleanedRows.get( j ).get( i ) );
            }

            typeForColumn.add( Z3Type.findType( valuesFromColumn ) );

        }

        /* Actually go do the conversions based on what was found above */

        // for every row
        for ( int i = 0; i < cleanedRows.size(); i++ ) {
            final List<String> cleanedRow = cleanedRows.get( i );

            final List<Z3Type> row = new ArrayList<Z3Type>();

            // for every element in that row
            for ( int j = 0; j < cleanedRow.size(); j++ ) {
                // parse that element based on the type we found of the column
                // it belongs to
                row.add( Z3Type.parse( cleanedRow.get( j ),
                        typeForColumn.get( j ) ) );
            }

            sourceValues.add( row );

        }

        // -1 because of the header
        Z3Utils.put( "sourceSize", sourceLines.length - 1 );

        final String[] destLines = dest.split( "\n" );

        /*
         * Trim and filter out empty string to be a bit more forgiving of bad
         * formating of the input data
         */
        destHeaders = Arrays.asList( destLines[0].split( " " ) ).stream()
                .map( e -> e.trim() ).filter( e -> !e.isEmpty() )
                .collect( Collectors.toList() );

        destValues = new ArrayList<>();

        /* Clear our lists above for reuse */
        cleanedRows.clear();
        typeForColumn.clear();

        /* Otherwise, we do the same thing here that we did above */
        for ( int i = 1; i < destLines.length; i++ ) {

            // Clean the row
            final List<String> cleanedRow = Arrays
                    .asList( destLines[i].split( "\\s+" ) ).stream()
                    .map( e -> e.trim() ).filter( e -> !e.isEmpty() )
                    .collect( Collectors.toList() );

            cleanedRows.add( cleanedRow );

            // // Figure out the type we need for storing all of it
            // final Class<Z3Type> rowClass = Z3Type.findType( cleanedRow );
            //
            // // And convert
            // final List<Z3Type> row = cleanedRow.stream().map( e ->
            // Z3Type.parse( e, rowClass ) )
            // .collect( Collectors.toList() );
            //
            // destValues.add( row );
        }

        for ( int i = 0; i < destHeaders.size(); i++ ) {

            final List<String> valuesFromColumn = new ArrayList<String>();
            for ( int j = 0; j < cleanedRows.size(); j++ ) {
                valuesFromColumn.add( cleanedRows.get( j ).get( i ) );
            }

            typeForColumn.add( Z3Type.findType( valuesFromColumn ) );

        }

        for ( int i = 0; i < cleanedRows.size(); i++ ) {
            final List<String> cleanedRow = cleanedRows.get( i );

            final List<Z3Type> row = new ArrayList<Z3Type>();

            for ( int j = 0; j < cleanedRow.size(); j++ ) {
                row.add( Z3Type.parse( cleanedRow.get( j ),
                        typeForColumn.get( j ) ) );
            }

            destValues.add( row );

        }

        // -1 because of the header
        Z3Utils.put( "destSize", destLines.length - 1 );

    }

    public List<List<Z3Type>> getSourceValues () {
        return sourceValues;
    }

    public List<List<Z3Type>> getDestValues () {
        return destValues;
    }

    public List<String> getSourceHeaders () {
        return sourceHeaders;
    }

    public List<String> getDestHeaders () {
        return destHeaders;
    }

}
