Übersicht über die Hilfsdatei ``Antwort Helfer``
--

Der ``Reponse_Helper`` ist dafür da, um an einer Zentrallenstelle die Antworten
vom Server zuverabeiten, so das die Daten an das Web-Frontend und an die Mobile-App 
korrekt gesendet werden.

In ``function prepareResponse(array $_data = array(), string $_route = "", string $_view = "", bool $_success = false, array $_viewdata = array(), string $_message = "", string $_session = "", string $_headercode = "200")
         {...}``
wird geschaut ob die Anfrage vom Web-Frontend oder von der App kommt, und dem entsprechent die passende Antwort geschickt.
Das Web-Frontend bekommt die Daten in der dafür ausgelegten View geladen und die Mobile-App bekommt die ganzen Daten als
JSON zurück geschickt der immer den selben aufbau hat.
```JSON
{
  "success": true | false,
  "data": [
    {
      "attribut1": "value1",
      "attribut2": "value2",
      "attribut3": "value3",
      "attribut4": "value4", 
    }
  ],
  "message": "MessageString",
  "session": "SessionString" 
}
```
Die Funktion `` function getSession() {...}`` sorgt dafür das immer auf die richtige Session zugegriffen wird.
Das Problem bestand da das Web, automatisch die Session immer austauscht aber die Java-App nicht, also musste ein Weg gefunden werden
das die App ebenfalls auf eine erzeugte Session immer zugreifen kann
Dafür wird dann immer bei jeder Anfrage die ``SESSION_ID`` von der App mitgeschickt, und anhand dieser, die Daten der Sitzung geladen.


<-- Zurück zu [Webserver Backend Übersicht](Webserver Backend).