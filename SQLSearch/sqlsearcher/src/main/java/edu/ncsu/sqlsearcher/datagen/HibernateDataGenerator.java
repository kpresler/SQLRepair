package edu.ncsu.sqlsearcher.datagen;

import java.text.ParseException;

import edu.ncsu.sqlsearcher.models.SQLProblem;

/**
 * Populates database with the problems to solve, but _not_ the actual data for
 * each one, since we want to manage that outside of Hibernate
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
    public static void refreshDB () throws NumberFormatException, ParseException {

        SQLProblem.deleteAll();

        {
            final SQLProblem problem1 = new SQLProblem( "Problem 1" );

            problem1.addSourceTable( "alpha" ).addSourceTable( "alpha2" ).addSourceTable( "alpha3" )
                    .addDestinationTable( "alphad" );

            problem1.addDestinationTable( "alpha2d" ).addDestinationTable( "alpha3d" ).save();

        }
        {
            final SQLProblem problem2 = new SQLProblem( "Problem 2" );
            problem2.addSourceTable( "bravo" ).addSourceTable( "bravo2" );
            problem2.addDestinationTable( "bravod" ).addDestinationTable( "bravo2d" ).save();

        }
        {
            final SQLProblem problem3 = new SQLProblem( "Problem 3" );
            problem3.addSourceTable( "charlie" ).addSourceTable( "charlie2" ).addSourceTable( "charlie3" );

            problem3.addDestinationTable( "charlied" ).addDestinationTable( "charlie2d" )
                    .addDestinationTable( "charlie3d" ).save();

        }
        {
            final SQLProblem problem4 = new SQLProblem( "Problem 4" );
            problem4.addSourceTable( "delta" );

            problem4.addDestinationTable( "deltad" ).save();
        }
        {
            final SQLProblem problem5 = new SQLProblem( "Problem 5" );
            problem5.addSourceTable( "echo" ).addSourceTable( "echo2" );

            problem5.addDestinationTable( "echod" ).addDestinationTable( "echo2d" ).save();
        }
        {
            final SQLProblem problem6 = new SQLProblem( "Problem 6" );

            problem6.addSourceTable( "foxtrot" ).addSourceTable( "foxtrot2" );

            problem6.addDestinationTable( "foxtrotd" ).addDestinationTable( "foxtrot2d" ).save();
        }
        {
            final SQLProblem problem7 = new SQLProblem( "Problem 7" );
            problem7.addSourceTable( "golf" ).addSourceTable( "golf2" );

            problem7.addDestinationTable( "golfd" ).addDestinationTable( "golf2d" ).save();
        }
        {
            final SQLProblem problem8 = new SQLProblem( "Problem 8" );
            problem8.addSourceTable( "hotel" ).addSourceTable( "hotel2" );

            problem8.addDestinationTable( "hoteld" ).addDestinationTable( "hotel2d" ).save();
        }

        {
            final SQLProblem problem9 = new SQLProblem( "Problem 9" );
            problem9.addSourceTable( "india" );

            problem9.addDestinationTable( "indiad" ).save();
        }
        {
            final SQLProblem problem10 = new SQLProblem( "Problem 10" );
            problem10.addSourceTable( "kilo" ).addSourceTable( "kilo2" );

            problem10.addDestinationTable( "kilod" ).addDestinationTable( "kilo2d" ).save();
        }

    }

}
