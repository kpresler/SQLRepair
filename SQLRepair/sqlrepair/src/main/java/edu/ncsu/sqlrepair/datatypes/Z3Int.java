package edu.ncsu.sqlrepair.datatypes;

public class Z3Int extends Z3Type {

    private final Integer value;

    public Z3Int ( final Integer value ) {
        this.value = value;
    }

    @Override
    public Integer getValue () {
        return this.value;
    }

    @Override
    public Class<Integer> getDataClass () {
        return Integer.class;
    }

    @Override
    public String toString () {
        return value.toString();
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
        final Z3Int other = (Z3Int) obj;
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
        return value.compareTo( (Integer) o.getValue() );
    }

}
