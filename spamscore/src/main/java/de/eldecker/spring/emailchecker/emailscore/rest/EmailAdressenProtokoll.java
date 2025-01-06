package de.eldecker.spring.emailchecker.emailscore.rest;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.LocalDateTime.now;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


/**
 * Klasse für Bean, um in eine Log-Datei alle empfangenen Email-Adressen und Scores
 * zu schreiben. 
 */
@Component
public class EmailAdressenProtokoll {

    private static Logger LOG = LoggerFactory.getLogger( EmailAdressenProtokoll.class );
    
    /** Objekt für Datum/Zeit am Anfang von Dateiname. */
    private static final DateTimeFormatter DATUMSFORMATIERER = ofPattern( "yyyy-MM-dd_HH-mm" );
    
    /** Dateiname mit Portnummer, wird lazy erzeugt. */
    private String _dateiname = null;
    
    /** Bean zur Abfrage der Portnummer. */
    @Autowired
    private Environment _environment;
    
    @PostConstruct
    public void initialisierung() {
        
        protokolliereEmailAdresse( "" );
    }
    
    
    /**
     * Eintrag in Protokolldatei erzeugen.
     * 
     * @param emailAdresse Email-Adresse
     * 
     * @param spamScore Spam-Score von {@code Email-Adresse}
     */
    public void protokolliereEmailAdresse( String zeile ) {
        
        if ( _dateiname == null ) {
            
            final String serverPort = _environment.getProperty( "server.port", "portunbekannt" );

            final String datumZeit = DATUMSFORMATIERER.format( now() );
            
            _dateiname = String.format( "%s_email-adressen_%s.log", datumZeit, serverPort );
            
            LOG.info( "Dateiname Email-Adressen-Protokoll: {}", _dateiname );
        }
        
        
        BufferedWriter bf = null;
        try {

            final Path emailProtokollPath = Paths.get( _dateiname );
            if ( Files.exists( emailProtokollPath ) == false ) {
                
                Files.createFile( emailProtokollPath );
            }

            bf = Files.newBufferedWriter( emailProtokollPath, UTF_8, APPEND, CREATE );
                        
            bf.write( zeile + "\n" );
        }
        catch ( IOException ex ) {
            
            LOG.error( "Konnte Zeile \"{}\" nicht in Datei {} schreiben: " + ex.getMessage(),
                       zeile, _dateiname, ex );
        }
        finally {
            
            if ( bf != null ) {
                
                try {
                    bf.close(); 
                }
                catch ( IOException ex ) {
      
                    LOG.error( "Fehler beim Schliessen der Datei {}: " + ex.getMessage(), 
                               _dateiname );
                }
            }
        }
    }
    
}
