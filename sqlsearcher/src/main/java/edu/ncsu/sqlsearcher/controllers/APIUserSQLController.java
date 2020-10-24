package edu.ncsu.sqlsearcher.controllers;

import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.sqlsearcher.SQLExecutor;
import edu.ncsu.sqlsearcher.SQLQuery;
import edu.ncsu.sqlsearcher.SQLResponse;
import edu.ncsu.sqlsearcher.SQLSuccessResponse;
import edu.ncsu.sqlsearcher.forms.SQLVotingOptions;
import edu.ncsu.sqlsearcher.models.SQLProblem;
import edu.ncsu.sqlsearcher.models.SQLStatement;

@SuppressWarnings ( { "rawtypes", "unchecked" } )
@RestController
public class APIUserSQLController extends APIController {

    private static final FileWriter fw;

    static {
        try {
            fw = new FileWriter( "sql_execution_log.txt" );
        }
        catch ( final Exception e ) {
            throw new RuntimeException( e );
        }
    }

    @PostMapping ( BASE_PATH + "/submitQuery" )
    public ResponseEntity submitQuery ( @RequestBody final SQLQuery query ) {

        final SQLProblem prblm = SQLProblem.getByName( query.getProblem() );

        final List<String> hiddenTables = prblm.getSourceTables();

        final List<String> hiddenDestTables = prblm.getDestinationTables();

        final SQLStatement stmt = new SQLStatement();
        stmt.setCount( 0 );
        stmt.setParticipant( query.getParticipant() );
        stmt.setStatement( query.getQuery() );
        stmt.setProblem( query.getProblem() );
        stmt.setSubmitTime( Calendar.getInstance() );
        stmt.setProblem( query.getProblem() );
        stmt.setCanBeDisplayed( false );

        int index = 0;

        for ( final String hiddenTable : hiddenTables ) {
            List results;
            try {
                fw.write( String.format( "#### Executing query %s for user %s at time %s\n", query.getQuery(),
                        query.getParticipant(), new Date().toString() ) );

                fw.flush();

                results = SQLExecutor.executeSQL( query.getQuery().replace( hiddenTables.get( 0 ), hiddenTable ) );

            }
            catch ( final Exception e ) {
                stmt.setCorrect( false );
                stmt.save();
                return new ResponseEntity(
                        new SQLResponse( e.getCause().getMessage(), APIRepairController
                                .allowedToRequestQueries( query.getParticipant(), query.getProblem() ) ),
                        HttpStatus.BAD_REQUEST );
            }

            /*
             * For each exercise, we'll make a table that contains exactly what
             * should have been there, so we can just retrieve everything from
             * it
             */
            final List expectedResults = SQLExecutor.executeSQL( "SELECT * FROM " + hiddenDestTables.get( index ) );

            final List sourceTableResults = SQLExecutor.executeSQL( "SELECT * FROM " + hiddenTable );

            /*
             * Lazy approach for seeing if the two tables are easy; since we
             * don't need a diff of the differences that's fine
             */
            if ( !results.equals( expectedResults ) ) {
                stmt.setCorrect( false );
                stmt.save();
                return new ResponseEntity( new SQLResponse( sourceTableResults, expectedResults, results,
                        hiddenTables.get( 0 ), String.format( "for test %d of %d", ( index + 1 ), hiddenTables.size() ),
                        String.format(
                                "An error occurred comparing the output from your query with the expected output in test %d of %d",
                                ( index + 1 ), hiddenTables.size() ),
                        APIRepairController.allowedToRequestQueries( query.getParticipant(), query.getProblem() ) ),
                        HttpStatus.NOT_FOUND );
            }

            index++;

        }

        stmt.setCorrect( true );
        stmt.save();

        final SQLVotingOptions vo = APIRepairController.getVotingOptions( query.getParticipant(), query.getProblem() );

        return new ResponseEntity(
                new SQLSuccessResponse(
                        "Good work, results matched!  You've successfully solved " + query.getProblem() + ".", vo ),
                HttpStatus.OK );

    }

}
