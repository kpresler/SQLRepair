package edu.ncsu.sqlrepair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entrypoint for the Spring Boot application
 * 
 * @author Kai Presler-Marshall
 * @author Spring team
 *
 */
@SpringBootApplication
public class SQLRepair {

    public static void main ( final String[] args ) {
        SpringApplication.run( SQLRepair.class, args );
    }
}
