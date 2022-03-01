Webserver Installtion Prozedur
---

Sollten noch keine Systemvariablen gesetzt werden sprich, das System weiß nicht,
welche URL als ```base_url``` , oder wenn kein Datenbanknutzer eingerichtet ist,
erscheint eine Setup-Prozedur.

In dieser hat der Anwender die Möglichkeit das Projekt einzurichten, 
damit dieses auf eigenem Webserver fehlerfrei laufen kann.

Zuerst erscheint eine Maske wo die Systemvariablen gesetzt werden, 
zufinden ist diese Konfiguration in der ```.env``` Datei.

| Variable | Beschreibung |
|----------|--------------|
| DB_HOST | ist die URL, welche zur Datenbank gehört |
| DB_USER | ist der Benutzername für den Datenbanknutzer |
| DB_PASS | ist das Passwort für den dazu gehörigen Benutzername |
| ENVIROMENT | ist die Umgebung in der das System läuft (dev/prod) |
| BASE_URL | ist die URL, die besagt wo sich das System befindet |
| API_KEY | ist die Variable für den BING_API Key, genutzt um Orte zu geocoden |
| UPLOAD_SIZE | definiert welche maximale größe von Datein per Upload erlaubt sind (in Kbyte) |
| UPLOAD_PATH | ist der Pfad, wo die hochgeladenen Datein anbgelegt werden |
| IMG_WIDTH | legt fest wie breit ein Bild maximal ist |
| IMG_HEIGHT | legt fest wie hoch ein Bild maximal ist |
| IMG_TYPE | legt fest welche Bild Datein erlaubt sind (png/jpg/gif...) |

Anschließend hat der Anwender die Möglichkeit einen Nutzer anzulegen um sich in das System einzuloggen und
schon mal das Datenbank-Schema generieren zu lassen.

hat das alles geklappt, wird auf die Login-Maske des Projektes weitergeleitet
und man kann das Projekt benutzen.

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).