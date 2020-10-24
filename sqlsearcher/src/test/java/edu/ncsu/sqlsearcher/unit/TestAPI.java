package edu.ncsu.sqlsearcher.unit;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.ncsu.sqlsearcher.HibernateDataGenerator;
import edu.ncsu.sqlsearcher.SQLQuery;
import edu.ncsu.sqlsearcher.SQLSuccessResponse;
import edu.ncsu.sqlsearcher.controllers.APIRepairController;
import edu.ncsu.sqlsearcher.controllers.APIUserSQLController;
import edu.ncsu.sqlsearcher.forms.SQLVotingOptions;
import edu.ncsu.sqlsearcher.models.SQLStatement;
import edu.ncsu.sqlsearcher.models.SQLVotingResponse;

@SuppressWarnings ( "unchecked" )
public class TestAPI {

    static private final String               ME         = "kpresler";
    static private final String               ERIC       = "ewhorton";

    static final private APIUserSQLController submitCtrl = new APIUserSQLController();
    static private final APIRepairController  repCtrl    = new APIRepairController();

    @Before
    public void setup () {
        SQLStatement.deleteAll();

        HibernateDataGenerator.addRepairedQueries();
    }

    @Test
    public void testValidQueryAccepted () {
        final SQLQuery query = new SQLQuery();
        query.setParticipant( ME );
        query.setProblem( "Problem 1" );
        query.setQuery( "SELECT * FROM alpha WHERE min = 0 ORDER BY 1,2,3,4" );

        final ResponseEntity<SQLSuccessResponse> ctrlResponse = submitCtrl.submitQuery( query );

        Assert.assertEquals( "A valid query should be accepted by the API", HttpStatus.OK,
                ctrlResponse.getStatusCode() );

        final SQLSuccessResponse response = ctrlResponse.getBody();

        final SQLVotingOptions voting = response.getVotingOptions();

        Assert.assertNotNull( "With no other queries submitted, we should get a repaired query already in the system",
                voting.getRepairedQuery() );
        Assert.assertNull( "With no other queries submitted, there should be no possibility for another correct query",
                voting.getCorrectQuery() );

    }

    @Test
    public void testValidQueryAcceptedWithNoCorrectAlternative () {
        {
            final SQLQuery query = new SQLQuery();
            query.setParticipant( ERIC );
            query.setProblem( "Problem 1" );
            query.setQuery( "SELECT * FROM alpha WHERE min = 1 ORDER BY 1,2,3,4" );

            final ResponseEntity ctrlResponse = submitCtrl.submitQuery( query );

            Assert.assertEquals( "An invalid query should give an error", HttpStatus.NOT_FOUND,
                    ctrlResponse.getStatusCode() );
        }
        {
            final SQLQuery query = new SQLQuery();
            query.setParticipant( ME );
            query.setProblem( "Problem 1" );
            query.setQuery( "SELECT * FROM alpha WHERE min = 0 ORDER BY 1,2,3,4" );

            final ResponseEntity<SQLSuccessResponse> ctrlResponse = submitCtrl.submitQuery( query );

            Assert.assertEquals( "A valid query should be accepted by the API", HttpStatus.OK,
                    ctrlResponse.getStatusCode() );

            final SQLSuccessResponse response = ctrlResponse.getBody();

            final SQLVotingOptions voting = response.getVotingOptions();

            Assert.assertNull( "With only an incorrect query from someone else, we shouldn't get another correct query",
                    voting.getCorrectQuery() );

        }

        {
            final SQLQuery query = new SQLQuery();
            query.setParticipant( ERIC );
            query.setProblem( "Problem 1" );
            query.setQuery( "SELECT COL, DES, MIN, AV, MAX, FIL FROM alpha WHERE min = 0 ORDER BY 1,2,3,4" );

            final ResponseEntity ctrlResponse = submitCtrl.submitQuery( query );

            Assert.assertEquals( "A valid query from someone else should be accepted", HttpStatus.OK,
                    ctrlResponse.getStatusCode() );

        }

        {
            final SQLQuery query = new SQLQuery();
            query.setParticipant( ME );
            query.setProblem( "Problem 1" );
            query.setQuery( "SELECT * FROM alpha WHERE min = 0 ORDER BY 1,2,3,4" );

            final ResponseEntity<SQLSuccessResponse> ctrlResponse = submitCtrl.submitQuery( query );

            Assert.assertEquals( "A valid query should be accepted by the API", HttpStatus.OK,
                    ctrlResponse.getStatusCode() );

            final SQLSuccessResponse response = ctrlResponse.getBody();

            final SQLVotingOptions voting = response.getVotingOptions();

            Assert.assertNull( "Queries submitted as part of the activity should not be considered eligible for voting",
                    voting.getCorrectQuery() );

            Assert.assertNotNull( "We should be able to vote on our own correct query", voting.getMyCorrect() );

            Assert.assertNotNull( "We should have a repaired version of out query too", voting.getMyRepaired() );

            Assert.assertEquals(
                    "With an incorrect query submitted by someone else, we should be able to view the repair of it",
                    "SELECT * FROM alpha WHERE MIN = 0", voting.getMyRepaired() );

            Assert.assertEquals(
                    "With a correct query submitted by someone other than us, submitting a correct query should let us see the other correct query",
                    "SELECT * FROM alpha WHERE min = 0 ORDER BY 1,2,3,4", voting.getMyCorrect() );

        }

    }

    @Test
    public void testNotAuthorisedToRetrieve () {
        final ResponseEntity response = repCtrl.getQueriesForVoting( ME, "Problem 1" );
        Assert.assertEquals(
                "With no attempt to submit any problems before, we shouldn't be able to request a correct query yet",
                HttpStatus.UNAUTHORIZED, response.getStatusCode() );

        Assert.assertNull( "With no submitted queries, we shouldn't be able to get anything to vote on",
                response.getBody() );

    }

    @Test
    public void testQueriesFound () {
        for ( int i = 0; i < 5; i++ ) {
            final SQLStatement stmt = new SQLStatement();
            stmt.setCorrect( false );
            stmt.setParticipant( ME );
            stmt.setProblem( "Problem 1" );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 0" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }

        ResponseEntity response = repCtrl.getQueriesForVoting( ME, "Problem 1" );

        Assert.assertEquals(
                "With enough queries to be authorised, we should get a repair and/or another correct query from the database",
                HttpStatus.OK, response.getStatusCode() );

        /*
         * Create a correct query from someone else, so now we can expect to see
         * a response
         */
        final SQLStatement stmt = new SQLStatement();
        stmt.setCorrect( true );
        stmt.setParticipant( ERIC );
        stmt.setProblem( "Problem 1" );
        stmt.setStatement( "SELECT * FROM alpha WHERE min = 0" );
        stmt.setSubmitTime( Calendar.getInstance() );
        stmt.save();

        response = repCtrl.getQueriesForVoting( ME, "Problem 1" );

        Assert.assertEquals(
                "With enough queries to be authorised and a correct query from someone else we get a query to vote on",
                HttpStatus.OK, response.getStatusCode() );

    }

    @Test
    public void testVoteAccepted () {
        final SQLVotingResponse vote = new SQLVotingResponse();
        vote.setRepairedQuery( "SELECT * FROM alpha WHERE min = 0 OR min = 1" );
        vote.setOtherQuery( "SELECT a,b,c FROM alpha WHERE min = 0" );
        vote.setParticipant( ME );
        vote.setRepairedScore( 7 );
        vote.setOtherScore( 5 );
        vote.setProblem( "Problem 1" );

        final ResponseEntity response = repCtrl.submitVotes( vote );

        Assert.assertEquals( "Submitting a valid vote should work successfully", HttpStatus.OK,
                response.getStatusCode() );
    }

    @Test
    public void testBadVotes () {
        SQLVotingResponse vote = new SQLVotingResponse();
        vote.setOtherQuery( "SELECT a,b,c FROM alpha WHERE min = 0" );
        vote.setParticipant( ME );
        vote.setRepairedScore( 7 );
        vote.setOtherScore( 5 );
        vote.setProblem( "Problem 1" );
        ResponseEntity response = repCtrl.submitVotes( vote );
        Assert.assertEquals( "Submitting a vote with missing piece \"Repaired Query\" should be rejected",
                HttpStatus.BAD_REQUEST, response.getStatusCode() );

        vote = new SQLVotingResponse();
        vote.setRepairedQuery( "SELECT * FROM alpha WHERE min = 0 OR min = 1" );
        vote.setParticipant( ME );
        vote.setRepairedScore( 7 );
        vote.setOtherScore( 5 );
        vote.setProblem( "Problem 1" );
        response = repCtrl.submitVotes( vote );
        Assert.assertEquals( "Submitting a vote with missing piece \"Other Query\" should be rejected",
                HttpStatus.BAD_REQUEST, response.getStatusCode() );

        vote = new SQLVotingResponse();
        vote.setRepairedQuery( "SELECT * FROM alpha WHERE min = 0 OR min = 1" );
        vote.setOtherQuery( "SELECT a,b,c FROM alpha WHERE min = 0" );
        vote.setRepairedScore( 7 );
        vote.setOtherScore( 5 );
        vote.setProblem( "Problem 1" );
        response = repCtrl.submitVotes( vote );
        Assert.assertEquals( "Submitting a vote with missing piece \"Participant\" should be rejected",
                HttpStatus.BAD_REQUEST, response.getStatusCode() );

        vote = new SQLVotingResponse();
        vote.setRepairedQuery( "SELECT * FROM alpha WHERE min = 0 OR min = 1" );
        vote.setOtherQuery( "SELECT a,b,c FROM alpha WHERE min = 0" );
        vote.setParticipant( ME );
        vote.setOtherScore( 5 );
        vote.setProblem( "Problem 1" );
        response = repCtrl.submitVotes( vote );
        Assert.assertEquals( "Submitting a vote with missing piece \"Repaired Score\" should be rejected",
                HttpStatus.BAD_REQUEST, response.getStatusCode() );

        vote = new SQLVotingResponse();
        vote.setRepairedQuery( "SELECT * FROM alpha WHERE min = 0 OR min = 1" );
        vote.setOtherQuery( "SELECT a,b,c FROM alpha WHERE min = 0" );
        vote.setParticipant( ME );
        vote.setRepairedScore( 7 );
        vote.setProblem( "Problem 1" );
        response = repCtrl.submitVotes( vote );
        Assert.assertEquals( "Submitting a vote with missing piece \"Other Score\" should be rejected",
                HttpStatus.BAD_REQUEST, response.getStatusCode() );

        vote = new SQLVotingResponse();
        vote.setRepairedQuery( "SELECT * FROM alpha WHERE min = 0 OR min = 1" );
        vote.setOtherQuery( "SELECT a,b,c FROM alpha WHERE min = 0" );
        vote.setParticipant( ME );
        vote.setRepairedScore( 7 );
        vote.setOtherScore( 5 );
        response = repCtrl.submitVotes( vote );
        Assert.assertEquals( "Submitting a vote with missing piece \"Problem\" should be rejected",
                HttpStatus.BAD_REQUEST, response.getStatusCode() );

        /* TODO: Also test invalid integer values */

    }

}
