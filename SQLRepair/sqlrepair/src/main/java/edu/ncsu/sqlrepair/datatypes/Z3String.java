package edu.ncsu.sqlrepair.datatypes;

public class Z3String extends Z3Type {

    private final String value;

    public Z3String ( final String value ) {
        this.value = value;
    }

    @Override
    public String getValue () {
        return this.value;
    }

    @Override
    public Class<String> getDataClass () {
        return String.class;
    }

    @Override
    public String toString () {
        return value;
    }

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
        final Z3String other = (Z3String) obj;
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
    public int compareTo ( final Z3Type o ) {
        return value.compareTo( (String) o.getValue() );
    }

}
