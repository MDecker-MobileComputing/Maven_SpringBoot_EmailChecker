package de.eldecker.spring.emailchecker.emailscore.rest;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping( "/api/v1" )
public class SpamScoreRestController {

    private static Logger LOG = LoggerFactory.getLogger( SpamScoreRestController.class );
    
    /** Regulärer Ausdruck für Syntax-Prüfung einer E-Mail-Adresse. */
    private static final Pattern REGEXP_EMAIL = 
            Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", CASE_INSENSITIVE );

    
    /**
     * Liefert den Spam-Score für die als URL-Parameter übergebene E-Mail-Adresse zurück.
     * <br><br>
     * 
     * Beispiel-URL für lokalen Aufruf auf Portnummer 8010:
     * <pre>
     * http://localhost:8010/api/v1/emailSpamScore?email_adresse=hans@wiwi.de
     * </pre>
     * 
     * @param emailAdresse Email-Adresse aus URL-Parameter "email_adresse"
     * 
     * @return Wenn Email-Adresse syntaktisch korrekt, dann Spam-Score zwischen 1 und 10 
     *         (HTTP-Status: 200 OK), sonst -1 (HTTP-Status 400 Bad Request).         
     */
    @GetMapping( "/emailSpamScore" )
    public ResponseEntity<Integer> getSpamScore( @RequestParam("email_adresse") String emailAdresse ) {
    
        emailAdresse = emailAdresse.trim();
        
        final boolean emailSyntaxOkay = REGEXP_EMAIL.matcher( emailAdresse ).matches();
        if ( emailSyntaxOkay == false ) {
            
            LOG.warn( "Ungueltige E-Mail-Adresse erhalten: {}", emailAdresse );
            return ResponseEntity.status( BAD_REQUEST )
                                 .header( "X-FEHLERMELDUNG", "Email-Adresse syntaktisch ungueltig" )
                                 .body( -1 );
        }
        
        LOG.info( "Anfrage fuer E-Mail-Adresse erhalten: {}", emailAdresse );
        
        final int hashWert = Math.abs( emailAdresse.hashCode() );
        final int spamScore = ( hashWert % 10 ) + 1;
        
        return ResponseEntity.status( OK ).body( spamScore );
    }
    
}
