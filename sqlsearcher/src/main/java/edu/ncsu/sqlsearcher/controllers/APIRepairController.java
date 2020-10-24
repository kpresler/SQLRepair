package edu.ncsu.sqlsearcher.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.sqlsearcher.forms.SQLVotingOptions;
import edu.ncsu.sqlsearcher.models.RepairedQuery;
import edu.ncsu.sqlsearcher.models.SQLStatement;
import edu.ncsu.sqlsearcher.models.SQLVotingResponse;
import edu.ncsu.z3sqlparser.SQLTableExample;
import edu.ncsu.z3sqlparser.SQLTableExamples;
import edu.ncsu.z3sqlparser.controllers.APIZ3Controller;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
public class APIRepairController extends APIController {

	@PostMapping(BASE_PATH + "/requestQueries/{name}/{problem}")
	public ResponseEntity getQueriesForVoting(@PathVariable("name") final String myName,
			@PathVariable("problem") final String whichProblem) {

		if (!allowedToRequestQueries(myName, whichProblem)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		final SQLVotingOptions vo = getVotingOptions(myName, whichProblem);
		return new ResponseEntity(vo, HttpStatus.OK);

	}

	/**
	 * Checks to see if the student is allowed to look at working solutions for this
	 * problem yet. Must have submitted at least 5 queries to be allowed to do so.
	 *
	 * @param myName
	 * @param whichProblem
	 * @return
	 */
	public static boolean allowedToRequestQueries(final String myName, final String whichProblem) {
		List<SQLStatement> myQueries = SQLStatement.getMyTotalQueries(myName, whichProblem);

		myQueries.sort((a, b) -> a.getSubmitTime().compareTo(b.getSubmitTime()));

		if (myQueries.size() < 5) {
			return false;
		}

		Long oldestTime = myQueries.get(0).getSubmitTime().getTimeInMillis();

		Long now = System.currentTimeMillis();

		return now >= (oldestTime + (5 * 60 * 1000 /* 5 minutes */ ));

	}

	public static SQLVotingOptions getVotingOptions(final String myName, final String whichProblem) {
		final SQLStatement myCorrect = SQLStatement.getMyCorrectQuery(myName, whichProblem);

		final String anotherCorrectQuery = findAnotherQuery(myName, whichProblem);

		final String anotherRepairedQuery = findAnotherRepair(myName, whichProblem);

		final String myRepairedQuery = getMyRepairedQuery(myName, whichProblem);

		return new SQLVotingOptions(anotherRepairedQuery, anotherCorrectQuery, whichProblem,
				null == myCorrect ? null : myCorrect.getStatement(), myRepairedQuery);
	}

	@PostMapping(BASE_PATH + "/submitVote")
	public ResponseEntity submitVotes(@RequestBody final SQLVotingResponse response) {
		try {
			response.save();
			response.updateVotes();
			return new ResponseEntity(HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

	}

	protected static String findAnotherQuery(final String myName, final String whichProblem) {
		final SQLStatement match = SQLStatement.getAnotherCorrectQuery(myName, whichProblem);

		if (null == match) {
			return null;
		}
		// Increase the count by one since se've sent this to someone to vote on
		match.setCount(match.getCount() + 1);
		match.save();

		return match.getStatement();
	}

	protected static String findAnotherRepair(final String myName, final String whichProblem) {
		final RepairedQuery match = RepairedQuery.getAnotherRepairForProblem(myName, whichProblem);

		if (null == match) {
			return null;
		}
		// Increase the count by one since se've sent this to someone to vote on
		match.setCount(match.getCount() + 1);
		match.save();

		return match.getQuery();
	}

	protected static String getMyRepairedQuery(final String myName, final String whichProblem) {

		// Try to find one of my queries first

		List<SQLStatement> myWrong = SQLStatement.getMyWrongQueries(myName, whichProblem);

		/* Sort new to old */
		myWrong.sort((a, b) -> a.getSubmitTime().compareTo(b.getSubmitTime()));

		/* We want to repair, most recent first */
		Collections.reverse(myWrong);

		String query = repairQueries(whichProblem, myWrong, 10);

		if (null == query) {
			final RepairedQuery rq = RepairedQuery.getRepairsForProblem(whichProblem);
			query = null == rq ? null : rq.getQuery();
		}

		return query;

	}

	protected static String repairQueries(String whichProblem, final List<SQLStatement> possibleQueriesToRepair,
			int numAttempts) {
		for (int i = 0; i < numAttempts; i++) {
			/* Try repairing one at a time until we get one right, or hit the max number */
			SQLStatement statement = possibleQueriesToRepair.get(i);
			final String result = QueryRepairer.repairAQuery(whichProblem, statement);
			if (null != result) {
				return result;
			}
		}
		return null;
	}

	
}
