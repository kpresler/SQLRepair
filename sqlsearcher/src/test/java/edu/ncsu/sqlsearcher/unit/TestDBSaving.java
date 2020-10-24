package edu.ncsu.sqlsearcher.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.sqlsearcher.models.SQLStatement;

public class TestDBSaving {

    static private final String ME   = "kpresler";
    static private final String ERIC = "ewhorton";

    @Before
    public void setup () {
        SQLStatement.deleteAll();
    }

    @Test
    public void testSavingQueries () {

        assertEquals( 0, SQLStatement.getAll().size() );

        final SQLStatement stmt = new SQLStatement();

        stmt.setProblem( "Problem 1" );
        stmt.setCorrect( true );
        stmt.setParticipant( ME );
        stmt.setStatement( "SELECT * FROM alpha WHERE min = 0" );
        stmt.setSubmitTime( Calendar.getInstance() );

        stmt.save();

        assertEquals( 1, SQLStatement.getAll().size() );

    }

    @Test
    public void testGetMyWrongQueries () {
        {

            final SQLStatement stmt = new SQLStatement();

            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( false );
            stmt.setParticipant( ME );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 0" );
            stmt.setSubmitTime( Calendar.getInstance() );

            stmt.save();

        }
        {
            final SQLStatement stmt = new SQLStatement();

            stmt.setProblem( "Problem 2" );
            stmt.setCorrect( false );
            stmt.setParticipant( ME );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 1" );
            stmt.setSubmitTime( Calendar.getInstance() );

            stmt.save();

        }
        {

            final SQLStatement stmt = new SQLStatement();

            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( false );
            stmt.setParticipant( ME );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 2" );
            stmt.setSubmitTime( Calendar.getInstance() );

            stmt.save();

        }
        {

            final SQLStatement stmt = new SQLStatement();

            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( true );
            stmt.setParticipant( ME );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 3" );
            stmt.setSubmitTime( Calendar.getInstance() );

            stmt.save();

        }

        /*
         * At this point we've created two wrong statements for Problem 1, one
         * right statement for Problem 1, and one wrong statement for Problem 2
         */

        List<SQLStatement> wrong = SQLStatement.getMyWrongQueries( ME, "Problem 1" );

        assertEquals( 2, wrong.size() );

        {

            final SQLStatement stmt = new SQLStatement();

            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( false );
            stmt.setParticipant( ME );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 4" );
            stmt.setSubmitTime( Calendar.getInstance() );

            stmt.save();

        }
        {

            final SQLStatement stmt = new SQLStatement();

            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( true );
            stmt.setParticipant( ME );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 5" );
            stmt.setSubmitTime( Calendar.getInstance() );

            stmt.save();

        }
        /*
         * Adding a new wrong problem should increase the count
         */

        wrong = SQLStatement.getMyWrongQueries( ME, "Problem 1" );

        assertEquals( 3, wrong.size() );

        /*
         * But if we add it by someone else, it shouldn't
         */

        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( false );
            stmt.setParticipant( ERIC );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 6" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }
        wrong = SQLStatement.getMyWrongQueries( ME, "Problem 1" );

        assertEquals( 3, wrong.size() );

        /* Also make sure that we have one of the ones we'd expect */
        try {
            wrong.stream().filter( e -> e.getStatement().equals( "SELECT * FROM alpha WHERE min = 4" ) ).findAny()
                    .get();
        }
        catch ( final NoSuchElementException nse ) {
            fail();
        }

    }

    @Test
    public void testCorrectOtherQueries () {
        SQLStatement matched = null;
        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( false );
            stmt.setParticipant( ERIC );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 1" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }
        matched = SQLStatement.getAnotherCorrectQuery( ME, "Problem 1" );

        /* Only statement so far is wrong */
        assertNull( matched );

        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( true );
            stmt.setParticipant( ME );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 2" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }
        matched = SQLStatement.getAnotherCorrectQuery( ME, "Problem 1" );

        /* We now have a correct query, but only from me */
        assertNull( matched );

        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( true );
            stmt.setParticipant( ERIC );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 3" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }
        matched = SQLStatement.getAnotherCorrectQuery( ME, "Problem 1" );

        /*
         * Now we have a correct one from someone else, so this should be fine
         */
        assertNotNull( matched );

        assertEquals( ERIC, matched.getParticipant() );
        assertEquals( "SELECT * FROM alpha WHERE min = 3", matched.getStatement() );

        {
            matched.setCount( 50 );
            matched.save();
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( true );
            stmt.setParticipant( ERIC );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 4" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.setCount( 5 );
            stmt.save();
        }

        /* Now make sure that we get the one with the lower count */
        matched = SQLStatement.getAnotherCorrectQuery( ME, "Problem 1" );
        assertEquals( "SELECT * FROM alpha WHERE min = 4", matched.getStatement() );

        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( true );
            stmt.setParticipant( ME );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 5" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.setCount( 1 );
            stmt.save();
        }
        /* But that a lower count from the current user doesn't take priority */
        matched = SQLStatement.getAnotherCorrectQuery( ME, "Problem 1" );
        assertEquals( "SELECT * FROM alpha WHERE min = 4", matched.getStatement() );
        assertEquals( ERIC, matched.getParticipant() );

        /* nor one from other problems */
        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 2" );
            stmt.setCorrect( true );
            stmt.setParticipant( ERIC );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 6" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.setCount( 1 );
            stmt.save();
        }
        /* But that a lower count from the current user doesn't take priority */
        matched = SQLStatement.getAnotherCorrectQuery( ME, "Problem 1" );
        assertEquals( "SELECT * FROM alpha WHERE min = 4", matched.getStatement() );
        assertEquals( String.valueOf( 5 ), String.valueOf( matched.getCount() ) );

    }

    @Test
    public void testOtherWrongQueries () {
        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( true );
            stmt.setParticipant( ERIC );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 1" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }

        /* Only query is correct */
        List<SQLStatement> wrong = SQLStatement.getOtherWrongQueries( ME, "Problem 1" );
        assertEquals( 0, wrong.size() );

        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( false );
            stmt.setParticipant( ME );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 2" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }

        /* Only wrong query is from me */
        wrong = SQLStatement.getOtherWrongQueries( ME, "Problem 1" );
        assertEquals( 0, wrong.size() );

        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 2" );
            stmt.setCorrect( false );
            stmt.setParticipant( ERIC );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 3" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }

        /* Only wrong query from someone else is also for another problem */
        wrong = SQLStatement.getOtherWrongQueries( ME, "Problem 1" );
        assertEquals( 0, wrong.size() );

        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( false );
            stmt.setParticipant( ERIC );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 4" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }
        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( false );
            stmt.setParticipant( ERIC );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 5" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }
        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setProblem( "Problem 1" );
            stmt.setCorrect( false );
            stmt.setParticipant( ERIC );
            stmt.setStatement( "SELECT * FROM alpha WHERE min = 6" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.save();
        }
        /* Now let's make sure we get some queries finally */
        wrong = SQLStatement.getOtherWrongQueries( ME, "Problem 1" );
        assertEquals( 3, wrong.size() );

    }

}
