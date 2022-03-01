Übersicht über den Checklisten Elemente Controller
---
Der Checklisten Elemente Controller ist dafür zuständig alle Anfragen bezüglich der Entität Checklisten Elemente.
- ``public function index() {...}`` - lädt die Übersicht Seite der Checklisten Elemente.
- ``public function show(int $checklist_element_id) {...}`` - lädt die Detail-Übersicht eines Checklisten Elements mit der ID ``$checklist_element_id``
- ``public function add_element() {...}`` - lädt die Checklisten Elementen Erstellen Seite und bearbeitet das Hinzufügen eines Checklisten Elementes
- ``public function delete_element($checklist_element_id) {...}`` - löscht ein Checklisten Element mit der ID ``$checklist_element_id``
- ``public function edit_element($checklist_element_id) {...}`` - kümmert sich um das bearbeiten des Checklisten Elementes mit der Id ``$checklist_element_id``

Modelle
---
Der Checklisten Elemente Controller braucht für seine Funktionen diese Modelle
- ``Checklisten_elemente_model``
- ``Checkliste_name_model``
- ``Checkliste_zuordnen_model``

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).