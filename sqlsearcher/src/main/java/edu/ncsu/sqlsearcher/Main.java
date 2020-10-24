package edu.ncsu.sqlsearcher;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.ncsu.sqlsearcher.controllers.APIUserSQLController;
import edu.ncsu.sqlsearcher.models.SQLProblem;
import edu.ncsu.sqlsearcher.models.SQLStatement;

public class Main {

    static public void main ( final String[] args ) {

        final APIUserSQLController instance = new APIUserSQLController();

        // these are what students submitted
        final List<SQLStatement> submissions = SQLStatement.getAll();

        int totalCount = 0;
        int failedCount = 0;
        int errorCount = 0;
        int successCount = 0;

        // these are what they were tasked with solving
        final List<SQLProblem> problems = SQLProblem.getAll();

        for ( final SQLStatement stmt : submissions ) {

            final SQLQuery query = new SQLQuery();
            query.setQuery( stmt.getStatement() );
            query.setProblem( stmt.getProblem() );
            query.setParticipant( "AAAAAA" );

            final ResponseEntity result = instance.submitQuery( query );

            if ( result.getStatusCode() == HttpStatus.NOT_FOUND ) {
                failedCount++;
            }
            else if ( result.getStatusCode() == HttpStatus.OK ) {
                successCount++;
            }
            else if ( result.getStatusCode() == HttpStatus.BAD_REQUEST ) {
                errorCount++;
            }
            totalCount++;

        }

        System.out.printf(
                "Submitted statements: %d\nSuccessful statements: %d\nIncorrect statements: %d\nError statements:%d\n",
                totalCount, successCount, failedCount, errorCount );
        System.exit( 0 );

    }

}
