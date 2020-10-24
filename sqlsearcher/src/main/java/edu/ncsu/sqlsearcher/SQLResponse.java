package edu.ncsu.sqlsearcher;

import java.util.List;

@SuppressWarnings ( "rawtypes" )
public class SQLResponse {

    private final List    source;

    private final List    destination;

    private final List    actual;

    private final String  sourceTableName;

    private final String  destinationTableName;

    private final String  errorMessage;

    private final boolean canRequestToVote;

    public SQLResponse ( final List source, final List destination, final List actual, final String sourceTableName,
            final String destinationTableName, final String errorMessage, final boolean canRequestToVote ) {
        this.source = source;
        this.destination = destination;
        this.actual = actual;
        this.sourceTableName = sourceTableName;
        this.destinationTableName = destinationTableName;
        this.errorMessage = errorMessage;
        this.canRequestToVote = canRequestToVote;
    }

    public SQLResponse ( final String errorMessage, final boolean canRequestToVote ) {
        this( null, null, null, null, null, errorMessage, canRequestToVote );
    }

    public List getSource () {
        return source;
    }

    public List getDestination () {
        return destination;
    }

    public List getActual () {
        return actual;
    }

    public String getSourceTableName () {
        return sourceTableName;
    }

    public String getDestinationTableName () {
        return destinationTableName;
    }

    public String getErrorMessage () {
        return errorMessage;
    }

    public boolean getCanRequestToVote () {
        return canRequestToVote;
    }

}
