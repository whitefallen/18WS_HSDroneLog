Übersicht über den Checklist Controller
---

Der Checklisten Controller ist dafür zuständig alle Anfragen bezüglich der Entität Checkliste.
- ``public function index() {...}`` - lädt die Übersicht Seite der Checklisten.
- ``public function show(int $checklist_id) {...}`` - lädt die Detail-Übersicht einer Checkliste mit der ID ``$checklist_id``
- ``public function add_checklist() {...}`` - lädt die Checklisten Erstellen Seite und bearbeitet das Hinzufügen eines Checkliste
- ``public function delete_checklist($checklist_id) {...}`` - löscht eine Checklsite mit der ID ``$checklist_id``
- ``public function edit_checklist($checklist_id) {...}`` - kümmert sich um das bearbeiten einer Checkliste mit der Id ``$checklist_id``

Modelle
----
Der Checklisten Controller braucht für seine Funktionen diese Models
- ``Checkliste_name_model``
- ``Checklisten_elemente_model``
- ``Checkliste_zuordnen_model``
- ``Checkliste_werte_model``

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).