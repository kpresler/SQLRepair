package edu.ncsu.sqlrepair.datatypes;

import java.io.Serializable;

/**
 * Boolean datatype for Z3. Can be either `true` or `false`
 * 
 * @author Kai Presler-Marshall
 *
 */
public class Z3Bool extends Z3Type {

    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
        return result;
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Z3Bool other = (Z3Bool) obj;
        if ( value == null ) {
            if ( other.value != null ) {
                return false;
            }
        }
        else if ( !value.equals( other.value ) ) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo ( final Z3Type other ) {
        return value.compareTo( (Boolean) other.getValue() );
    }

    final private Boolean value;

    public Z3Bool ( final Boolean value ) {
        this.value = value;
    }

    @Override
    public Serializable getValue () {
        return value;
    }

    @Override
    public Class< ? > getDataClass () {
        return Boolean.class;
    }

    @Override
    public String toString () {
        return value.toString();
    }

}
