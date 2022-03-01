Übersicht über den Dashboard Controller
---
Der Dashboard Controller ist dafür da, um das Dashboard des Web-Frontend und der Mobile-App mit Daten zuversorgen

In der ``public function index() {...}`` Methode werden alle daten die für das Web-Frontend benötigt werden zusammengefasst und weitergegben.

Die Mobile-App bekommt die Daten über die einzelnen Funktionen die in der ``public function index() {...}`` aufgerufen werden
- ``public function most_used_drones() {...}`` - für die Daten der Meist genutzen Drohnen
- ``public function amount_drones() {...}`` - für die Anzahl der bestehenden Drohnen
- ``public function longest_flight() {...}`` - für die Daten des Längsten Flug im System
- ``public function flight_data() {...}`` - um alle Flüge im System zubekommen
- ``public function flight_data_before() {...}`` - um alle Flüge vor dem Heutigen Tag zubekommen
- ``public function flight_data_today() {...}`` - um alle Flüge zum heutigen Tag zubekommen
- ``public function flight_data_after() {...}`` - um alle Flüge nach dem heutigen Tag zubekommen
- ``public function pilot_of_the_week() {...}`` - um dem Piloten der Woche zubekommen
- ``public function flights_without_drones() {...}`` um Flüge ohne eine Drohne zubekommen

Modelle
----
Der Dashboard Controller braucht für seine Funktionen diese Models
- ``User_model``
- ``Drone_model``
- ``Flight_model``

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).