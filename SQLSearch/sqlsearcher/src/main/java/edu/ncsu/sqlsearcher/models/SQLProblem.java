package edu.ncsu.sqlsearcher.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistent entity that represents a database record of a problem for a
 * participant to solve. Contains the name of the problem and the matching pairs
 * of (source, destination) problems to select from and compare against.
 *
 * @author Kai Presler-Marshall
 *
 */
@Entity
@Table ( name = "SQLProblems" )
public class SQLProblem extends DomainObject<SQLProblem> {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long         id;

    @Column ( unique = true )
    private String       problemName;

    @ElementCollection
    private List<String> sourceTables;

    @ElementCollection
    private List<String> destinationTables;

    @SuppressWarnings ( "unchecked" )
    static public List<SQLProblem> getAll () {
        return (List<SQLProblem>) getAll( SQLProblem.class );
    }

    public SQLProblem ( final String name ) {
        this();
        setName( name );
    }

    public SQLProblem () {
        super();
    }

    public void setName ( final String name ) {
        this.problemName = name;
    }

    public String getProblemName () {
        return this.problemName;
    }

    static public SQLProblem getByName ( final String name ) {
        return (SQLProblem) getWhere( SQLProblem.class, eqList( "problemName", name ) ).get( 0 );
    }

    public static void deleteAll () {
        deleteAll( SQLProblem.class );
    }

    public List<String> getSourceTables () {
        return sourceTables;
    }

    public void setSourceTables ( final List<String> sourceTables ) {
        this.sourceTables = sourceTables;
    }

    public List<String> getDestinationTables () {
        return destinationTables;
    }

    public void setDestinationTables ( final List<String> destinationTables ) {
        this.destinationTables = destinationTables;
    }

    public SQLProblem addDestinationTable ( final String table ) {
        if ( null == destinationTables ) {
            destinationTables = new ArrayList<String>();
        }
        destinationTables.add( table );
        return this;
    }

    public SQLProblem addSourceTable ( final String table ) {
        if ( null == sourceTables ) {
            sourceTables = new ArrayList<String>();
        }
        sourceTables.add( table );
        return this;
    }

    @Override
    public Serializable getId () {
        return id;
    }

}
