package edu.ncsu.sqlrepair;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a collection of multiple SQL table examples. Each one is a
 * (source, destination) tuple from the user of what transformation it is that
 * they want
 *
 * @author kai
 *
 */
public class SQLTableExamples implements Iterable<SQLTableExample> {

    private final String                queryToRepair;

    final private List<SQLTableExample> examples;

    public List<SQLTableExample> getExamples () {
        return examples;
    }

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

}
