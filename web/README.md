# 18WS_HSDroneLog

"SneakyDroneLog"

Config Einträge die zu setzen sind
--
* server_backend/application/config/config.php 
    * $config['base_url'] = 'SERVER_URL';
* server_backend/application/config/database.php
    * $db['default'] - dort den hostname, benutzername, password, datenbanknamen setzen
 
 
 Datenbank Nameskonvention
 --
 - Deutsche Tabllen und Spalten-namen
 - Tabellen namen am Anfang Groß
 - Spaltennamen klein und Wörter mit "_" verbinden 
 
 Passwort verschlüsseln
 --
 Das Passwort wird beim übermitteln der Form mit der PHP eigenen `password_hash` Funktion 
 gehashed und in die Datenbank gespeichert. Beim Login in das System wird mittels `password_verify` 
 der Hash aus der Datenbank ausgelesen und mit dem aus dem Formular verifiziert,
  ist das erfolgreich ist der nutzer erst dann eingeloggt.
  
  Mit Url's in Codeigniter arbeiten
  --
  Weiterleitungen auf andere Seiten oder das definieren von Url's ist in Codeigniter ein bisschen umständlich 
  da in der Regel jeder Url über die Index.php gehen muss.
  Zum glück gibt es von Codeigniter eine Hilfsklasse womit man mit der funktion `site_url(controller/methode)` 
  automatisch die Url/Pfad bekommt der eben über diese Datei läuft.
  
  Webseiten mit PHP erstellen
  --
  PHP unterstüzt alles was HTML auch kann, nur ergänz es dies mit eigenen Funktionen und Variablen.
  Alles was dafür gebraucht wird ist zu sagen das nun ein PHP-Block folgt, dies geschieht mit dem PHP tag `<?php` , am ende muss man dies noch mit
   `?>` schließen
  z.b
  
  ```
  <html>
  <body><?php echo "Hallo welt" ?></body>
  </html>
  ```
  ist das selbe wie

  ```
  <html>
  <body> Hallo welt </body>
  <html>
  ```
 
 