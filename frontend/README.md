## Email-Checker: Frontend ##

<br>

Dieser Unterordner enthält ein Maven-Projekt mit einer auf *Spring Boot* basierenden Anwendung,
die einen Email-Checker darstellt. Auf einer HTML-Seite kann eine Email-Adresse eingegeben
werden, die dann vom Backend auf syntaktische Fehler überprüft wird. Wenn dabei keine Fehler
gefunden werden, dann wird noch über *client-seitiges Load Balancing* die 
[REST-API für die Abfrage des Spam-Scores](../spamscore/) aufgerufen.
Die aktuell zur Verfügung stehenden Instanzen dieser REST-API werden vom
[Eureka-Server (Service Registry)](../registry/) abgefragt.

<br>