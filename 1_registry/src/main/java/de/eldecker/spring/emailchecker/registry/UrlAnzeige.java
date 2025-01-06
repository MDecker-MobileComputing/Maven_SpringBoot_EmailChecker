package de.eldecker.spring.emailchecker.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 * Diese Klasse gibt die lokale URL für das Eureka-Dashboard auf den
 * Logger aus.
 */
@Component
public class UrlAnzeige implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger( UrlAnzeige.class );
    
    
    /** Bean für Abfrage Portnummer */
    @Autowired
    private Environment _environment;
    
    
    /**
     * Diese Methode wird nach dem Start der Anwendung ausgeführt.
     * Schreibt Hinweis mit URL für Dashboard ins Log.
     */
    @Override
    public void run( String... args ) throws Exception {
        
        final String portNummer = _environment.getProperty( "server.port" );
        
        LOG.info( "Eureka-Dashboard erreichbar unter http://localhost:{}", 
                  portNummer );
    }
    
}
