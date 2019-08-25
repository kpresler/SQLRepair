package edu.ncsu.sqlsearcher.models;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistent entity that represents a concrete submission from a participant
 * using this application. Keeps track of the statement submitted, the tables it
 * was submitted against, when it was submitted, and who submitted it.
 *
 * @author Kai Presler-Marshall
 *
 */
@Entity
@Table ( name = "SQLStatements" )
public class SQLStatement extends DomainObject<SQLStatement> {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long     id;

    private String   statement;

    private String   sourceTable;

    private String   destinationTable;

    private Calendar submitTime;

    private String   participant;

    public SQLStatement () {

    }

    @Override
    public String toString () {
        return "SQLStatement [id=" + id + ", statement=" + statement + "]";
    }

    @Override
    public Long getId () {
        return id;
    }

    @SuppressWarnings ( "unchecked" )
    static public List<SQLStatement> getAll () {
        return (List<SQLStatement>) getAll( SQLStatement.class );
    }

    static public void deleteAll () {
        deleteAll( SQLStatement.class );
    }

    public String getStatement () {
        return statement;
    }

    public SQLStatement setStatement ( final String statement ) {
        this.statement = statement;
        return this;
    }

    public String getSourceTable () {
        return sourceTable;
    }

    public SQLStatement setSourceTable ( final String sourceTable ) {
        this.sourceTable = sourceTable;
        return this;
    }

    public String getDestinationTable () {
        return destinationTable;
    }

    public SQLStatement setDestinationTable ( final String destinationTable ) {
        this.destinationTable = destinationTable;
        return this;
    }

    public SQLStatement setId ( final Long id ) {
        this.id = id;
        return this;
    }

    public Calendar getSubmitTime () {
        return submitTime;
    }

    public SQLStatement setSubmitTime ( final Calendar submitTime ) {
        this.submitTime = submitTime;
        return this;
    }

    public String getParticipant () {
        return participant;
    }

    public SQLStatement setParticipant ( final String participant ) {
        this.participant = participant;
        return this;
    }

}
