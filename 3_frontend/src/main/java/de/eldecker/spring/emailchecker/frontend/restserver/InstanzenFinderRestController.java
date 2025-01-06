package de.eldecker.spring.emailchecker.frontend.restserver;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST-Controller mit Endpunkt zur Anzeige der programmatisch abgefragten
 * Service-Discovery-Ergebnisse.  
 */
@RestController
@RequestMapping( "/api/v1" )
public class InstanzenFinderRestController {

    private static Logger LOG = LoggerFactory.getLogger( InstanzenFinderRestController.class);
 
    /** Bean zur programmatischen Abfrage von Service-Discovery-Information von Eureka-Server. */
    @Autowired
    private DiscoveryClient _discoveryClient;
    
    
    /**
     * REST-Endpunkt liefert JSON mit von Discovery Client erhaltenen Information zum
     * Service "spam-score-api" zurück.
     * <br><br>
     * 
     * URL für lokalen Aufruf:
     * <pre>
     * http://localhost:8080/api/v1/instanzen
     * </pre>
     * 
     * @return Objekt mit HTTP-Status-Code "200 OK" 
     */
    @GetMapping( "/instanzen" )
    public ResponseEntity<List<ServiceInstance>> holeServiceInstanzen() {
        
        final List<String> serviceList = _discoveryClient.getServices();
        LOG.info( "Services gefunden ({}): {}", serviceList.size(), serviceList );                                       
    
        final List<ServiceInstance> instanzenListe = 
                        _discoveryClient.getInstances( "spam-score-api" );
        
        return ResponseEntity.status( OK ).body( instanzenListe );
    }
    
}
