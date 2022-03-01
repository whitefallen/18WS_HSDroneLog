Übersicht über die Upload-Helper Klasse
---
Die Upload-Hilfklasse biete für alle Controller die Hilfsfunktion einkommende Uploads zuverabeiten.

Um ein Upload von der Mobilen App zum Webserver zu bewerkstelligen, muss mithilfe von PHP den 
PHP-Eingabestrom (``php://input``) gelesen werden und in das gewollte Dateinformat geschrieben werden.

Dafür wird einmal der gewünschte Dateiname übergeben und ein Punkt, der besagt ab wann der inhalt des Eingabestroms zu der Datei gehört.

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).