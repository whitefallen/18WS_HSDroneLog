Übersicht über den Drohnen Controller
---
Der Drohnen Controller ist dafür zuständig alle Anfragen bezüglich der Entität Drohne.
- ``public function index() {...}`` - lädt die Übersicht Seite der Drohnen.
- ``public function show(int $drone_id) {...}`` - lädt die Detail-Übersicht einer Drohne mit der ID ``$drone_id``
- ``public function add_drone() {...}`` - lädt die Drohnen Erstellen Seite und bearbeitet das Hinzufügen einer Drohne
- ``public function delete_drone($drone_id) {...}`` - löscht eine Drohne mit der ID ``$drone_id``
- ``public function edit_drone($drone_id) {...}`` - kümmert sich um das bearbeiten der Drohne mit der Id ``$drone_id``

Modelle
---
Der Drohnen Controller braucht für seine Funktionen diese Modelle
- ``Drone_model``
- ``Akku_model``

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).