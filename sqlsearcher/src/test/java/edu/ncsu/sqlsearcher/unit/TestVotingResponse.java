package edu.ncsu.sqlsearcher.unit;

import org.junit.Assert;
import org.junit.Test;

import edu.ncsu.sqlsearcher.models.SQLVotingResponse;

public class TestVotingResponse {

    String QUERY_A = "SELECT * FROM alpha WHERE MIN = 0";
    String QUERY_B = "SELECT A,B,C,D FROM alpha WHERE MIN = 0";

    @Test
    public void testVoteExpansion () {

        final SQLVotingResponse resp = new SQLVotingResponse();

        resp.setRepairedQuery( QUERY_A );
        resp.setMyRepairedQuery( QUERY_A );

        resp.setRepairedScore( 7 );

        resp.setOtherQuery( QUERY_A );

        resp.updateVotes();

        Assert.assertEquals( 7, (int) resp.getOtherScore() );

        Assert.assertEquals( 7, (int) resp.getMyRepairedScore() );

        Assert.assertNull( resp.getMyCorrectScore() );

        Assert.assertEquals( 7, (int) resp.getRepairedScore() );

    }

    @Test
    public void testVoteExpansionAll () {
        final SQLVotingResponse resp = new SQLVotingResponse();

        resp.setRepairedQuery( QUERY_A );
        resp.setMyRepairedQuery( QUERY_A );

        resp.setRepairedScore( 7 );

        resp.setOtherQuery( QUERY_A );

        resp.setMyCorrectQuery( QUERY_A );

        resp.updateVotes();

        Assert.assertEquals( 7, (int) resp.getOtherScore() );

        Assert.assertEquals( 7, (int) resp.getMyRepairedScore() );

        Assert.assertEquals( 7, (int) resp.getMyCorrectScore() );

        Assert.assertEquals( 7, (int) resp.getRepairedScore() );

    }

    @Test
    public void testVoteExpansionOne () {
        SQLVotingResponse resp = new SQLVotingResponse();

        resp.setRepairedQuery( QUERY_A );
        resp.setMyRepairedQuery( QUERY_A );
        resp.setMyRepairedScore( 7 );

        resp.updateVotes();

        Assert.assertNull( resp.getOtherScore() );

        Assert.assertEquals( 7, (int) resp.getMyRepairedScore() );

        Assert.assertNull( resp.getMyCorrectScore() );

        Assert.assertEquals( 7, (int) resp.getRepairedScore() );

        resp = new SQLVotingResponse();

        resp.setRepairedQuery( QUERY_A );
        resp.setMyRepairedQuery( QUERY_B );
        resp.setMyRepairedScore( 7 );

        resp.updateVotes();

        Assert.assertNull( resp.getOtherScore() );

        Assert.assertEquals( 7, (int) resp.getMyRepairedScore() );

        Assert.assertNull( resp.getMyCorrectScore() );

        Assert.assertNull( resp.getRepairedScore() );

        resp = new SQLVotingResponse();

        resp.setRepairedQuery( QUERY_A );
        resp.setMyCorrectQuery( QUERY_A );
        resp.setMyCorrectScore( 7 );

        resp.updateVotes();

        Assert.assertNull( resp.getOtherScore() );

        Assert.assertNull( resp.getMyRepairedScore() );

        Assert.assertEquals( 7, (int) resp.getMyCorrectScore() );

        Assert.assertEquals( 7, (int) resp.getRepairedScore() );

        resp = new SQLVotingResponse();

        resp.setMyRepairedQuery( QUERY_A );
        resp.setMyCorrectQuery( QUERY_A );
        resp.setMyCorrectScore( 7 );

        resp.updateVotes();

        Assert.assertNull( resp.getOtherScore() );

        Assert.assertNull( resp.getRepairedScore() );

        Assert.assertEquals( 7, (int) resp.getMyCorrectScore() );

        Assert.assertEquals( 7, (int) resp.getMyRepairedScore() );

        resp = new SQLVotingResponse();

        resp.setMyRepairedQuery( QUERY_A );
        resp.setOtherQuery( QUERY_A );
        resp.setOtherScore( 7 );

        resp.updateVotes();

        Assert.assertEquals( 7, (int) resp.getOtherScore() );

        Assert.assertNull( resp.getRepairedScore() );

        Assert.assertNull( resp.getMyCorrectScore() );

        Assert.assertEquals( 7, (int) resp.getMyRepairedScore() );

        resp = new SQLVotingResponse();

        resp.setMyRepairedQuery( QUERY_A );
        resp.setOtherQuery( QUERY_A );
        resp.setMyRepairedScore( 7 );

        resp.updateVotes();

        Assert.assertEquals( 7, (int) resp.getOtherScore() );

        Assert.assertNull( resp.getRepairedScore() );

        Assert.assertNull( resp.getMyCorrectScore() );

        Assert.assertEquals( 7, (int) resp.getMyRepairedScore() );

        resp = new SQLVotingResponse();

        resp.setRepairedQuery( QUERY_A );
        resp.setOtherQuery( QUERY_A );
        resp.setOtherScore( 7 );

        resp.updateVotes();

        Assert.assertEquals( 7, (int) resp.getOtherScore() );

        Assert.assertNull( resp.getMyRepairedScore() );

        Assert.assertNull( resp.getMyCorrectScore() );

        Assert.assertEquals( 7, (int) resp.getRepairedScore() );

        resp = new SQLVotingResponse();

        resp.setMyCorrectQuery( QUERY_A );
        resp.setMyRepairedQuery( QUERY_A );
        resp.setMyRepairedScore( 7 );

        resp.updateVotes();

        Assert.assertNull( resp.getOtherScore() );

        Assert.assertEquals( 7, (int) resp.getMyRepairedScore() );

        Assert.assertEquals( 7, (int) resp.getMyCorrectScore() );

        Assert.assertNull( resp.getRepairedScore() );

        resp = new SQLVotingResponse();

        resp.setMyCorrectQuery( QUERY_A );
        resp.setOtherQuery( QUERY_A );
        resp.setOtherScore( 7 );

        resp.updateVotes();

        Assert.assertEquals( 7, (int) resp.getOtherScore() );

        Assert.assertNull( resp.getMyRepairedScore() );

        Assert.assertEquals( 7, (int) resp.getMyCorrectScore() );

        Assert.assertNull( resp.getRepairedScore() );

    }

    @Test
    public void testExpandAll () {
        SQLVotingResponse resp = new SQLVotingResponse();

        resp.setRepairedQuery( QUERY_A );
        resp.setMyRepairedQuery( QUERY_A );
        resp.setMyCorrectQuery( QUERY_A );
        resp.setOtherQuery( QUERY_A );

        resp.setMyRepairedScore( 7 );

        resp.updateVotes();

        Assert.assertEquals( 7, (int) resp.getMyRepairedScore() );
        Assert.assertEquals( 7, (int) resp.getRepairedScore() );
        Assert.assertEquals( 7, (int) resp.getOtherScore() );
        Assert.assertEquals( 7, (int) resp.getMyCorrectScore() );

        resp = new SQLVotingResponse();

        resp.setRepairedQuery( QUERY_A );
        resp.setMyRepairedQuery( QUERY_A );
        resp.setMyCorrectQuery( QUERY_A );
        resp.setOtherQuery( QUERY_B );

        resp.setMyRepairedScore( 7 );

        resp.updateVotes();

        Assert.assertEquals( 7, (int) resp.getMyRepairedScore() );
        Assert.assertEquals( 7, (int) resp.getRepairedScore() );
        Assert.assertNull( resp.getOtherScore() );
        Assert.assertEquals( 7, (int) resp.getMyCorrectScore() );

    }

}
