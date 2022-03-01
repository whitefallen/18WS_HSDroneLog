-- phpMyAdmin SQL Dump
-- version 4.0.10deb1ubuntu0.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 25. Mrz 2019 um 23:25
-- Server Version: 5.5.62-0ubuntu0.14.04.1
-- PHP-Version: 5.5.9-1ubuntu4.26

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `dronelog`
--

TRUNCATE TABLE `Akku`;
--
-- Daten für Tabelle `Akku`
--

INSERT INTO `Akku` (`akku_id`, `bezeichnung`, `anzahl`) VALUES
(1, 'Mavic Pro Akku', 4),
(2, 'Spark Akku', 2),
(3, 'Phantom 3 Akku', 1),
(4, 'M600', 2),
(5, 'Zenmuse X4S', 5),
(6, 'Zenmuse X7', 3),
(7, 'Xiaomi Akku', 2),
(8, 'Chang Xi', 2),
(9, 'Motorola NiMH', 1),
(10, 'Tianqu Akku', 13),
(11, 'Fytoo 2 Turbo', 2),
(12, 'Blade BLH8619 ', 2),
(13, 'Fat Shark Li-on Akku', 2),
(14, 'Eachine E58', 2),
(15, 'DH800', 2),
(16, 'Potensic Akku', 1),
(17, 'Top Race Akku', 2),
(18, 'Ryce Tello Akku', 1),
(19, 'Electron 2555', 10),
(20, 'Bionic Bird Akku', 2),
(21, 'Express Akku', 5),
(22, 'Sjedon 500', 1);

TRUNCATE TABLE `Akkus_zuordnen`;
--
-- Daten für Tabelle `Akkus_zuordnen`
--

INSERT INTO `Akkus_zuordnen` (`akku_id`, `drohne_id`) VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 5),
(7, 3),
(8, 2),
(9, 1),
(10, 2),
(11, 3),
(12, 3),
(13, 2),
(14, 4),
(15, 4),
(16, 1),
(17, 3),
(18, 3),
(19, 1),
(19, 5),
(20, 4),
(21, 3),
(22, 4);

TRUNCATE TABLE `Checklisten_elemente`;
--
-- Daten für Tabelle `Checklisten_elemente`
--

INSERT INTO `Checklisten_elemente` (`element_id`, `bezeichnung`, `erklaerung`) VALUES
(1, 'Gelände absichern', 'Gelände absichern'),
(2, 'Flug Erlaubnis einholen', 'Flug Erlaubnis einholen'),
(3, 'Fotos machen', 'Fotos machen'),
(4, 'Akku stand überprüfen', 'Akku stand überprüfen'),
(5, 'Motoren überprüfen', 'Motoren überprüfen'),
(6, 'Not-Akkus mitnehmen', 'Not-Akkus mitnehmen'),
(7, 'Video aufnehmen', 'Video aufnehmen');

TRUNCATE TABLE `Checkliste_name`;
--
-- Daten für Tabelle `Checkliste_name`
--

INSERT INTO `Checkliste_name` (`checkliste_name_id`, `bezeichnung`, `erklaerung`) VALUES
(1, 'Erster Drohnen Flug', 'Erster Drohnen Flug'),
(2, 'Fotosession', 'Fotosession'),
(3, 'Testflug', 'Testflug'),
(4, 'Kurzer Rundflug', 'Kurzer Rundflug');

TRUNCATE TABLE `Checkliste_werte`;

TRUNCATE TABLE `Checkliste_zuordnen`;
--
-- Daten für Tabelle `Checkliste_zuordnen`
--

INSERT INTO `Checkliste_zuordnen` (`checkliste_name_id`, `element_id`) VALUES
(1, 1),
(2, 1),
(3, 1),
(1, 2),
(2, 2),
(3, 2),
(1, 3),
(2, 3),
(4, 3),
(1, 4),
(2, 4),
(3, 4),
(4, 4),
(1, 5),
(2, 5),
(3, 5),
(4, 5),
(1, 6),
(2, 6),
(3, 7),
(4, 7);

TRUNCATE TABLE `Drohne`;
--
-- Daten für Tabelle `Drohne`
--

INSERT INTO `Drohne` (`drohne_id`, `drohnen_modell`, `fluggewicht_in_gramm`, `flugzeit_in_min`, `diagonale_groesse_in_mm`, `maximale_flughoehe_in_m`, `hoechstgeschwindigkeit_in_kmh`, `bild`) VALUES
(1, 'Mavic Pro', 1000, 100, 1000, 1000, 100, '/uploads/GUEST_80f4959e-bfb9-4c56-96d8-1632882321e8.jpg'),
(2, 'Spark', 20, 200, 111, 120, 111, '/uploads/DJiSpark45436.jpg'),
(3, 'Phantom 3', 120, 90, 100, 100, 100, '/uploads/drone-inline1.jpg'),
(4, 'M600', 1233, 122, 511, 312, 128, '/uploads/dji-m600-640x360.jpg'),
(5, 'Inspire 2', 3440, 27, 605, 100, 94, '/uploads/inspire22.jpg');

TRUNCATE TABLE `Fluege`;
--
-- Daten für Tabelle `Fluege`
--

INSERT INTO `Fluege` (`flug_id`, `pilot_id`, `drohne_id`, `checkliste_name_id`, `flugbezeichnung`, `flugdatum`, `einsatzort_name`, `breitengrad`, `laengengrad`, `einsatzbeginn`, `einsatzende`, `flugdauer`, `start_und_landungen`, `besondere_vorkommnisse`, `flug_protokoll`) VALUES
(1, 2, 4, 2, 'HSD Fotoarbeiten', '2019-05-01', 'Hochschule Düsseldorf', 51.215629577637, 6.7760400772095, '11:09:00', '14:09:00', 10800, NULL, '', NULL),
(2, 1, 1, 2, 'Planet-Erde Doku filmen', '2019-07-12', 'Düsseldorf Rheintreppe', 51.215629577637, 6.7760400772095, '09:00:00', '14:00:00', 18000, NULL, '', NULL),
(3, 3, 4, 4, 'Fassade inspizieren ', '2020-07-17', 'Hochschule Düsseldorf', 51.215629577637, 6.7760400772095, '12:11:00', '15:11:00', 10800, NULL, '', NULL),
(4, 1, 4, 4, 'Düsseldorf Bilk erkunden', '2019-03-25', 'Friedrichstraße 129, 40217 Düsseldorf', 51.20875, 6.77577, '10:50:00', '20:50:00', 36000, NULL, 'Windig', NULL),
(5, 1, 5, 4, 'Köln-Kalk erkunden', '2019-03-07', 'Kalker Hauptstraße 55, 51103 Köln', 50.93765, 6.99805, '11:30:00', '15:50:00', 15600, NULL, 'Unwetterwarnung während des Fluges', NULL),
(6, 2, 2, 4, 'Flugkurs Medientechnik', '2019-03-20', 'Hochschule Düsseldorf', 51.215629577637, 6.7760400772095, '11:16:00', '15:29:00', 15180, NULL, '', NULL),
(7, 1, 2, 2, 'Frankreich Studienreise 2019', '2019-03-04', 'Aiguille du Midi, Frankreich', 45.91988, 6.86821, '10:25:00', '10:55:00', 1800, NULL, 'Keine', NULL);

TRUNCATE TABLE `Pilot`;
--
-- Daten für Tabelle `Pilot`
--

INSERT INTO `Pilot` (`pilot_id`, `profilbild`, `email_adresse`, `passwort`, `vorname`, `nachname`, `studiengang`, `rolle`, `loeschberechtigung`, `letzter_login`, `aktivitaet`) VALUES
(1, '/uploads/2502728-bewerbungsfotos-in-berlin1.jpg', 'test@test.de', '$2y$10$QtOia2PFJPIi9QvfULg6hOJu0Kw2XsDbWhNn8doFamXxBAGyImaN2', 'Max', 'Mustermann', 'bmt', b'1', b'1', '2019-03-25 21:23:09', b'1'),
(2, '/uploads/IMG_20181221_214202_559.jpg', 'yannikbrgs@gmail.com', '$2y$10$aGRhFN5UyA60d6PmPZUPWe0ZGnsmBX6Boi4C4MRwD8hWp88G5Unqe', 'Yannik', 'Borges', 'BMI', b'1', b'0', '2019-03-25 22:07:36', b'1'),
(3, '/uploads/222Fotolia_100419060_M-800x480.jpg', 'admin@dronelog.de', '$2y$10$hOL8C2hmzZidTXMKLD6Sle9eHKZc0OS1F8juUbpMnrRMp7uqRSpA2', 'Drohnenlogbuch', 'Admin', 'BMI', b'1', b'0', '2019-03-25 21:46:24', b'1'),
(4, '/assets/images/muster_pb.png', 'thomas.kammann@study.hs-duesseldorf.de', '$2y$10$UlUe5KVipXMhUnNTqVhzw.pJ8awYP6JjddHJeGuzysFsV1J2Lj3u2', 'Thomas', 'Kammann', 'BMI', b'1', b'0', '2019-03-25 21:23:34', b'1'),
(5, '/uploads/csm_Landschaftspark_Duisburg_Nord__c__Achim_Meurer_1920x900_1c584afb38.jpg', 'henrik.kother@study.hs-duesseldorf.de', '$2y$10$71UCuQs1nG70OUxXVRCvEesiWySi/kssijD7by7nLa7yITj6yTwvW', 'Henrik', 'Kother', 'Bmi', b'0', b'0', '2019-03-25 22:06:02', b'1');

SET FOREIGN_KEY_CHECKS=1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
