Übersicht des Index Controller
-----
Dieser Controller ist dafür da um die Login und Registrierungs Anfragen abzuarbeiten

Als erstes ``public function login_user() {...}`` dort wird über das ``User_model`` geprüft ob es zuerst einen Benutzer mit der einzuloggenen Email-Addresse gibt.
Anschließend wird dann die Kombination Email-Addresse und Passwort überprüft, zu jedem Schritt bekommt der Anwender eine passende Mitteilung, wie etwa
``Dieser Benutzer Existiert nicht.``, oder ``Email-Addresse und/oder Passwort ist falsch.``

Sollte noch kein Nutzerkonto vorhanden sein, gibt es die Möglichkeit sich zu Registrieren, dies Ruft dann die Methode ``public function register_user() {...}`` auf
welche dafür sorgt, das bei einer erfolgreichen Registrierung in der Datenbank die Daten eingespeichert werden, damit der Anwender sich danach mit diesen in das System einloggen kann.
Wie beim Login, wird auch hier immer gemeldet ob eine ``Registrierung Erolgreich.`` oder eine ``Registrierung Fehlgeschlagen.`` ist.

Ausserdem besteht die Möglichkeit das Passwort des Benutzters zurückzusetzen ``public function reset_passwort(){...}`` , dies generiert ein zufälliges 6-Stelliges passwort und schickt es
dem Benutzer per E-mail zu, sollte der E-mail Verkehr aufgrund fehlerhafte SMTP-Einstellungen nicht gelingen, wird das neue passwort auch nicht gesetzt.

Modelle
----
Der Index Controller benötigt diese Models
- ``User_model``

<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).