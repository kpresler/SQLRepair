package edu.ncsu.sqlsearcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.ncsu.sqlsearcher.controllers.APIRepairController;
import edu.ncsu.sqlsearcher.models.SQLProblem;
import edu.ncsu.sqlsearcher.models.SQLStatement;

public class QueryClassifier {

	public static void main(String[] args) {
		
		List<SQLStatement> incorrect = SQLStatement.getIncorrectQueries();
		
		List<SQLStatement> semErrors = new ArrayList<SQLStatement>();
		
		List<SQLStatement> synErrors = new ArrayList<SQLStatement>();
		
		incorrect.forEach(statement -> {
	        final SQLProblem prblm = SQLProblem.getByName( statement.getProblem() );

	        final List<String> hiddenTables = prblm.getSourceTables();

	        final List<String> hiddenDestTables = prblm.getDestinationTables();


	        int index = 0;

	        for ( final String hiddenTable : hiddenTables ) {
	            List results = new ArrayList();
	            try {

	                results = SQLExecutor.executeSQL( statement.getStatement().replace( hiddenTables.get( 0 ), hiddenTable ) );

	            }
	            catch ( final Exception e ) {
	                synErrors.add(statement);
	                return;
	            }

	            /*
	             * For each exercise, we'll make a table that contains exactly what
	             * should have been there, so we can just retrieve everything from
	             * it
	             */
	            final List expectedResults = SQLExecutor.executeSQL( "SELECT * FROM " + hiddenDestTables.get( index ) );

	            /*
	             * Lazy approach for seeing if the two tables are easy; since we
	             * don't need a diff of the differences that's fine
	             */
	            if ( !results.equals( expectedResults ) ) {
	                semErrors.add(statement);
	                return;
	            }

	            index++;

	        }
		});
		
		
		
		System.out.println("*******Syntax Errors*******: " + synErrors.size());
		System.out.println("id, problem, participant, statement, submitTime");
		synErrors.forEach(statement -> System.out.println(statement.getCSV()));
		
		
		System.out.println("*******Semantic Errors*******: " + semErrors.size());
		System.out.println("id, problem, participant, statement, submitTime");
		semErrors.forEach(statement -> System.out.println(statement.getCSV()));
		
		System.exit(0);
	}
	
}
