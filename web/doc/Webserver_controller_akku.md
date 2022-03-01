Übersicht des Akku Controllers
---
Der Akku Controller ist dafür zuständig alle Anfragen bezüglich der Entität Akku.
- ``public function index() {...}`` - lädt die Übersicht Seite der Akkus.
- ``public function show(int $akku_id) {...}`` - lädt die Detail-Übersicht eines Akkus mit der ID ``$akku_id``
- ``public function add_akku() {...}`` - lädt die Akku Erstellen Seite und bearbeitet das Hinzufügen eines Akkus
- ``public function delete_akku($akku_id) {...}`` - löscht einen Akku mit der ID ``$akku_id``
- ``public function edit_akku($akku_id) {...}`` - kümmert sich um das bearbeiten des Akkus mit der Id ``$akku_id``

Modelle
---
Der Akku Controller braucht für seine Funktionen diese Modelle
- ``Akku_model``
- ``Drone_model``
- ``Akku_zuweisen_model``

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).