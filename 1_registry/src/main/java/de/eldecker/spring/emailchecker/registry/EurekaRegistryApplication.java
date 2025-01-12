package de.eldecker.spring.emailchecker.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * Klasse mit Einstiegsmethode. Man beachte die Annotation
 * {@code EnableEurekaServer}.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaRegistryApplication {

    
    /**
     * Einstiegsmethode.
     * 
     * @param args Kommandozeilenargumente werden an Spring durchgereicht
     */
	public static void main( String[] args ) {

		SpringApplication.run( EurekaRegistryApplication.class, args );
	}

}
