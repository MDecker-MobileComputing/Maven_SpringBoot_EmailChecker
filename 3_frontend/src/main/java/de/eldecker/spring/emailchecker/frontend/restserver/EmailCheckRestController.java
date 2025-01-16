package de.eldecker.spring.emailchecker.frontend.restserver;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.eldecker.spring.emailchecker.frontend.restclient.SpamScoreAbfrageClient;


/**
 * REST-Controller für REST-Endpunkt, mit dem HTML-Frontend angebunden ist. 
 */
@RestController
@RequestMapping( "/api/v1" )
public class EmailCheckRestController {

    private static Logger LOG = LoggerFactory.getLogger( EmailCheckRestController.class );
    
    /** 
     * Dummy-Wert für Spam-Score, wenn wegen Syntax-Fehler in Email-Adresse der Spam-Score
     * nicht von einer externen REST-API abgefragt wurde.
     */
    private static final int SPAM_SCORE_NO_CHECK = -1;
    
    /** Dummy-Wert für Spam-Score, wenn die externe REST-API einen Fehler geliefert hat. */
    private static final int SPAM_SCORE_ERROR = -2;    
    
    /** Bool'scher Wert für den Fall, dass kein Syntax-Fehler in Email-Adresse entdeckt wurde. */
    private static final boolean SYNTAX_KORREKT = true;
    
    /** Bool'scher Wert für den Fall, dass Syntax-Fehler in Email-Adresse entdeckt wurde. */
    private static final boolean SYNTAX_FEHLER = false;
    
    /** Regulärer Ausdruck für Syntax-Prüfung einer E-Mail-Adresse ohne Unterscheidung Groß-/Kleinschreibung. */
    private static final Pattern REGEXP_EMAIL = 
            Pattern.compile( "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", CASE_INSENSITIVE );    
    
    
    /** Bean für Abfrage Spam-Score bei REST-API inkl. <i>client-seitiges Load Balancing</i>. */
    @Autowired
    private SpamScoreAbfrageClient _spamScoreClient;

    
    /**
     * REST-Endpunkt für die Überprüfung einer Email-Adresse. Es wird zuerst überprüft, ob die
     * Email-Adresse syntaktisch korrekt ist. Dann wird ihr Spam-Score von einem Microservice
     * abgefragt.
     * <br><br>
     * 
     * Beispiel-URL für lokalen Aufruf:
     * <pre>
     * http://localhost:8080/api/v1/emailCheck?email_adresse=max.mustermann@gmx.de
     * </pre>
     * 
     * @param emailAdresse Email-Adresse, die zu überprüfen ist.
     * 
     * @return HTTP-Status-Code ist immer "200 OK", auch wenn {@code emailAdresse} syntaktisch
     *         nicht in Ordnung ist
     */
    @GetMapping( "/emailCheck" )
    public ResponseEntity<SpamCheckResult> checkeEmail( 
                               @RequestParam( "email_adresse" ) String emailAdresse ) {
    
        SpamCheckResult result = null;
        
        emailAdresse = emailAdresse.trim();
        
        final boolean emailSyntaxOkay = REGEXP_EMAIL.matcher( emailAdresse ).matches();
        if ( emailSyntaxOkay == false ) {
            
            LOG.info( "Email-Adresse mit Syntax-Fehler von HTML-Frontend erhalten: {}", 
                      emailAdresse );
            
            result = new SpamCheckResult( emailAdresse,
                                          "Email-Adresse ist syntaktisch nicht korrekt.",
                                          SYNTAX_FEHLER,
                                          SPAM_SCORE_NO_CHECK 
                                        );                                            
        } else {
            
            final Optional<Integer> spamScoreOptional = _spamScoreClient.holeSpamScore( emailAdresse );
            if ( spamScoreOptional.isEmpty() ) {
                
                LOG.info( "Kein Spam-Score für Email-Adresse erhalten: {}", emailAdresse );                                          
                
                result = new SpamCheckResult( emailAdresse,
                                              "Email-Adresse ist syntaktisch korrekt, Spam-Score konnte nicht ermittelt werden.",
                                              SYNTAX_KORREKT,
                                              SPAM_SCORE_ERROR 
                                            );            
            } else {

                final int spamScore = spamScoreOptional.get();
                
                LOG.info( "Spam-Score {} für Email-Adresse {} erhalten.", spamScore, emailAdresse ); 
                
                result = new SpamCheckResult( emailAdresse,
                                              "Email-Adresse ist syntaktisch korrekt, Spam-Score konnte ermittelt werden.",
                                              SYNTAX_KORREKT,
                                              spamScore
                                            );                            
            }            
        }
                
        return ResponseEntity.status( OK ).body( result );
    }
    
}
