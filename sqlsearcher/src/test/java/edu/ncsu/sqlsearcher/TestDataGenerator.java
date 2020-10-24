package edu.ncsu.sqlsearcher;

import java.util.Calendar;

import edu.ncsu.sqlsearcher.models.RepairedQuery;
import edu.ncsu.sqlsearcher.models.SQLStatement;

/**
 * Separate from the HDG, which is actually production-facing, this is entirely
 * for making things for testing with
 *
 * @author Kai Presler-Marshall
 *
 */
public class TestDataGenerator {

    static private final String ERIC = "ewhorton";
    static private final String ME   = "kpresle";

    static public void main ( final String[] args ) {
        {
            final SQLStatement stmt = new SQLStatement();
            stmt.setStatement( "SELECT * FROM alpha WHERE MIN = 0 ORDER BY 5,4,3,2;" );
            stmt.setProblem( "Problem 1" );
            stmt.setSubmitTime( Calendar.getInstance() );
            stmt.setParticipant( ERIC );
            stmt.setCorrect( true );
        }
        {
            final RepairedQuery rq = new RepairedQuery( "SELECT * FROM beta WHERE min = 0 ORDER BY 1,2,3,4,5", ME,
                    "Problem 1" );
            rq.save();
        }

        System.exit( 0 );

    }

}
