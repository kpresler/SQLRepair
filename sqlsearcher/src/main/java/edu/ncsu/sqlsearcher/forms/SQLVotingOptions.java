package edu.ncsu.sqlsearcher.forms;

public class SQLVotingOptions {

    private final String  repairedQuery;

    private final String  correctQuery;

    private final String  problem;

    private final String  myCorrect;

    private final String  myRepaired;

    private final Boolean displayA;

    private final Boolean displayB;

    private Boolean       displayC;

    private Boolean       displayD;

    public SQLVotingOptions ( final String repairedQuery, final String correctQuery, final String problem,
            final String myCorrect, final String myRepaired ) {
        this.problem = problem;

        this.correctQuery = correctQuery;
        displayA = correctQuery != null;

        this.repairedQuery = repairedQuery;
        // If the DB-lookup repaired is not the same as the correct one, let the
        // user vote on it
        displayB = repairedQuery != null && !repairedQuery.equals( correctQuery );

        this.myCorrect = myCorrect;
        // If my correct query exists and is not one of the previous, let the
        // user vote
        if ( null != myCorrect && !myCorrect.equals( correctQuery ) && !myCorrect.equals( repairedQuery ) ) {
            displayC = true;
        }
        else {
            displayC = false;
        }

        this.myRepaired = myRepaired;
        // If repair exists and is distinct from all previous ones
        if ( null != myRepaired && !myRepaired.equals( correctQuery ) && !myRepaired.equals( repairedQuery )
                && !myRepaired.equals( myCorrect ) ) {
            displayD = true;
        }
        else {
            displayD = false;
        }

    }

    public String getRepairedQuery () {
        return repairedQuery;
    }

    public String getCorrectQuery () {
        return correctQuery;
    }

    public String getProblem () {
        return problem;
    }

    public String getMyCorrect () {
        return myCorrect;
    }

    public String getMyRepaired () {
        return myRepaired;
    }

    public Boolean getDisplayA () {
        return displayA;
    }

    public Boolean getDisplayB () {
        return displayB;
    }

    public Boolean getDisplayC () {
        return displayC;
    }

    public Boolean getDisplayD () {
        return displayD;
    }

}
