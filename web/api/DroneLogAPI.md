# DroneLog
# Überblick über die DroneLog API

Alle anfragen die von der Mobile App an die API gehen werden hier beschrieben.

# Group flight

## Flight Index [/flight/index]

### /flight [POST]
Gibt alle Flüge aus der Datenbank zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + offset (string, required)

        Offsets the Result Data by x Amount

        + Sample: 0

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Flight Show By Id [/flight/show/{{$id}}]

### /flight/show/id [POST]
Gibt Daten zu genau einem Flug zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Flight Add Flight [/flight/add_flight]

### /flight/add_flight [POST]
Route zum hinzufügen eines Fluges
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + pilot_id (string, required)

        + Sample: 22
    + drohne_id (string, required)

        + Sample: 2
    + flugbezeichnung (string, required)

        + Sample: irgendeintext
    + einsatzort_name (string, required)

        + Sample: PostmanAPI
    + flugdatum (string, required)

        + Sample: 2019-01-30
    + einsatzende (string, required)

        + Sample: 10:50
    + einsatzbeginn (string, required)

        + Sample: 10:00
    + checkliste_name_id (string, required)

        + Sample: 1
    + besondere_vorkommnisse (string, required)

        + Sample: irgendeintext

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Flight Delete Flight By Id [/flight/delete_flight/{{$id}}]

### /flight/delete_flight/id [POST]
Löscht einen Flug aus der Datenbank anhand einer ID
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Flight Edit Flight By Id [/flight/edit_flight/{{$id}}]

### /flight/edit_flight/id [POST]
Route zum editieren eines Fluges
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + pilot_id (string, optional)

        + Sample: 1
    + drohne_id (string, optional)

        + Sample: 1
    + flugbezeichnung (string, optional)

        + Sample: Wölfe finden
    + einsatzort_name (string, optional)

        + Sample: PostmanAPI
    + flugdatum (string, optional)

        + Sample: 20-12-1995
    + einsatzbeginn (string, optional)

        + Sample: 08:20
    + einsatzende (string, optional)

        + Sample: 13:50
    + checkliste_name_id (string, optional)

        + Sample: 2
    + besondere_vorkommnisse (string, optional)

        + Sample: Wölfe gefunden

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Flight RequestAvailableDrones [/flight/requestAvailableDrones]

### /flight/requestAvailableDrones [POST]
Route zum ermitteln aller verfügbaren Drohnen zu einem Flugzeitpunkt
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + startzeit (string, required)

        + Sample: 10:10:00
    + endzeit (string, required)

        + Sample: 15:10:00
    + datum (string, required)

        + Sample: 11.01.2019

+ Request (application/x-www-form-urlencoded)

+ Response 200

# Group user

## User Logout [/user/logout]

### /user/logout [POST]
Route um ein Benutzer aus dem System auszuloggen
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## User Index [/user/index]

### /user/index [POST]
Gibt die Daten fürs alle Benutzer zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## User Delete User By Id [/user/delete_user/{{$id}}]

### /user/delete_user/id [POST]
Löscht einen Benutzer anhand der ID
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## User Edit User By Id [/user/edit_user/{{$id}}]

### /user/edit_user/id [POST]
Route zum Bearbeiten eines Benutzers
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + email_adresse (string, optional)

        + Sample: test_edit@test.de
    + passwort (string, optional)

        + Sample: test_edit
    + vorname (string, optional)

        + Sample: test_Edit
    + nachname (string, optional)

        + Sample: test_Edit
    + studiengang (string, optional)

        + Sample: tes
    + rolle (string, optional)

        + Sample: 1
    + aktivitaet (string, optional)

        + Sample: 1
    + filename (string, optional)

        MOBILE ONLY

        + Sample: IMG_TO_NAME.jpg
    + profilbild (string, optional)

        + Sample: bitstrom/datei

+ Request (application/x-www-form-urlencoded)

+ Response 200

## User Add User [/user/add_user]

### /user/add_user [POST]
Route zum Anlegen eines Benutzers
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + email_adresse (string, required)

        + Sample: deleteme@deleteme.de
    + passwort (string, required)

        + Sample: postman
    + vorname (string, required)

        + Sample: Postman
    + nachname (string, required)

        + Sample: API
    + studiengang (string, required)

        + Sample: API
    + rolle (string, required)

        + Sample: 0
    + aktivitaet (string, required)

        + Sample: 0
    + filename (string, optional)

        MOBILE ONLY

        + Sample: IMG_DT_Q.jpg
    + profilbild (string, optional)

        + Sample: bitstrom/datei

+ Request (application/x-www-form-urlencoded)

+ Response 200

## User Show By Id [/user/show/{{$id}}]

### /user/show/id [POST]
Gibt die Daten für einen Benutzer zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

# Group drone

## Drone [/drone]

### /drone [POST]
Gibt alle Drohnen aus der Datenbank zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + offset (string, required)

        Offsets the Result Data by x Amount

        + Sample: 1

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Drone Show By Id [/drone/show/{{$id}}]

### /drone/show/id [POST]
Gibt Daten zu genau einer Drohne zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Drone Add Drone [/drone/add_drone]

### /drone/add_drone [POST]
Route zum hinzufügen einer Drohne
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + drohnen_modell (string, required)

        + Sample: PostmanDrohne3336
    + fluggewicht_in_gramm (string, required)

        + Sample: 50
    + flugzeit_in_min (string, required)

        + Sample: 50
    + diagonale_groesse_in_mm (string, required)

        + Sample: 50
    + maximale_flughoehe_in_m (string, required)

        + Sample: 50
    + hoechstgeschwindigkeit_in_kmh (string, required)

        + Sample: 50
    + filename (string, optional)

        MOBILE ONLY

        + Sample: IMG_to_display.jpg
    + bild (string, optional)

        + Sample: bitstrom/datei

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Drone Delete Drone By Id [/drone/delete_drone/{{$id}}]

### /drone/delete_drone/id [POST]
Löscht eine Drohne aus der Datenbank anhand ihrer ID
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Drone Edit Drone By Id [/drone/edit_drone/{{$id}}]

### /drone/edit_drone/id [POST]
Route zum editieren einer Drohne
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + drohnen_modell (string, optional)

        + Sample: PostmanDrohne3
    + fluggewicht_in_gramm (string, optional)

        + Sample: 50
    + flugzeit_in_min (string, optional)

        + Sample: 50
    + diagonale_groesse_in_mm (string, optional)

        + Sample: 50
    + maximale_flughoehe_in_m (string, optional)

        + Sample: 50
    + hoechstgeschwindigkeit_in_kmh (string, optional)

        + Sample: 50
    + filename (string, optional)

        MOBILE ONLY

        + Sample: IMG_to_display.jpg
    + bild (string, optional)

        + Sample: bitstrom/datei

+ Request (application/x-www-form-urlencoded)

+ Response 200

# Group akku

## Akku [/akku]

### /akku [POST]
Gibt alle Akkus aus der Datenbank zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + offset (string, required)

        Offsets the Result Data by x Amount

        + Sample: 1

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Akku Show By Id [/akku/show/{{$id}}]

### /akku/show/id [POST]
Gibt Daten zu genau einem Akku zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Akku Add Akku [/akku/add_akku]

### /akku/add_akku [POST]
Route zum hinzufügen eines Akkus
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + bezeichnung (string, required)

        + Sample: PostmanAkku5
    + anzahl (string, required)

        + Sample: 5
    + drohnen[] (string, required)

        + Sample: 2,3

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Akku Delete Akku By Id [/akku/delete_akku/{{$id}}]

### /akku/delete_akku/id [POST]
Löscht einen Akku aus der Datenbank anhand einer ID
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Akku Edit Akku By Id [/akku/edit_akku/{{$id}}]

### /akku/edit_akku/id [POST]
Route zum edtieren eines Akkus
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + bezeichnung (string, optional)

        + Sample: PostmanAkku
    + anzahl (string, optional)

        + Sample: 5
    + drohnen[] (string, optional)

        + Sample: 2,5

+ Request (application/x-www-form-urlencoded)

+ Response 200

# Group checklist

## Checklist [/checklist]

### /checklist [POST]
Gibt alle Checklisten aus der Datenbank zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + offset (string, required)

        Offsets the Result Data by x Amount

        + Sample: 1

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Checklist Show By Id [/checklist/show/{{$id}}]

### checklistshowid [POST]
Gibt Daten zu genau einer Checkliste zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Checklist Add Checklist [/checklist/add_checklist]

### /checklist/add_checklist [POST]
Route zum hinzufügen einer Checkliste
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + bezeichnung (string, required)

        + Sample: Neue_Checkliste
    + erklaerung (string, required)

        + Sample: Generiert aus der Postman APP
    + elements[] (string, required)

        + Sample: 4,5

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Checklist Delete Checklist By Id [/checklist/delete_checklist/{{$id}}]

### /checklist/delete_checklist/id [POST]
Löscht eine Checkliste aus der Datenbank anhand einer ID
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Checklist Edit Checklist By Id [/checklist/edit_checklist/{{$id}}]

### /checklist/edit_checklist/id [POST]
Route zum editieren einer Checkliste
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + bezeichnung (string, optional)

        + Sample: NoElement
    + erklaerung (string, optional)

        + Sample: Checkliste für die Postman App
    + elements[] (string, optional)

        + Sample: 2,3

+ Request (application/x-www-form-urlencoded)

+ Response 200

# Group checklistelements

## Checklistelement [/checklistelement]

### /checklistelement [POST]
Gibt alle Checklisten Elemente aus der Datenbank zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Checklistelement Show By Id [/checklistelement/show/{{$id}}]

### checklistelementshowid [POST]
Gibt Daten zu genau einem Checklistenelement zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Checklistelement Add Element [/checklistelement/add_element]

### /checklistelement/add_element [POST]
Route zum hinzufügen eines Checklisten Elementes
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + bezeichnung (string, required)

        + Sample: PostmanAPIListElement2
    + erklaerung (string, required)

        + Sample: ChecklistElement für die Postman Api App
    + checklists[] (string, required)

        + Sample: 2,3

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Checklistelement Delete Element By Id [/checklistelement/delete_element/{{$id}}]

### /checklistelement/delete_element/id [POST]
Löscht ein Checklisten Element aus der Datenbank anhand einer ID
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Checklistelement Edit Element By Id [/checklistelement/edit_element/{{$id}}]

### /checklistelement/edit_element/id [POST]
Route zum editieren eines Elements
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + bezeichnung (string, optional)

        + Sample: bezeichnung
    + erklaerung (string, optional)

        + Sample: text
    + checklists[] (string, optional)

        + Sample: 3,7

+ Request (application/x-www-form-urlencoded)

+ Response 200

# Group message

## Message [/message]

### /message [POST]
Gibt alle Nachrichten aus der Datenbank zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Message Show By Message Key [/message/show/{{$message_key}}]

### /message/show/messagekey [POST]
Gibt Daten zu genau einer Nachricht zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Message Edit Message By Message Key [/message/edit_message/{{$message_key}}]

### /message/edit_message/messagekey [POST]
Bearbeitet Daten von genau einer Nachricht
+ Attributes
    + session (string, required)

        + Sample: {{$session}}
    + nachricht (string, required)

        + Sample: Sie wurden erfolgreich eingeloggt.

+ Request (application/x-www-form-urlencoded)

+ Response 200

# Group dashboard

## Dashboard [/dashboard]

### /dashbaord [POST]
Gibt die Daten fürs Dashboard zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Dashboard Most Used Drones [/dashboard/most_used_drones]

### /dashbaord/most_used_drones [POST]
Gibt die meist genutzen Drohnen fürs Dashboard zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Dashboard Flights Without Drones [/dashboard/flights_without_drones]

### /dashbaord/flights_without_drones [POST]
Gibt die Flüge ohne Drohne fürs Dashboard zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Dashboard Amount Drones [/dashboard/amount_drones]

### /dashbaord/amount_drones [POST]
Gibt die Anzahl an Drohnen fürs Dashboard zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Dashboard Longest Flight [/dashboard/longest_flight]

### /dashbaord/longest_flight [POST]
Gibt den Längsten Flug fürs Dashboard zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Dashboard Flight Data [/dashboard/flight_data]

### /dashbaord/flight_data [POST]
Gibt die Flug Daten fürs Dashboard zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Dashboard Flight Data Before [/dashboard/flight_data_before]

### /dashbaord/flight_data_before [POST]
Gibt die Flug Daten vor dem Heutigen Datum fürs Dashboard zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Dashboard Flight Data Today [/dashboard/flight_data_today]

### /dashbaord/flight_data_today [POST]
Gibt die Flug Daten für den Heutigen fürs Dashboard zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Dashboard Flight Data After [/dashboard/flight_data_after]

### dashbaordflightdataafter [POST]
Gibt die Flug Daten nach dem Heutigen Tag fürs Dashboard zurück
+ Attributes
    + session (string, required)

        + Sample: {{$session}}

+ Request (application/x-www-form-urlencoded)

+ Response 200

# Group index

## Index Login [/index/login]

### /index/login [POST]
Route um ein Benutzer in das System einzuloggen
+ Attributes
    + email_adresse (string, required)

        + Sample: test@test.de
    + passwort (string, required)

        + Sample: test

+ Request (application/x-www-form-urlencoded)

+ Response 200 (application/json; charset=utf-8)

## Index Register [/index/register]

### /index/register [POST]
Route zum Anlegen eines Benutzers
+ Attributes
    + email_adresse (string, required)

        + Sample: deleteme@deletee.de
    + passwort (string, required)

        + Sample: postman
    + vorname (string, required)

        + Sample: Postman
    + nachname (string, required)

        + Sample: API
    + studiengang (string, required)

        + Sample: API
    + aktivitaet (string, required)

        + Sample: 0
    + filename (string, optional)

        + Sample: testbild.jpg
    + profilbild (string, optional)

        + Sample: bitstrom/datei

+ Request (application/x-www-form-urlencoded)

+ Response 200

## Index Password Reset [/index/password_reset]

### index/password_reset [POST]
Route zum zurücksetzen des passwortes eines Benutzers
+ Attributes
    + mail (string, required)

        + Sample: deleteme@deletee.de

+ Request (application/x-www-form-urlencoded)

+ Response 200