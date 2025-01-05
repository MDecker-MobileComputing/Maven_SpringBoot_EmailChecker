package de.eldecker.spring.emailchecker.frontend.restclient;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;


@Component
public class InstanzenFinder implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger( InstanzenFinder.class );
    
    @Autowired
    private DiscoveryClient _discoveryClient;
    
    
    public void run( String... args ) throws Exception {
    
        final List<String> serviceList = _discoveryClient.getServices();
        LOG.info( "Services gefunden ({}): {}", 
                  serviceList.size(), 
                  serviceList ); 
        
        
        final List<ServiceInstance> instanzenListe = _discoveryClient.getInstances( "spam-score-api" );                
        LOG.info( "Spam-Score Instanzen gefunden ({}): {}", 
                  instanzenListe.size(), 
                  instanzenListe ); 
    }

}
