package edu.ncsu.sqlsearcher;

import edu.ncsu.sqlsearcher.forms.SQLVotingOptions;

public class SQLSuccessResponse {

    String           message;

    SQLVotingOptions options;

    Boolean          voting;

    public SQLSuccessResponse ( final String message, final SQLVotingOptions options ) {
        super();
        this.message = message;
        this.options = options;
        this.voting = null != options.getCorrectQuery() && null != options.getRepairedQuery();
    }

    public String getMessage () {
        return message;
    }

    public SQLVotingOptions getVotingOptions () {
        return options;
    }

    public Boolean getVoting () {
        return voting;
    }

}
