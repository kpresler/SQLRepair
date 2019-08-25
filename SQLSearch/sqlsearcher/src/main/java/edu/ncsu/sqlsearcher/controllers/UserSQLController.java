package edu.ncsu.sqlsearcher.controllers;

import java.util.Calendar;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.sqlsearcher.SQLExecutor;
import edu.ncsu.sqlsearcher.SQLQuery;
import edu.ncsu.sqlsearcher.SQLResponse;
import edu.ncsu.sqlsearcher.models.SQLProblem;
import edu.ncsu.sqlsearcher.models.SQLStatement;

/**
 * Main controller class for this application. Takes a SQLQuery from a
 * participant, executes it, and then compares the actual results from the
 * submitted query against what should have been generated to give feedback on
 * what, if anything, was done wrong.
 *
 * @author Kai Presler-Marshall
 *
 */
@SuppressWarnings ( { "rawtypes", "unchecked" } )
@RestController
public class UserSQLController extends APIController {

    @PostMapping ( BASE_PATH + "/submitQuery" )
    public ResponseEntity submitQuery ( @RequestBody final SQLQuery query ) {

        final SQLProblem problem = SQLProblem.getByName( query.getProblem() );

        final List<String> sourceTables = problem.getSourceTables();

        final List<String> destTables = problem.getDestinationTables();

        /* Maintain separate index so we can retrieve from other List too */
        int index = 0;
        for ( final String hiddenTable : sourceTables ) {
            List results;
            try {
                /*
                 * Save the statement generated so that we can try to repair it
                 * later
                 */
                new SQLStatement().setStatement( query.getQuery() ).setSourceTable( hiddenTable )
                        .setDestinationTable( destTables.get( index ) ).setSubmitTime( Calendar.getInstance() )
                        .setParticipant( query.getParticipant() ).save();
                /* Then run it to see what the result was */
                results = SQLExecutor.executeSQL( query.getQuery().replace( sourceTables.get( 0 ), hiddenTable ) );
            }
            catch ( final Exception e ) {
                return new ResponseEntity( errorResponse( e.getCause().getMessage() ), HttpStatus.BAD_REQUEST );
            }

            /*
             * For each exercise, we'll make a table that contains exactly what
             * should have been there, so we can just retrieve everything from
             * it
             */
            final List expectedResults = SQLExecutor.executeSQL( "SELECT * FROM " + destTables.get( index ) );

            final List sourceTableResults = SQLExecutor.executeSQL( "SELECT * FROM " + hiddenTable );

            /*
             * Lazy appraoch for seeing if the two tables are easy; since we
             * don't need a diff of the differences that's fine. Return an error
             * for the first (source, destination) pair where there is a
             * mismatch.
             */
            if ( !results.equals( expectedResults ) ) {
                return new ResponseEntity( new SQLResponse( sourceTableResults, expectedResults, results,
                        sourceTables.get( 0 ), String.format( "for test %d of %d", ( index + 1 ), sourceTables.size() ),
                        String.format(
                                "An error occurred comparing the output from your query with the expected output in test %d of %d",
                                ( index + 1 ), sourceTables.size() ) ), // +1
                                                                        // since
                                                                        // normal
                                                                        // people
                                                                        // don't
                                                                        // like
                                                                        // 0-indexed
                                                                        // results
                        HttpStatus.NOT_FOUND );
            }

            index++;

        }

        /* Should everything work for all problems, indicate so */
        return new ResponseEntity(
                successResponse(
                        "Good work, results matched!  You've successfully solved " + query.getProblem() + "." ),
                HttpStatus.OK );

    }

}
