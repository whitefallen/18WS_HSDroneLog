Übersicht des Flug Controllers
---
Der Flug Controller ist dafür zuständig alle Anfragen bezüglich der Entität Flug.
- ``public function index() {...}`` - lädt die Übersicht Seite der Flüge.
- ``public function show(int $flight_id) {...}`` - lädt die Detail-Übersicht eines Fluges mit der ID ``$flight_id``
- ``public function add_flight() {...}`` - lädt die Flug Erstellen Seite und bearbeitet das Hinzufügen eines Fluges
- ``public function delete_flight($flight_id) {...}`` - löscht einen Flug mit der ID ``$flight_id``
- ``public function edit_flight($flight_id) {...}`` - kümmert sich um das bearbeiten des Flugs mit der Id ``$flight_id``
- ``public function locationCodes(string $einsatzort_name) {...}`` - wird beim editieren/anlegen eines flugs aufgerufen um den Einsatzort in Geo-Datem( Breiten- und Längengrad umzuwandeln )
- ``public function requestAvailableDrones() {...}`` - gibt alle zurzeit verfügbaren Drohnen zurück, basierend auf einem zeitraum der per POST-Parameter übergeben werdem

Modelle
---
Der Flug Controller braucht für seine Funktionen diese Modelle
- ``Fluege_model``
- ``Drone_model``
- ``User_model``
- ``Checkliste_name_model``
- ``Checkliste_zuordnen_model``

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).