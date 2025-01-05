package de.eldecker.spring.emailchecker.frontend.restclient;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * Diese Klasse ist mit {@code Configuration} annotiert und enthält
 * deshalb Bean-Erzeuger-Methoden (welche selbst wieder mit
 * {@code Bean} annotiert sind).
 */
@Configuration
public class LoadBalancerKonfiguration {

    
    /**
     * Objekt für REST-Abfrage (REST-Client), konfiguriert für client-seitiges Load Balancing;
     * siehe Annotation {@code LoadBalanced}.
     * <br><br>
     *
     * Offizielle Doku zu {@code RestTemplate} mit dem Spring Load Balancer:
     * <a href="https://bit.ly/3Pmo2UV">siehe hier</a>
     *
     * @return REST-Client-Objekt
     */    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplateMitLoadBalancing() {

        return new RestTemplate();
    }
    
}

