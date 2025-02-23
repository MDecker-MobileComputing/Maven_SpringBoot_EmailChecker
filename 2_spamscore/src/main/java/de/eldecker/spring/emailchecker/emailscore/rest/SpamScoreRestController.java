package de.eldecker.spring.emailchecker.emailscore.rest;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST-Controller für REST-Endpunkt, um Spam-Score für eine Email-Adresse abzufragen. 
 */
@RestController
@RequestMapping( "/api/v1" )
public class SpamScoreRestController {

    private static Logger LOG = LoggerFactory.getLogger( SpamScoreRestController.class );
    
    
    /** Regulärer Ausdruck für Syntax-Prüfung einer E-Mail-Adresse ohne Unterscheidung Groß-/Kleinschreibung. */
    private static final Pattern REGEXP_EMAIL = 
            						Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", 
            								         CASE_INSENSITIVE );

    /** Bean um Email-Adresse samt Score in eine Datei zu schreiben. */
    @Autowired
    private EmailAdressenProtokollierer _emailProtokoll;
    
    
    /**
     * Liefert den Spam-Score für die als URL-Parameter übergebene E-Mail-Adresse zurück.
     * Für Interpreation der Spam-Score-Werte siehe Methode {@link #berechneSpamScore(String)}.
     * <br><br>
     * 
     * Beispiel-URL für lokalen Aufruf auf Portnummer 8010:
     * <pre>
     * http://localhost:8010/api/v1/emailSpamScore?email_adresse=hans@wiwi.de
     * </pre>
     * 
     * @param emailAdresse Email-Adresse aus URL-Parameter "email_adresse"
     * 
     * @return Wenn Email-Adresse syntaktisch korrekt, dann Spam-Score zwischen 0 und 3 
     *         (HTTP-Status: 200 OK), sonst -1 (HTTP-Status 400 Bad Request).         
     */
    @GetMapping( "/emailSpamScore" )
    public ResponseEntity<Integer> getSpamScore( @RequestParam( "email_adresse" ) String emailAdresse ) {
    
        emailAdresse = emailAdresse.trim();
        
        final boolean emailSyntaxOkay = REGEXP_EMAIL.matcher( emailAdresse ).matches();
        if ( emailSyntaxOkay == false ) {
            
            LOG.warn( "Ungueltige E-Mail-Adresse erhalten: {}", emailAdresse );
            return ResponseEntity.status( BAD_REQUEST )
                                 .header( "X-FEHLERMELDUNG", "Email-Adresse syntaktisch ungueltig." )
                                 .body( -1 );
        }
        
        LOG.info( "Anfrage fuer E-Mail-Adresse erhalten: {}", emailAdresse );
        
        final int spamScore = berechneSpamScore( emailAdresse );
        _emailProtokoll.protokolliereEmailAdresse( emailAdresse + ": " + spamScore );
        
        return ResponseEntity.status( OK )
                             .body( spamScore );
    }
    
    
    /**
     * Spam-Score für die als Argument übergebene E-Mail-Adresse berechnen.
     * <br><br>
     * 
     * Bedeutung Spam-Score-Werte (Häufigkeit in Klammer)
     * <ul>
     * <li>0: Keine    Fälle für Verwendung der Email-Adresse zum Spamming bekannt (30%).</li>
     * <li>1: Einzelne Fälle für Verwendung der Email-Adresse zum Spamming bekannt (40%).</li>
     * <li>2: Mehrere  Fälle für Verwendung der Email-Adresse zum Spamming bekannt (20%).</li>
     * <li>3: Viele    Fälle für Verwendung der Email-Adresse zum Spamming bekannt (10%).</li>
     * </ul>
     * 
     * @param emailAdresse Email-Adresse, muss syntaktisch korrekt sein!
     * 
     * @return Spam-Score von 0 bis 3
     */        
    private int berechneSpamScore( String emailAdresse ) {
        
        final int hashWert = Math.abs( emailAdresse.hashCode() ) % 10; 
        switch ( hashWert ) {
        
            case 0: 
            case 1:
            case 2:
            case 3: return 1; // 40%                
                
            case 4:              
            case 5: return 2; // 20%
            
            case 6: return 3; // 10%
                    
            default:
                return 0; // 30%
        }
    }

}
