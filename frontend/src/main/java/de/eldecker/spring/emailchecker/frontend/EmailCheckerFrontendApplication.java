package de.eldecker.spring.emailchecker.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Klasse mit Einstiegsmethode.
 */
@SpringBootApplication
public class EmailCheckerFrontendApplication {

    
    /**
     * Einstiegsmethode des Programm.
     * 
     * @param args Kommandozeilenargument, werden an Spring durchgereicht
     */
	public static void main( String[] args ) {
	    
		SpringApplication.run( EmailCheckerFrontendApplication.class, args );
	}

}
