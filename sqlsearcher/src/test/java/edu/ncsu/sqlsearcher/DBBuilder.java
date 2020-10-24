package edu.ncsu.sqlsearcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Drops and rebuilds the entire database. Also provides some utility methods.
 * DO NOT PUT TEST DATA HERE!!!
 */
public class DBBuilder {

    public static void main ( final String[] args ) throws Exception {
//        executeSQLFile( "sql/dropTables.sql" );
        System.out.println( "Dropped existing tables" );
        Thread.sleep( 500 );

        executeSQLFile( "sql/createTables.sql" );

        Thread.sleep( 500 );
        System.out.println( "Built new tables" );

        createData();

        System.out.println( "Finished: erreicht" );
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
