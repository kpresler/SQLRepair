package edu.ncsu.sqlrepair.datatypes;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Common supperclass for all types that we support operating on. This is
 * necessary so that we can have collections of them and actually figure out
 * what they are, since there's no common superclass for boolean and numeric
 * types, and the JRE won't let us extend them.
 *
 * @author kai
 *
 */
public abstract class Z3Type implements Comparable<Z3Type> {

    public abstract Serializable getValue ();

    /**
     * JVM is stupid and won't let us override `Object.getClass()` to actually
     * return the class that we're wrapping, so we have to use this hacky
     * approach instead.
     *
     * @return
     */
    abstract public Class< ? > getDataClass ();

    /**
     * Attempt to parse the String into one of the data types that we support
     * operating on
     *
     * @param s
     *            String to parse
     * @return A matching Z3Type object if possible, `null` if it couldn't be
     *         understood as anything we can use
     */
    public static Z3Type parse ( final String s ) {
        Z3Type val = null;
        if ( null == val ) {
            val = asInt( s );
        }
        if ( null == val ) {
            val = asBool( s );
        }
        if ( null == val ) {
            val = asDate( s );
        }
        if ( null == val ) {
            val = asString( s );
        }
        return val;

    }

    static public Z3Type parse ( final String s, final Class< ? > parseAs ) {
        if ( parseAs.equals( Z3Int.class ) ) {
            return asInt( s );
        }
        if ( parseAs.equals( Z3Bool.class ) ) {
            return asBool( s );
        }
        if ( parseAs.equals( Z3Date.class ) ) {
            return asDate( s );
        }
        return asString( s );
    }

    /**
     * Figure out the most constrained type that we can use for storing all
     * values in an incoming list. Int gets preference, then boolean, date, and
     * finally string as a fallback.
     *
     * @param values
     * @return
     */
    @SuppressWarnings ( "unchecked" )
    static public Class<Z3Type> findType ( final List<String> values ) {
        final List<Z3Type> parsedValues = values.stream().map( e -> parse( e ) )
                .collect( Collectors.toList() );

        // Promote upwards: (Int | Bool | Date) -> String. We'd like to be able
        // to use the more specific classes (because there's more you can do
        // with a number than a string) but we have to deal with that we don't
        // have provided types for each column.
        Class< ? > mostGeneralType = Z3Int.class;
        for ( final Z3Type value : parsedValues ) {
            if ( mostGeneralType.equals( Z3Int.class )
                    && value.getDataClass().equals( Boolean.class ) ) {
                mostGeneralType = Z3Bool.class;
            }

            if ( ( mostGeneralType.equals( Z3Int.class )
                    || mostGeneralType.equals( Z3Bool.class ) )
                    && value.getDataClass().equals( Long.class ) ) {
                mostGeneralType = Z3Date.class;
            }

            if ( value.getDataClass().equals( String.class ) ) {
                mostGeneralType = Z3String.class;
            }

        }

        return (Class<Z3Type>) mostGeneralType;

    }

    /**
     * Try to parse a String into an Integer, then wrap it for our use
     *
     * @param s
     *            String to parse
     * @return Z3Int representing the string if possible, else `null`
     */
    static protected Z3Int asInt ( final String s ) {
        try {
            return new Z3Int( Integer.parseInt( s ) );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Try to parse a String into a Boolean, then wrap it for our use. Java will
     * happily parse anything other than `true` into a boolean representing
     * `false`, which is obviously not what we want, so we have to check that
     * first.
     *
     * @param s
     *            String to parse
     * @return Z3Bool representing the string if possible, else `null`
     */
    static protected Z3Bool asBool ( final String s ) {
        try {
            return ( s.equalsIgnoreCase( "true" )
                    || s.equalsIgnoreCase( "false" ) )
                            ? new Z3Bool( Boolean.parseBoolean( s ) )
                            : null;

        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Turn a String into our Z3String. Not much that needs to be done here.
     * 
     * @param s
     *            String to parse
     * @return Z3String, always will get this, can never fail
     */
    static protected Z3String asString ( final String s ) {
        return new Z3String( s );
    }

    /**
     * Attempt to parse the String provided as a date for Z3 to use. Returns
     * null if it isn't valid to interpret as a date.
     * 
     * @param s
     * @return
     */
    static protected Z3Date asDate ( final String s ) {
        try {
            return new Z3Date( s );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

}
