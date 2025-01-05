# Email-Checker: Eureka-Registry #

<br>

Dieser Ordner enthält die Eureka-Registry für den Email-Check.

Vorlage: [dieses Tutorial auf *baeldung.com*](https://www.baeldung.com/spring-cloud-netflix-eureka#Eureka)

<br>

**Schritte:**
* in `pom.xml`: Dependencies `spring-cloud-starter-netflix-eureka-server` und `spring-cloud-starter-parent`
* Konfigurationswerte in `application.properties`
* Annotation `@EnableEurekaServer` für Einstiegsklasse

<br>

Web-Oberfläche von Eureka: http://localhost:8761/

<br>

----

## Warnung: EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN...

<br>

Kompletter Fehlertext:

> EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. 
> RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.

<br>

Laut [diesem Issue](https://github.com/spring-cloud/spring-cloud-netflix/issues/1195#issuecomment-246288151) 
verschwindet dieser Text sobald der erste Client sich registriert hat

<br>
