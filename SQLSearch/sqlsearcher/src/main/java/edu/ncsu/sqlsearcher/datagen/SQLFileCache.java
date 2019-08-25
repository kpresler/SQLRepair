package edu.ncsu.sqlsearcher.datagen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Helper class for reading and storing SQL files.
 * 
 * @author Kai Presler-Marshall
 *
 */
public class SQLFileCache {
    private static SQLFileCache instance;

    public static SQLFileCache getInstance () {
        if ( instance == null ) {
            instance = new SQLFileCache();
        }
        return instance;
    }

    private final HashMap<String, List<String>> cache = new HashMap<String, List<String>>( 100 );

    private SQLFileCache () {
    }

    public List<String> getQueries ( final String fileName ) throws FileNotFoundException, IOException {
        final List<String> queries = cache.get( fileName );
        if ( queries != null ) {
            return queries;
        }
        else {
            return parseAndCache( fileName );
        }
    }

    private List<String> parseAndCache ( final String fileName ) throws FileNotFoundException, IOException {
        final List<String> queries = parseSQLFile( fileName );
        cache.put( fileName, queries );
        return queries;
    }

    private List<String> parseSQLFile ( final String filepath ) throws FileNotFoundException, IOException {
        final List<String> queries = new ArrayList<String>();

        BufferedReader reader = null;
        FileReader fileReader = null;
        try {

            fileReader = new FileReader( new File( filepath ) );
            reader = new BufferedReader( fileReader );
            String line = "";
            String currentQuery = "";
            while ( ( line = reader.readLine() ) != null ) {
                if ( line.startsWith( "--" ) ) {
                    continue;
                }
                for ( int i = 0; i < line.length(); i++ ) {
                    if ( line.charAt( i ) == ';' ) {
                        queries.add( currentQuery );
                        currentQuery = "";
                    }
                    else {
                        currentQuery += line.charAt( i );
                    }
                }
            }
        }
        finally {
            try {
                if ( fileReader != null ) {
                    fileReader.close();
                }
            }
            finally {
                if ( reader != null ) {
                    reader.close();
                }
            }
        }
        return queries;
    }

}
