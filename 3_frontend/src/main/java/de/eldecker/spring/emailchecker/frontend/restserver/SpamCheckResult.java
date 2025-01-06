package de.eldecker.spring.emailchecker.frontend.restserver;


/**
 * Ein Objekt dieser Record-Klasse wird nach JSON serialisiert und
 * an das HTML-Frontend als HTTP-Response geschickt.
 * 
 * @param emailAdresse Die Email-Adresse, die überprüft wurde
 * 
 * @param zusammenfassung Eine Zusammenfassung des Ergebnis, kann dem Nutzer 
 *                        angezeigt werden, z.B.
 *                        "Email-Adresse ist syntaktisch nicht korrekt".
 *                        
 * @param syntaxKorrekt {@code true} gdw. keine syntaktischen Fehler wie z.B.
 *                      fehlendes "@"-Zeichen gefunden wurden. 
 *                   
 * @param spamScore Spam-Score von 0 bis einschl. 3 für {@code syntaxKorrekt=true}.
 *                  Bei {@code syntaxKorrekt=false} wird die Email-Adresse nicht
 *                  zur Spam-Score-REST-API geschickt, der Wert ist dann {@code -1}. 
 *                  Wenn die Spam-Score-REST-API keinen Wert zurückliefert, dann
 *                  ist der Wert {@code -2}.
 */
public record SpamCheckResult( 
                                String  emailAdresse   ,
                                String  zusammenfassung,
                                boolean syntaxKorrekt  , 
                                int     spamScore 
                             ) {                            
}
