package de.eldecker.spring.emailchecker.emailscore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Einstiegsklasse der Anwendung.
 */
@SpringBootApplication
public class EmailScoreApplication {

    /**
     * Einstiegsmethode der Anwendung.
     * 
     * @param args Kommandozeilenargumente werden an Spring durchgereicht.     
     */
	public static void main( String[] args ) {
	    
		SpringApplication.run( EmailScoreApplication.class, args );
	}

}
