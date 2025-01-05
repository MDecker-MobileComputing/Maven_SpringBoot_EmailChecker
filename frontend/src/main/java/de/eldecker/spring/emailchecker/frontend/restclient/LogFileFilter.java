package de.eldecker.spring.emailchecker.frontend.restclient;

import static ch.qos.logback.core.spi.FilterReply.ACCEPT;
import static ch.qos.logback.core.spi.FilterReply.DENY;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;


/**
 * Filter für Logging: Nur Zeilen durchlassen, in denen die vom Load Balancer
 * ausgewählte Service-Instanz genannt wird.
 * <br><br>
 * 
 * Diese Klasse wird in der Datei {@code logback.xml} bei der Definition
 * des Appenders {@code LOAD_BALANCER_DATEI_LOG} im Element {@code <filter>} 
 * verwendet.
 */
public class LogFileFilter extends Filter<ILoggingEvent> {

    /**
     * Entscheidet, ob eine Log-Nachricht durchgelassen wird. Eine Log-Nachricht
     * wird durchgelassen (in Log-Datei geschrieben), wenn sie den folgenden
     * Text enthält: "Selected service instance: "
     * 
     * @return {@code ACCEPT}, wenn die Log-Nachricht durchgelassen werden soll,
     *        ansonsten {@code DENY}. 
     */
    @Override
    public FilterReply decide( ILoggingEvent event ) {
   
        final String logNachricht = event.getMessage();
        if ( logNachricht.contains( "Selected service instance: " ) ) {
            
            return ACCEPT;
        }
        return DENY;
    }

}