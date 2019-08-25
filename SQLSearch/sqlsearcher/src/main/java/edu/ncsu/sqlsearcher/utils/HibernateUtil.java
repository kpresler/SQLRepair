package edu.ncsu.sqlsearcher.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * A utility class for setting up the Hibernate SessionFactory
 *
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory () {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        }
        catch ( final Throwable ex ) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println( "Initial SessionFactory creation failed." + ex );
            throw new ExceptionInInitializerError( ex );
        }
    }

    /**
     * Returns the Hibernate SessionFactory.
     *
     * @return session factory
     */
    public static Session openSession () {
        return sessionFactory.openSession();
    }

    /**
     * Shuts down the connection to the database.
     */
    public static void shutdown () {
        // Close caches and connection pools
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }
}
