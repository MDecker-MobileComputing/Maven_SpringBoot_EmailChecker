"use strict";

/** Referenz auf <input>-Element für Email-Adresse. */
let inputEmailAdresse = null;


/**
 * Initialisierungen ausführen, sobald das Dokument geladen wurde.
 */
document.addEventListener( "DOMContentLoaded", function() {

    inputEmailAdresse = document.getElementById( "inputEmailAdresse" );
    if ( inputEmailAdresse === null ) {

        console.error( "Eingabefeld \"inputEmailAdresse\" wurde nicht gefunden." );
        return;
    }
});


/**
 * Event-Handler für Button "Email-Adresse überprüfen".
 */
function checkEmailAdresse() {

    const emailAdresse = inputEmailAdresse.value.trim();
    if ( emailAdresse.length == 0 ) {

        alert( "Bitte geben Sie eine Email-Adresse ein." );
        return;
    }

    const pfad = "api/v1/emailCheck?email_adresse=" + emailAdresse;
    fetch( pfad )
        .then( antwort => {

            if ( !antwort.ok ) {

                throw new Error( "Netzwerkfehler: " + antwort.statusText );
            }

            return antwort.json();
        })
        .then( daten => {

            const { emailAdresse, zusammenfassung, syntaxKorrekt, spamScore } = daten;

            console.log( `Ergebnis von Check für Email-Adresse "${emailAdresse}": ${zusammenfassung}` );

            if ( syntaxKorrekt == false ) {

                alert( `Email-Adresse "${emailAdresse}" ist syntaktisch nicht korrekt.` );
                return;
            }

            if ( spamScore >= 0 ) {

                alert( `Email-Adresse "${emailAdresse}" ist syntaktisch korrekt und hat einen Spam-Score von ${spamScore}.` );

            } else {

                alert( `Email-Adresse "${emailAdresse}" ist syntaktisch korrekt, aber Spam-Score konnte nicht ermittelt werden.` );
            }
        })
        .catch( fehler => {

            alert( fehler );
        });    
}


/**
 * Event-Handler für Button "Zurücksetzen".
 */
function resetEingabefeld() {

    inputEmailAdresse.value = "";
}