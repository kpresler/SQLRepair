package edu.ncsu.sqlrepair.z3components;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a SQL statement that has potentially been rewritten
 *
 * @author Kai Presler-Marshall
 *
 */
public class SQLRewrite {

    /**
     * Original SQL query before any fixing has happened
     */
    private String  originalStatement;

    /**
     * New statement, with any changes made
     */
    private String  newStatement;

    /**
     * Whether a match was found or not
     */
    private Boolean found;

    /**
     * The difference between the original and new statements
     */
    private String  diff;

    /**
     * Whether we changed anything, defined as
     * !(originalStatement.equals(newStatement))
     */
    private Boolean wasChanged;

    public void update ( final String newS, final Boolean found ) {
        if ( null == newS ) {
            this.newStatement = null;
            this.found = false;
            this.diff = null;
            this.wasChanged = false;

            return;

        }

        if ( newS.equals( this.originalStatement ) ) {
            wasChanged = false;
        }
        else {
            this.newStatement = newS;
            this.diff = diff( this.originalStatement, this.newStatement )
                    .toString();
        }

        this.found = found;
    }

    public String getCurrentStatement () {
        return newStatement == null ? getOriginalStatement()
                : getNewStatement();
    }

    public String getOriginalStatement () {
        return originalStatement;
    }

    public void setOriginalStatement ( final String originalStatement ) {
        this.originalStatement = originalStatement;
    }

    public String getNewStatement () {
        return newStatement;
    }

    public void setNewStatement ( final String newStatement ) {
        this.newStatement = newStatement;
    }

    public Boolean getFound () {
        return found;
    }

    public void setFound ( final Boolean found ) {
        this.found = found;
    }

    public String getDiff () {
        return diff;
    }

    public void setDiff ( final String diff ) {
        this.diff = diff;
    }

    public Boolean getWasChanged () {
        return wasChanged;
    }

    public void setWasChanged ( final Boolean wasChanged ) {
        this.wasChanged = wasChanged;
    }

    @Override
    public String toString () {
        return "SQLRewrite [originalStatement=" + originalStatement
                + ", newStatement=" + newStatement + ", found=" + found
                + ", diff=" + diff + ", wasChanged=" + wasChanged + "]";
    }

    public SQLRewrite ( final String original ) {
        this.originalStatement = original;

    }

    /*
     * Approach for computing a minimal diff between two strings comes from
     * https://stackoverflow.com/a/52743737/9275267
     */

    /**
     * Returns a minimal set of characters that have to be removed from (or
     * added to) the respective strings to make the strings equal.
     */
    private static Pair<String> diff ( final String a, final String b ) {
        return diffHelper( a, b, new HashMap<>() );
    }

    /**
     * Recursively compute a minimal set of characters while remembering already
     * computed substrings. Runs in O(n^2).
     */
    private static Pair<String> diffHelper ( final String a, final String b,
            final Map<Long, Pair<String>> lookup ) {
        final long key = ( (long) a.length() ) << 32 | b.length();
        if ( !lookup.containsKey( key ) ) {
            Pair<String> value;
            if ( a.isEmpty() || b.isEmpty() ) {
                value = new Pair<>( a, b );
            }
            else if ( a.charAt( 0 ) == b.charAt( 0 ) ) {
                value = diffHelper( a.substring( 1 ), b.substring( 1 ),
                        lookup );
            }
            else {
                final Pair<String> aa = diffHelper( a.substring( 1 ), b,
                        lookup );
                final Pair<String> bb = diffHelper( a, b.substring( 1 ),
                        lookup );
                if ( aa.first.length() + aa.second.length() < bb.first.length()
                        + bb.second.length() ) {
                    value = new Pair<>( a.charAt( 0 ) + aa.first, aa.second );
                }
                else {
                    value = new Pair<>( bb.first, b.charAt( 0 ) + bb.second );
                }
            }
            lookup.put( key, value );
        }
        return lookup.get( key );
    }

    private static class Pair <T> {
        public Pair ( final T first, final T second ) {
            this.first = first;
            this.second = second;
        }

        public final T first, second;

        @Override
        public String toString () {
            return "(" + first + "," + second + ")";
        }
    }

}
