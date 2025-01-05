# Email-Checker: REST-API für Spam-Score #

<br>

Dieser Unterordner enthält ein auf *Spring Boot* basierendes Maven-Projekt
mit einer REST-API, die einen REST-Endpunkt für die Abfrage eines
simulierten Spam-Scores für Email-Adressen bietet. Eine Instanz dieser Anwendung 
registriert sich nach dem Hochfahren beim 
[Eureka-Server (Service Registry)](../registry/), 
und  kann dann von der Frontend-Anwendung über *client-seitiges Load Balancing* 
angesprochen werden.

<br>