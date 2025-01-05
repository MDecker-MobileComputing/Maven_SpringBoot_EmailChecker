package de.eldecker.spring.emailchecker.frontend.restclient;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Bean, um Spam-Score für eine Email-Adresse von einer Instanz der entsprechenden REST-API,
 * die gerade beim Eureka-Server registriert ist, abzufragen. 
 */
@Component
public class SpamScoreAbfrageClient {

    private static Logger LOG = LoggerFactory.getLogger( SpamScoreAbfrageClient.class );
    
    
    /**
     * In der URL wird anstelle der Domain die Service-ID "spam-score-api" verwendet.     
     * Diese Service-ID ist in der Applikation, die die REST-API zur verfügung stellt,
     * in der Konfigurationsdatei {@code application.properties} unter dem Schlüssel 
     * {@code spring.application.name} definiert. 
     * <br><br>
     * 
     * URL des REST-Endpunkts: {@code api/v1/emailSpamScore }
     */
    private static final String REST_API_URL = "http://spam-score-api/api/v1/emailSpamScore";
    
    
    /** 
     * Bean für HTTP-Request mit client-seitigem Load Balancing, siehe Klasse
     * {@link LoadBalancerKonfiguration} für Konfiguration.
     */
    @Autowired
    private RestTemplate _restTemplate;
        
    
    /**
     * Spam-Score für eine Email-Adresse von REST-API abfragen.
     * <br><br>
     * 
     * Bedeutung Spam-Score-Werte:
     * <ul>
     * <li>0: Keine Fälle für Verwendung der Email-Adresse zum Spamming bekannt</li>
     * <li>1: Einzelne Fälle für Verwendung der Email-Adresse zum Spamming bekannt</li>
     * <li>2: Mehrere Fälle für Verwendung der Email-Adresse zum Spamming bekannt</li>
     * <li>3: Viele Fälle für Verwendung der Email-Adresse zum Spam bekannt</li>
     * </ul>
     * 
     * @param emailAdresse Email-Adresse (sollte schon auf syntaktische Korrektheit geprüft sein)
     * 
     * @return Optional enthält den Spam-Score für {@code emailAdresse} wenn dieser von 
     *         der REST-API zurückgeliefert wurde, im Fehlerfall ist das Optional leer.
     *         Score-Werte sind im Bereich von einschl. 0 (bester Wert) bis einschl. 3 
     *         (schlechtester Wert).
     */
    public Optional<Integer> holeSpamScore( String emailAdresse ) {

        final String url =
                UriComponentsBuilder.fromUriString( REST_API_URL ) 
                                    .queryParam( "email_adresse", emailAdresse )
                                    .toUriString();        
        try {

            final ResponseEntity<Integer> responseEntity =
                                    _restTemplate.getForEntity( url, Integer.class );
            
            final HttpStatusCode statusCode = responseEntity.getStatusCode();
            if ( statusCode.is2xxSuccessful() ) {

                final Integer spamScore = responseEntity.getBody();

                LOG.info( "Spam-Score für Email-Adresse {} von REST-API erhalten: {}",
                          emailAdresse, spamScore );

                return Optional.of( spamScore );

            } else {

                LOG.error( "HTTP-GET-Request für Abfrage Spam-Score von Email-Adresse {} lieferte Fehler-Code zurück: {}", 
                           emailAdresse, statusCode );
                return Optional.empty();
            }            
        }
        catch ( RestClientException ex ) {

            LOG.error( "HTTP-GET-Request für Abfrage Spam-Score für Email-Adresse={} hat Ausnahme geworfen.",
                       emailAdresse, ex );
            return Optional.empty();            
        }
    }
    
}
