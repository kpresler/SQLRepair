package edu.ncsu.sqlsearcher.datagen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import edu.ncsu.sqlsearcher.SQLExecutor;

/**
 * Populates the database with the test data for individual problems.
 *
 * @author Kai Presler-Marshall
 *
 */
public class DBBuilder {

    public static void main ( final String[] args ) throws Exception {
        executeSQLFile( "sql/dropTables.sql" );
        executeSQLFile( "sql/createTables.sql" );
        createData();
        System.out.println( "Finished" );
    }

    static private void createData () throws FileNotFoundException, IOException, SQLException {
        executeSQLFile( "sql/Problem1.sql" );
        executeSQLFile( "sql/Problem2.sql" );
        executeSQLFile( "sql/Problem3.sql" );
        executeSQLFile( "sql/Problem4.sql" );
        executeSQLFile( "sql/Problem5.sql" );
        executeSQLFile( "sql/Problem6.sql" );
        executeSQLFile( "sql/Problem7.sql" );
        executeSQLFile( "sql/Problem8.sql" );
        executeSQLFile( "sql/Problem9.sql" );
        executeSQLFile( "sql/Problem10.sql" );
    }

    static private void executeSQLFile ( final String filepath )
            throws FileNotFoundException, SQLException, IOException {
        for ( final String query : SQLFileCache.getInstance().getQueries( ( filepath ) ) ) {
            SQLExecutor.executeSQL( query );
        }
    }
}
