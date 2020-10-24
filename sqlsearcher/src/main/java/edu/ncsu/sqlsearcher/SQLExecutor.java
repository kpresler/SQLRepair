package edu.ncsu.sqlsearcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLExecutor {

    private static final String user     = "root";
    private static final String password = "";
    private static final String jdbcURL  = "jdbc:mariadb://localhost/sqlsearchdata?createDatabaseIfNotExist=true";

    static public List<List<Object>> executeSQL ( final String query ) {

        final List<List<Object>> results = new ArrayList<List<Object>>();

        Connection connection = null;

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection( jdbcURL, user, password );
            statement = connection.createStatement();

            resultSet = statement.executeQuery( query );

            final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            final int columnCount = resultSetMetaData.getColumnCount();

            final List<Object> columns = new ArrayList<Object>();

            for ( int i = 1; i <= columnCount; i++ ) {
                columns.add( resultSetMetaData.getColumnLabel( i ) );
            }

            results.add( columns );

            while ( resultSet.next() ) {
                final List<Object> aRow = new ArrayList<Object>();
                for ( int i = 1; i <= columnCount; i++ ) {
                    aRow.add( resultSet.getObject( i ) );
                }
                results.add( aRow );

            }

        }
        catch ( final SQLException e ) {
            e.printStackTrace( System.err );
            throw new RuntimeException( e );
        }
        finally {
            try {
                resultSet.close();
            }
            catch ( SQLException | NullPointerException e ) {
            }
            try {
                statement.close();
            }
            catch ( SQLException | NullPointerException e ) {
            }
            try {
                connection.close();
            }
            catch ( SQLException | NullPointerException e ) {
            }
        }

        return results;
    }

}
