package edu.ncsu.sqlsearcher.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.criterion.Criterion;

/**
 * Represents an incorrect query that has been submitted by a student, and then
 * repaired. We keep all repaired queries saved to make things run faster
 * because we then don't need to re-repair them each time
 *
 * @author Kai
 *
 */
@Entity
@Table ( name = "RepairedQueries" )
public class RepairedQuery extends DomainObject<RepairedQuery> {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long    id;

    private String  query;

    private String  participant;

    private String  problem;

    private Integer count;

    public RepairedQuery ( final String query, final String participant, final String problem ) {
        this.query = query;
        this.participant = participant;
        this.problem = problem;
        setCount( 0 );
    }

    public static RepairedQuery getMyRepairsForProblem ( final String myName, final String problem ) {
        final List<Criterion> criteria = new ArrayList<Criterion>();
        criteria.add( eq( "problem", problem ) );
        if ( null != myName ) {
            criteria.add( eq( "participant", myName ) );
        }
        final List<RepairedQuery> matching = getWhere( criteria );

        matching.sort( ( a, b ) -> a.getCount().compareTo( b.getCount() ) );
        return matching.isEmpty() ? null : matching.get( 0 );
    }

    public RepairedQuery () {
    }

    public void setCount ( final Integer count ) {
        this.count = count;
    }

    public static RepairedQuery getRepairsForProblem ( final String problem ) {
        return getMyRepairsForProblem( null, problem );
    }

    public static RepairedQuery getAnotherRepairForProblem ( final String myName, final String problem ) {
        final List<Criterion> criteria = new ArrayList<Criterion>();
        criteria.add( eq( "problem", problem ) );
        criteria.add( ne( "participant", myName ) );
        final List<RepairedQuery> matching = getWhere( criteria );

        matching.sort( ( a, b ) -> a.getCount().compareTo( b.getCount() ) );
        return matching.isEmpty() ? null : matching.get( 0 );
    }

    @Override
    public Serializable getId () {
        return id;
    }

    public String getQuery () {
        return query;
    }

    public String getParticipant () {
        return participant;
    }

    public String getProblem () {
        return problem;
    }

    public Integer getCount () {
        return count;
    }

    @SuppressWarnings ( "unchecked" )
    static private List<RepairedQuery> getWhere ( final List<Criterion> where ) {
        return (List<RepairedQuery>) getWhere( RepairedQuery.class, where );
    }

    static public void deleteAll () {
        DomainObject.deleteAll( RepairedQuery.class );
    }

}
