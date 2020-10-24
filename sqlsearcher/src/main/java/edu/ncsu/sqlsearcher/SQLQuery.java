package edu.ncsu.sqlsearcher;

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
