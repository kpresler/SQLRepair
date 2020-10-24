package edu.ncsu.sqlsearcher;

import java.text.ParseException;
import java.util.Calendar;

import edu.ncsu.sqlsearcher.models.RepairedQuery;
import edu.ncsu.sqlsearcher.models.SQLProblem;
import edu.ncsu.sqlsearcher.models.SQLStatement;

/**
 * Newly revamped Test Data Generator. This class is used to generate database
 * records for the various different types of persistent objects that exist in
 * the system. Takes advantage of Hibernate persistence. To use, instantiate the
 * type of object in question, set all of its parameters, and then call the
 * save() method on the object.
 *
 * @author Kai Presler-Marshall
 *
 */
public class HibernateDataGenerator {

    /**
     * Starts the data generator program.
     *
     * @param args
     *            command line arguments
     * @throws ParseException
     * @throws NumberFormatException
     */
    public static void main ( final String args[] ) throws NumberFormatException, ParseException {
        refreshDB();

        System.exit( 0 );
        return;
    }


	/**
	 * Generate sample users for the iTrust2 system.
	 *
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	public static void refreshDB() throws NumberFormatException, ParseException {

		SQLProblem.deleteAll();
		SQLStatement.deleteAll();

		RepairedQuery.deleteAll();

		{
			final SQLProblem problem1 = new SQLProblem("Problem 1");

			problem1.addSourceTable("alpha").addSourceTable("alpha2").addSourceTable("alpha3")
					.addDestinationTable("alphad");

			problem1.addDestinationTable("alpha2d").addDestinationTable("alpha3d").save();

		}
		{
			final SQLProblem problem2 = new SQLProblem("Problem 2");
			problem2.addSourceTable("bravo").addSourceTable("bravo2");
			problem2.addDestinationTable("bravod").addDestinationTable("bravo2d").save();

		}
		{
			final SQLProblem problem3 = new SQLProblem("Problem 3");
			problem3.addSourceTable("charlie").addSourceTable("charlie2").addSourceTable("charlie3");

			problem3.addDestinationTable("charlied").addDestinationTable("charlie2d").addDestinationTable("charlie3d")
					.save();

		}
		{
			final SQLProblem problem4 = new SQLProblem("Problem 4");
			problem4.addSourceTable("delta");

			problem4.addDestinationTable("deltad").save();
		}
		{
			final SQLProblem problem5 = new SQLProblem("Problem 5");
			problem5.addSourceTable("echo").addSourceTable("echo2");

			problem5.addDestinationTable("echod").addDestinationTable("echo2d").save();
		}
		{
			final SQLProblem problem6 = new SQLProblem("Problem 6");

			problem6.addSourceTable("foxtrot").addSourceTable("foxtrot2");

			problem6.addDestinationTable("foxtrotd").addDestinationTable("foxtrot2d").save();
		}
		{
			final SQLProblem problem7 = new SQLProblem("Problem 7");
			problem7.addSourceTable("golf").addSourceTable("golf2");

			problem7.addDestinationTable("golfd").addDestinationTable("golf2d").save();
		}
		{
			final SQLProblem problem8 = new SQLProblem("Problem 8");
			problem8.addSourceTable("hotel").addSourceTable("hotel2");

			problem8.addDestinationTable("hoteld").addDestinationTable("hotel2d").save();
		}

		{
			final SQLProblem problem9 = new SQLProblem("Problem 9");
			problem9.addSourceTable("india");

			problem9.addDestinationTable("indiad").save();
		}
		{
			final SQLProblem problem10 = new SQLProblem("Problem 10");
			problem10.addSourceTable("kilo").addSourceTable("kilo2");

			problem10.addDestinationTable("kilod").addDestinationTable("kilo2d").save();
		}

        addCorrectQueries();

        addRepairedQueries();

    }

    static private void addCorrectQueries () {
        addProblem1();
        addProblem2();
        addProblem3();
        addProblem4();
        addProblem5();
        addProblem6();
        addProblem7();
        addProblem8();

        // 9 and 10 not added because we can't repair them
    }

    static private void addProblem1 () {
        final String problem = "Problem 1";
        createStatement( "select * from alpha where MIN = 0", problem );
        createStatement( "SELECT * FROM alpha WHERE MIN = 0;", problem );
        createStatement( "select COL, DES, MIN, AV, MAX, FIL from alpha where min = 0 ", problem );
        createStatement( "SELECT * FROM alpha WHERE MIN = 0 ", problem );
        createStatement( "SELECT * FROM alpha WHERE alpha.min = 0 ", problem );
        createStatement( "SELECT * from alpha where MIN=0; ", problem );
        createStatement( "select * from alpha where min = '0'", problem );
    }

    static private void addProblem2 () {
        final String problem = "Problem 2";
        createStatement( "SELECT CUI1, RUI FROM bravo WHERE CUI2='C0364349'; ", problem );
        createStatement( "SELECT CUI1, RUI FROM bravo WHERE bravo.CUI2 = 'C0364349'  ", problem );
        createStatement( "select CUI1, RUI from bravo where CUI2 = 'C0364349'", problem );
        createStatement( "select CUI1, RUI FROM bravo where CUI2 = 'C0364349'", problem );

    }

    static private void addProblem3 () {
        final String problem = "Problem 3";
        createStatement( "SELECT * FROM charlie WHERE TFR < 2000 && CFR < 1650 ", problem );
        createStatement( "SELECT * FROM charlie WHERE CFR < 1635", problem );
        createStatement( "SELECT * FROM charlie WHERE CFR < 1635;", problem );
        createStatement( "SELECT * FROM charlie WHERE CFR < 1650; ", problem );
        createStatement( "select * from charlie where CFR < 1635", problem );

    }

    static private void addProblem4 () {
        final String problem = "Problem 4";
        createStatement( "select RSAB, TFR from delta where CFR < 1696", problem );
        createStatement( "SELECT RSAB, TFR FROM delta WHERE TFR < 1900 && CFR < 1650", problem );
    }

    static private void addProblem5 () {
        final String problem = "Problem 5";
        createStatement( "select * from echo where MRRANK_RANK < 380 or TTY = 'CD'", problem );
    }

    static private void addProblem6 () {
        final String problem = "Problem 6";
        createStatement( "select * from foxtrot where TTY = 'PT' or (TTY = 'SY' and MRRANK_RANK > 400)", problem );
        createStatement(
                "SELECT * FROM foxtrot WHERE foxtrot.tty = \"PT\" OR foxtrot.tty = \"SY\" AND foxtrot.mrrank_rank > 400",
                problem );
        createStatement( "select * from foxtrot where MRRANK_RANK > 400 and tty = 'SY' or tty = 'PT'", problem );
    }

    static private void addProblem7 () {
        final String problem = "Problem 7";
        createStatement( "select distinct SVER from golf where SVER <= 1995", problem );
        createStatement( "SELECT DISTINCT SVER FROM golf WHERE SVER < 1996", problem );
        createStatement( "SELECT DISTINCT SVER FROM golf WHERE SVER < 1996;", problem );
        createStatement( "SELECT DISTINCT SVER from golf WHERE SVER < 1996", problem );
        createStatement( "SELECT DISTINCT SVER from golf where SVER<1996;", problem );
        createStatement( "SELECT DISTINCT SVER FROM golf WHERE SVER < 1996", problem );
    }

    static private void addProblem8 () {
        final String problem = "Problem 8";
        createStatement( "SELECT * from hotel; ", problem );
    }

    static private void createStatement ( final String query, final String problem ) {
        final SQLStatement stmt = new SQLStatement();
        stmt.setCorrect( true );
        stmt.setCount( 0 );
        stmt.setParticipant( "kpresler" );
        stmt.setProblem( problem );
        stmt.setStatement( query );
        stmt.setSubmitTime( Calendar.getInstance() );
        stmt.save();
    }

    static public void addRepairedQueries () {
        RepairedQuery.deleteAll();

        new RepairedQuery( "SELECT * FROM alpha WHERE MIN = 0", "SYSTEM", "Problem 1" ).save();

        new RepairedQuery( "SELECT CUI1, RUI FROM bravo WHERE CUI2 = 'C0364349'", "SYSTEM", "Problem 2" ).save();

        new RepairedQuery( "SELECT * FROM charlie WHERE CFR < 1635", "SYSTEM", "Problem 3" ).save();
        new RepairedQuery( "SELECT RSAB, SF, TFR, CFR, TTYL FROM charlie WHERE CFR < 1635", "SYSTEM", "Problem 3" )
                .save();
        new RepairedQuery( "SELECT RSAB, SF, TFR, CFR, TTYL FROM charlie WHERE CFR <= 1634", "SYSTEM", "Problem 3" )
                .save();

        new RepairedQuery( "SELECT RSAB, TFR FROM delta WHERE CFR < 1635", "SYSTEM", "Problem 4" ).save();

        new RepairedQuery( "SELECT * FROM echo WHERE TTY = 'CD' OR MRRANK_RANK < 380", "SYSTEM", "Problem 5" ).save();
        new RepairedQuery( "SELECT * FROM echo WHERE MRRANK_RANK < 380 OR TTY = 'CD'", "SYSTEM", "Problem 5" ).save();
        new RepairedQuery( "SELECT MRRANK_RANK, SAB, TTY, SUPPRESS FROM echo WHERE MMRRANK_RANK <= 379 OR TTY = 'CD'",
                "SYSTEM", "Problem 5" ).save();

        new RepairedQuery( "SELECT * FROM foxtrot WHERE TTY = 'PT' OR TTY = 'SY' AND MRRANK_RANK>=400", "SYSTEM",
                "Problem 6" ).save();
        new RepairedQuery(
                "SELECT * FROM foxtrot WHERE TTY = 'PT' OR (TTY = 'SY' AND MRRANK_RANK > 400) ORDER BY MRRANK_RANK DESC",
                "SYSTEM", "Problem 6" ).save();

        new RepairedQuery( "SELECT DISTINCT SVER FROM golf WHERE SVER < 1996", "SYSTEM", "Problem 7" ).save();
        new RepairedQuery( "SELECT DISTINCT SVER FROM golf WHERE SVER < 1996;", "SYSTEM", "Problem 7" )
                .save();
        new RepairedQuery( "SELECT DISTINCT SVER FROM golf WHERE SVER <= 1995", "SYSTEM", "Problem 7" ).save();

        new RepairedQuery( "SELECT CUI, TUI, STN FROM hotel ORDER BY TUI DESC", "SYSTEM", "Problem 8" ).save();
        new RepairedQuery( "SELECT CUI, TUI, STN FROM hotel ORDER BY TUI DESC, CUI ASC", "SYSTEM", "Problem 8" ).save();
        new RepairedQuery( "SELECT * FROM hotel ORDER BY TUI DESC, CUI ASC", "SYSTEM", "Problem 8" ).save();
        new RepairedQuery( "SELECT * FROM hotel ORDER BY TUI DESC", "SYSTEM", "Problem 8" ).save();
        new RepairedQuery( "SELECT * FROM hotel ORDER BY TUI DESC, CUI", "SYSTEM", "Problem 8" ).save();

    }



}
