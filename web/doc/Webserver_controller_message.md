Übersicht über den Nachrichten Controller
---
Der Nachrichten Controller ist dafür da, um das System mit Vorgefertigten Meldungen zu beliefern, die über das Web-Frontend oder die App von Admin geändert werden können.

- ``public function index() {...}`` - lädt die Übersicht Seite im Frontend
- ``public function show(string $message_key) {...}`` - zeig die Nachricht zu einem Nachrichten-Schlüssel ``$message_key`` an
- ``public function edit_message($message_key) {...}`` - behandelt das Bearbeiten einer Nachricht mit dem Schlüssel ``$message_key``

Modelle
----
Der Nachrichten Controller braucht für seine Funktionen diese Models
- ``Nachrichten_model``

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).