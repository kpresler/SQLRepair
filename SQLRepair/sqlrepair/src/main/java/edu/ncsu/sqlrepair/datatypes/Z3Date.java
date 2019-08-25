package edu.ncsu.sqlrepair.datatypes;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;

public class Z3Date extends Z3Type {

    private final Long   value;

    private final String dateString;

    public Z3Date ( final String date ) throws ParseException {
        this.value = LocalDate.parse( date ).atStartOfDay( ZoneId.systemDefault() ).toEpochSecond();
        this.dateString = date;
    }

    @Override
    public Serializable getValue () {
        return value;
    }

    public String getDateString () {
        return dateString;
    }

    @Override
    public Class< ? > getDataClass () {
        return java.lang.Long.class;
    }

    @Override
    public String toString () {
        return dateString;
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
        final Z3Date other = (Z3Date) obj;
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
        return value.compareTo( (Long) o.getValue() );
    }

}
