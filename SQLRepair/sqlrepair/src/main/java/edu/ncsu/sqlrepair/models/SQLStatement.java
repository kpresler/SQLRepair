package edu.ncsu.sqlrepair.models;

public class SQLStatement {

    private String statement;

    public SQLStatement () {

    }

    @Override
    public String toString () {
        return statement;
    }

    /**
     * Construct a SQLStatement object from the provided SQL string.
     *
     * @param statement
     */
    public SQLStatement ( final String statement ) {
        this();
        setStatement( statement );
    }

    public String getStatement () {
        return statement;
    }

    public void setStatement ( final String statement ) {
        this.statement = statement;
    }

}
