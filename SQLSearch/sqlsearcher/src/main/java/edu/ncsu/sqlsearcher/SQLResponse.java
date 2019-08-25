package edu.ncsu.sqlsearcher;

import java.util.List;

/**
 * Class used to send a response to the frontend after executing a query from a
 * participant.
 *
 * @author Kai
 *
 */
@SuppressWarnings ( "rawtypes" )
public class SQLResponse {

    /**
     * The data in the source table that the participant is selecting from
     */
    List   source;

    /**
     * The data in the destination table that the user is trying to get
     */
    List   destination;

    /**
     * The actual data returned by the query the user submitted
     */
    List   actual;

    /**
     * Name of the source table
     */
    String sourceTableName;

    /**
     * Name of the destination table
     */
    String destinationTableName;

    /**
     * Any error message to send to the user
     */
    String errorMessage;

    public SQLResponse ( final List source, final List destination, final List actual, final String sourceTableName,
            final String destinationTableName, final String errorMessage ) {
        this.source = source;
        this.destination = destination;
        this.actual = actual;
        this.sourceTableName = sourceTableName;
        this.destinationTableName = destinationTableName;

        this.errorMessage = errorMessage;
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

}
