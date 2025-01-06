package de.eldecker.spring.emailchecker.frontend.restclient;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;


/**
 * Bean, um Spam-Score für eine Email-Adresse von einer Instanz der entsprechenden REST-API,
 * die gerade beim Eureka-Server registriert ist, abzufragen. 
 */
@Component
public class SpamScoreAbfrageClient {

    private static Logger LOG = LoggerFactory.getLogger( SpamScoreAbfrageClient.class );
    
    /** Zufallsgenerator für Auswahl Service-Instanz. */
    private Random _zufallsGenerator = new Random();
    
    /** Objekt für HTTP-Request */
    private RestTemplate _restTemplate = new RestTemplate();
        
    /** Bean für programmatische Abfrage der verfügbaren REST-API-Instanzen. */
    @Autowired
    private EurekaClient _eurekaClient;
    
    
    /**
     * Zufällige Auswahl einer Instanz der Spam-Score-API.
     * 
     * @return Optional enthält Basis-URL falls es mindest eines Instanz gibt;
     *         Beispiel: {@code http://localhost:8020}
     */
    private Optional<String> holeServiceInstanzHost() {
        
        final Application application = _eurekaClient.getApplication( "spam-score-api" );
        final List<InstanceInfo> instanceInfoList = application.getInstances();
        
        final int anzahlInstanzen = instanceInfoList.size();
        if ( anzahlInstanzen == 0 ) {
            
            LOG.error( "Keine einzige Instanz der Spam-Score-API gefunden!" );
            return Optional.empty();
            
        } else {
            
            LOG.info( "Anzahl Instanzen Spam-Score-API gefunden: {}", anzahlInstanzen );
        }
        
        final int zufallsIndex = _zufallsGenerator.nextInt( anzahlInstanzen ); 
        final InstanceInfo zufallsInstanz = instanceInfoList.get( zufallsIndex );
        
        final String hostName    = zufallsInstanz.getHostName();
        final int    port        = zufallsInstanz.getPort();
        final String hostUndPort = "http://" + hostName + ":" + port;
        LOG.info( "Ausgewählte Instanz Spam-Score-API mit Index {}: {}", 
                  zufallsIndex, hostUndPort );
        
        return Optional.of( hostUndPort );
    }
    
    
    /**
     * Spam-Score für eine Email-Adresse von REST-API abfragen.
     * <br><br>
     * 
     * Bedeutung Spam-Score-Werte:
     * <ul>
     * <li>0: Keine Fälle für Verwendung der Email-Adresse zum Spamming bekannt</li>
     * <li>1: Einzelne Fälle für Verwendung der Email-Adresse zum Spamming bekannt</li>
     * <li>2: Mehrere Fälle für Verwendung der Email-Adresse zum Spamming bekannt</li>
     * <li>3: Viele Fälle für Verwendung der Email-Adresse zum Spamming bekannt</li>
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

        final Optional<String> hostOptional = holeServiceInstanzHost( );
        if ( hostOptional.isEmpty() ) {
            
            return Optional.empty(); 
        }
        
        String url = hostOptional.get() + "/api/v1/emailSpamScore";
        url = UriComponentsBuilder.fromUriString( url ) 
                                  .queryParam( "email_adresse", emailAdresse )
                                  .toUriString();
        LOG.info( "Komplette URL Abfrage Spam-Score: {}", url );
        
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
