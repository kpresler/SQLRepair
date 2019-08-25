package edu.ncsu.sqlsearcher;

/**
 * Represents a SQL query submitted by one of the users of this application.
 * Tracks the problem this was intended to solve, the query submitted, and who
 * submitted it. This class is automatically deserialised by Spring from the
 * JSON sent by the frontend.
 *
 * @author Kai Presler-Marshall
 *
 */
public class SQLQuery {

    private String problem;

    private String query;

    private String participant;

    public SQLQuery () {

    }

    public String getQuery () {
        return query;
    }

    public void setQuery ( final String query ) {
        this.query = query;
    }

    public String getParticipant () {
        return participant;
    }

    public void setParticipant ( final String participant ) {
        this.participant = participant;
    }

    public String getProblem () {
        return problem;
    }

    public void setProblem ( final String problem ) {
        this.problem = problem;
    }

}
