package edu.ncsu.sqlsearcher.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.criterion.Criterion;

@Entity
@Table(name = "SQLStatements")
public class SQLStatement extends DomainObject<SQLStatement> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String statement;

	private String problem;

	private Calendar submitTime;

	private String participant;

	private Boolean correct;

	private Integer count;
	
	public String getCSV() {
		return String.format("%d:%s:%s:%s:%s", id, problem, participant, statement, submitTime.getTime().toString());
	}

	/**
	 * Queries that were generated after the time the experiment is started will not
	 * be displayed to students
	 */

	@NotNull
	private Boolean canBeDisplayed;

	public SQLStatement() {
		setCount(0);
		setCanBeDisplayed(true);

	}

	/**
	 * Retrieves another correct query, not written by me.
	 *
	 * @param myName
	 * @return Matching query with the lowest count;
	 */
	static public SQLStatement getAnotherCorrectQuery(final String myName, final String whichProblem) {
		final List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(eq("correct", true));
		criteria.add(ne("participant", myName));
		criteria.add(eq("problem", whichProblem));
		criteria.add(eq("canBeDisplayed", true));
		final List<SQLStatement> matching = getWhere(criteria);

		matching.sort((a, b) -> a.getCount().compareTo(b.getCount()));
		return matching.isEmpty() ? null : matching.get(0);
	}

	static public SQLStatement getMyCorrectQuery(final String myName, final String whichProblem) {
		final List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(eq("correct", true));
		criteria.add(eq("participant", myName));
		criteria.add(eq("problem", whichProblem));
		final List<SQLStatement> matching = getWhere(criteria);

		return matching.isEmpty() ? null : matching.get(0);
	}

	static public List<SQLStatement> getMyTotalQueries(final String myName, final String whichProblem) {
		final List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(eq("participant", myName));
		criteria.add(eq("problem", whichProblem));
		final List<SQLStatement> matching = getWhere(criteria);
		return matching;
	}

	static public List<SQLStatement> getMyWrongQueries(final String myName, final String whichProblem) {
		final List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(eq("correct", false));
		criteria.add(eq("participant", myName));
		criteria.add(eq("problem", whichProblem));
		final List<SQLStatement> matching = getWhere(criteria);
		Collections.shuffle(matching);
		return matching;

	}

	static public List<SQLStatement> getIncorrectQueries() {
		final List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(eq("correct", false));
		final List<SQLStatement> matching = getWhere(criteria);
		return matching;
	}

	static public List<SQLStatement> getOtherWrongQueries(final String myName, final String whichProblem) {
		final List<Criterion> criteria = new ArrayList<Criterion>();
		criteria.add(eq("correct", false));
		criteria.add(ne("participant", myName));
		criteria.add(eq("problem", whichProblem));
		criteria.add(eq("canBeDisplayed", true));
		final List<SQLStatement> matching = getWhere(criteria);
		Collections.shuffle(matching);
		return matching;
	}

	@SuppressWarnings("unchecked")
	static private List<SQLStatement> getWhere(final List<Criterion> where) {
		return (List<SQLStatement>) getWhere(SQLStatement.class, where);
	}

	@Override
	public String toString() {
		return "SQLStatement [id=" + id + ", statement=" + statement + "]";
	}

	@Override
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	static public List<SQLStatement> getAll() {
		return (List<SQLStatement>) getAll(SQLStatement.class);
	}

	static public void deleteAll() {
		deleteAll(SQLStatement.class);
	}

	public String getStatement() {
		return statement;
	}

	public SQLStatement setStatement(final String statement) {
		this.statement = statement;
		return this;
	}

	public SQLStatement setId(final Long id) {
		this.id = id;
		return this;
	}

	public Calendar getSubmitTime() {
		return submitTime;
	}

	public SQLStatement setSubmitTime(final Calendar submitTime) {
		this.submitTime = submitTime;
		return this;
	}

	public String getParticipant() {
		return participant;
	}

	public SQLStatement setParticipant(final String participant) {
		this.participant = participant;
		return this;
	}

	public Boolean getCorrect() {
		return correct;
	}

	public SQLStatement setCorrect(final Boolean correct) {
		this.correct = correct;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public SQLStatement setCount(final Integer count) {
		this.count = count;
		return this;
	}

	public String getProblem() {
		return problem;
	}

	public SQLStatement setProblem(final String whichProblem) {
		this.problem = whichProblem;
		return this;
	}

	public SQLStatement setCanBeDisplayed(final boolean canBeDisplayed) {
		this.canBeDisplayed = canBeDisplayed;
		return this;

	}

}
