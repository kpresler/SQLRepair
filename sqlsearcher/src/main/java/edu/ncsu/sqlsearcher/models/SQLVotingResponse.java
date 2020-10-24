package edu.ncsu.sqlsearcher.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table ( name = "SQLVotingResponses" )
public class SQLVotingResponse extends DomainObject<SQLVotingResponse> {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long    id;

    /**
     * Which problem this rating is for
     */
    @NotNull
    private String  problem;

    /**
     * Who submitted the vote
     */
    @NotNull
    private String  participant;

    /**
     * Optional explanation for which query they prefer
     */
    private String  explanation;

    /**
     * Repair of someone elese's query. Guaranteed to exist b/c we have DB
     * populated
     */
    @NotNull
    private String  repairedQuery;

    /**
     * Score given to repair of someone else's query. Guaranteed to exist
     * because we'll make them vote on a query we know we have.
     */
    @Min ( 1 )
    @Max ( 7 )
    @NotNull
    private Integer repairedScore;

    /**
     * Someone else's correct query. DB contains these, so we're sure we'll get
     * something here.
     */
    @NotNull
    private String  otherQuery;

    /**
     * Score given to someone else's correct query. We know we have these
     * queries so are guaranteed a vote
     */
    @Min ( 1 )
    @Max ( 7 )
    @NotNull
    private Integer otherScore;

    /**
     * Repair of one of my queries, if possible. Might be null if we never get
     * any wrong ones from the user.
     */
    private String  myRepairedQuery;

    /**
     * Score given to my repair. Might be null, if we don't have one.
     */
    @Min ( 1 )
    @Max ( 7 )
    private Integer myRepairedScore;

    /**
     * My correct query. Possibly null, if the problem never was solved
     * correctly.
     */
    private String  myCorrectQuery;

    /**
     * Vote on my own query. Could be null if the problem was never properly
     * solved.
     */
    @Min ( 1 )
    @Max ( 7 )
    private Integer myCorrectScore;

    public SQLVotingResponse () {

    }

    @Override
    public Serializable getId () {
        return id;
    }

    public String getRepairedQuery () {
        return repairedQuery;
    }

    public void setRepairedQuery ( final String repairedQuery ) {
        this.repairedQuery = repairedQuery;
    }

    public String getOtherQuery () {
        return otherQuery;
    }

    public void setOtherQuery ( final String otherQuery ) {
        this.otherQuery = otherQuery;
    }

    public String getParticipant () {
        return participant;
    }

    public void setParticipant ( final String participant ) {
        this.participant = participant;
    }

    public Integer getRepairedScore () {
        return repairedScore;
    }

    public void setRepairedScore ( final Integer repairedScore ) {
        this.repairedScore = repairedScore;
    }

    public Integer getOtherScore () {
        return otherScore;
    }

    public void setOtherScore ( final Integer otherScore ) {
        this.otherScore = otherScore;
    }

    public String getProblem () {
        return problem;
    }

    public void setProblem ( final String problem ) {
        this.problem = problem;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public String getExplanation () {
        return explanation;
    }

    public void setExplanation ( final String explanation ) {
        this.explanation = explanation;
    }

    public String getMyRepairedQuery () {
        return myRepairedQuery;
    }

    public void setMyRepairedQuery ( final String myRepairedQuery ) {
        this.myRepairedQuery = myRepairedQuery;
    }

    public Integer getMyRepairedScore () {
        return myRepairedScore;
    }

    public void setMyRepairedScore ( final Integer myRepairedScore ) {
        this.myRepairedScore = myRepairedScore;
    }

    public String getMyCorrectQuery () {
        return myCorrectQuery;
    }

    public void setMyCorrectQuery ( final String myCorrectQuery ) {
        this.myCorrectQuery = myCorrectQuery;
    }

    public Integer getMyCorrectScore () {
        return myCorrectScore;
    }

    public void setMyCorrectScore ( final Integer myCorrectScore ) {
        this.myCorrectScore = myCorrectScore;
    }

    /**
     * Handle expanding votes back out
     */
    public void updateVotes () {

        if ( null == myCorrectScore && null != myCorrectQuery ) {
            if ( myCorrectQuery.equals( myRepairedQuery ) && null != myRepairedScore ) {
                myCorrectScore = myRepairedScore;
            }
            else if ( myCorrectQuery.equals( otherQuery ) && null != otherScore ) {
                myCorrectScore = otherScore;
            }
            else if ( myCorrectQuery.equals( repairedQuery ) && null != repairedScore ) {
                myCorrectScore = repairedScore;
            }
        }

        if ( null == myRepairedScore && null != myRepairedQuery ) {
            if ( myRepairedQuery.equals( myCorrectQuery ) && null != myCorrectScore ) {
                myRepairedScore = myCorrectScore;
            }
            else if ( myRepairedQuery.equals( otherQuery ) && null != otherScore ) {
                myRepairedScore = otherScore;
            }
            else if ( myRepairedQuery.equals( repairedQuery ) && null != repairedScore ) {
                myRepairedScore = repairedScore;
            }
        }

        if ( null == otherScore && null != otherQuery ) {
            if ( otherQuery.equals( myCorrectQuery ) && null != myCorrectScore ) {
                otherScore = myCorrectScore;
            }
            else if ( otherQuery.equals( repairedQuery ) && null != repairedScore ) {
                otherScore = repairedScore;
            }
            else if ( otherQuery.equals( myRepairedQuery ) && null != myRepairedScore ) {
                otherScore = myRepairedScore;
            }
        }

        if ( null == repairedScore && null != repairedQuery ) {
            if ( repairedQuery.equals( myCorrectQuery ) && null != myCorrectScore ) {
                repairedScore = myCorrectScore;
            }
            if ( repairedQuery.equals( myRepairedQuery ) && null != myRepairedScore ) {
                repairedScore = myRepairedScore;
            }
            if ( repairedQuery.equals( otherQuery ) && null != otherScore ) {
                repairedScore = otherScore;
            }
        }

    }

}
