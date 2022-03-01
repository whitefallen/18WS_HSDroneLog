Übersicht des Backends des Webservers
-----------

Im Backend werden alle anfragen die an den Server geschickt wird verarbeitet und die Antworten dem entsprechend aufbereitet

Alle Anfragen sind auf Controller gemappt, die jeweils eine für Entität agieren. Das ganze sieht dann so aus <br>
``index.php/controller_name/methoden_name/methoden_parameter`` 

Controller die ein gleichnamiges Datenmodell verwalten, besitzen alle CRUD-Operation

- **C** - Create/Add
- **R** - Read/Show
- **U** - Update/Edit
- **D** - Delete

Für das Backend wird die Skriptsprache [_PHP_](http://php.net/docs.php) Objektorientiert benutzt, in verbindung mit dem Framework 
[_Codeigniter_](https://www.codeigniter.com/user_guide/) und [_MySql_](https://dev.mysql.com/doc/) als Datenbank.

Alle Anfragen an den Server, sind für das Web-Frontend und für die Mobile App geleich, der einzige Unterschied ist das,
beim Web-Frontend HTML-Seiten geladen und gerendert werden, und bei der App nur die Notwendigen Daten zurück geschickt werden, 
damit diese dort eingezeigt werden können.

Das Backend kann über ``Config`` PHP-Datein beliebig eingestellt werden.
Die wichtigsten dabei sind:
- ``autoload.php`` - in dieser wird definiert welche Datein beim aufruf einer Seite/Datei automatisch geladen werden soll
- ``database.php`` - in dieser wird die Datenbank eingestellt, Datenbank-Zugangsdaten etc
- ``routes.php`` - in dieser Können Routen verändert werden oder neue hinzugefügt werden.
- ``config.php`` - in dieser wird das Codeigniter Framework genauer eingestellt.

Für das Projekt wichtige Variablen, wie
- Datenbank-Benutzer
- Datenbank-Passwort
- Datenbank-Addresse
- Server-Addresse
- API-Schlüssel

Sind in einer eignen Datei auf dem Server abgelegt, damit diese nicht mit in der Versionverwaltung landen, zu finden sind diese in der .env Datei.


Controllers
-----------
- [Webserver User Controller](Webserver_controller_user)
- [Webserver Index Controller](Webserver_controller_index)
- [Webserver Dashboard Controller](Webserver_controller_dashboard)
- [Webserver Drohne Controller](Webserver_controller_drone)
- [Webserver Flug Controller](Webserver_controller_flight)
- [Webserver Akku Controller](Webserver_controller_akku)
- [Webserver Checkliste Controller](Webserver_controller_checklist)
- [Webserver Checklistenelement Controller](Webserver_controller_checklistelement)
- [Webserver Nachrichten Controller](Webserver_controller_message)

Hilfsdatein
-----------
``Methoden`` die alle ``Controller`` verwenden wurden an eine Zentrale Stelle ausgelagert, an sogenannte ```"Helper"```.
Diese werden per Autoload hinzugeladen (zu finden in ``/config/autoload.php``).

- [Webserver Antwort Helfer](Webserver_response_helper)
- [Webserver Upload Helfer](Webserver_upload_helper)

Feature
--
- [Webserver Email Feature](Webserver_Email_Feature)

Datenbank
---
- [Webserver Datenbank](Webserver_database)

<-- Zurück zu [Webserver Übersicht](Architektur_Webserver) 