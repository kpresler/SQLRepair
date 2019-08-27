package edu.ncsu.sqlrepair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statements;

/**
 * Parser for SQL queries. Uses JSQLParser to ensure that we don't waste time on
 * something with syntax errors.
 *
 * @author Kai Presler-Marshall
 *
 */
public class SQLParser {

    private Statements sqlStatements;

    /**
     * Private to force users to use a constructor that will actually initialize
     * things but still allows us to use a single path of construction.
     */
    private SQLParser () {

    }

    public SQLParser ( final String sql ) throws JSQLParserException {
        this();
        fromString( sql );

    }

    public SQLParser ( final File fileInput ) throws JSQLParserException {
        this();
        Scanner sc = null;
        try {
            sc = new Scanner( fileInput );
        }
        catch ( final FileNotFoundException e ) {
            throw new IllegalArgumentException( "File cannot be found" );
        }

        final StringBuilder sb = new StringBuilder();

        while ( sc.hasNextLine() ) {
            sb.append( sc.nextLine() );
        }

        sc.close();

        fromString( sb.toString() );

    }

    private void fromString ( final String sql ) throws JSQLParserException {
        sqlStatements = CCJSqlParserUtil.parseStatements( sql );
    }

    public Statements getSQLStatements () {
        return this.sqlStatements;
    }

}
