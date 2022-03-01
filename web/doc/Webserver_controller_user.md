Übersicht des User Controller
----
Der User Controller verwaltet alle Anfragen bezüglich der User

- ``public function index() {..}`` - lädt die User-Übersicht Seite. 
- ``public function show($user_id = 0) {...}`` - lädt die Detail Übersicht des Nutzer mit der ID ``$user_id``.
- ``public function edit_user($pilot_id) {...}`` - behandelt das Editieren der Daten des Nutzers mit der ID ``$pilot_id``.
- ``public function add_user() {...}`` - lädt die User-Hinzufügen Seite und behandelt die einkommenen Daten.
- ``public function logout() {...}`` - zerstört die derzeitige Sitzung
- ``public function delete_user($user_id) {...}`` Löscht den Nutzer mit der ID ``$user_id`` aus dem System

Modelle
----
Der User Controller benötigt diese Models für seine Funktionen
- ``User_model``

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).