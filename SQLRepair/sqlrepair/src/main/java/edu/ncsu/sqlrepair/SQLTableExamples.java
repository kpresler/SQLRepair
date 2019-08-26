package edu.ncsu.sqlrepair;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a collection of multiple SQL table examples (See class
 * SQLTableExample, in this package) and the query to repair using them. Each
 * one is a (source, destination) tuple from the user of what transformation it
 * is that they want. Iterable over the SQLTableExample for for-each loops.
 *
 * @author Kai Presler-Marshall
 *
 */
public class SQLTableExamples implements Iterable<SQLTableExample> {

    /**
     * The query to repair using the examples
     */
    private final String                queryToRepair;

    /**
     * List of the examples to use for repair process
     */
    final private List<SQLTableExample> examples;

    /* Constructors... */

    public SQLTableExamples ( final List<SQLTableExample> examples,
            final String queryToRepair ) {
        this.examples = examples;
        this.queryToRepair = queryToRepair;
    }

    public SQLTableExamples () {
        this( Collections.emptyList(), null );
    }

    public SQLTableExamples ( final String tableToRepair,
            final SQLTableExample... ex ) {
        this( Arrays.asList( ex ), tableToRepair );
    }

    @Override
    public Iterator<SQLTableExample> iterator () {
        return examples.iterator();
    }

    public String getQueryToRepair () {
        return this.queryToRepair;

    }

    public List<SQLTableExample> getExamples () {
        return examples;
    }

}
