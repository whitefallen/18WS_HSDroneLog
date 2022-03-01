-- phpMyAdmin SQL Dump
-- version 4.0.10deb1ubuntu0.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 11. Feb 2019 um 14:07
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

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Akku`
--

CREATE TABLE IF NOT EXISTS `Akku` (
  `akku_id` int(3) NOT NULL COMMENT 'ID des Akku',
  `bezeichnung` varchar(50) NOT NULL COMMENT 'Bezeichnung des Akkus',
  `anzahl` int(3) NOT NULL COMMENT 'Anzahl der Akkus',
  PRIMARY KEY (`akku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `Akku`
--

TRUNCATE TABLE `Akku`;
--
-- Daten für Tabelle `Akku`
--

INSERT INTO `Akku` (`akku_id`, `bezeichnung`, `anzahl`) VALUES
(1, 'network test', 20),
(2, 'NeuerAkku', 100),
(3, 'networkTest', 5),
(4, 'AppAkku', 10),
(5, 'AppAkkuTets', 10),
(6, 'networkTest', 5),
(7, 'MultiAkku', 5),
(8, 'PostmanAkku5', 5),
(9, ' KarenTookTheBatteryTooNippel', 100000),
(10, 'networkTest', 5),
(11, 'networkTest', 5),
(12, 'cooler akku', 3),
(13, '123456789', 123456789),
(14, '987654321', 987654321),
(16, 'JonasAkku', 15);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Akkus_zuordnen`
--

CREATE TABLE IF NOT EXISTS `Akkus_zuordnen` (
  `akku_id` int(11) NOT NULL,
  `drohne_id` int(11) NOT NULL,
  PRIMARY KEY (`akku_id`,`drohne_id`),
  KEY `akku_id` (`akku_id`),
  KEY `drohne_id` (`drohne_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `Akkus_zuordnen`
--

TRUNCATE TABLE `Akkus_zuordnen`;
--
-- Daten für Tabelle `Akkus_zuordnen`
--

INSERT INTO `Akkus_zuordnen` (`akku_id`, `drohne_id`) VALUES
(1, 11),
(1, 13),
(2, 2),
(2, 4),
(7, 2),
(8, 2),
(8, 3),
(10, 2),
(10, 3),
(11, 2),
(11, 3),
(12, 2),
(12, 3),
(13, 3),
(13, 4),
(13, 5),
(14, 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Checklisten_elemente`
--

CREATE TABLE IF NOT EXISTS `Checklisten_elemente` (
  `element_id` int(3) NOT NULL DEFAULT '0',
  `bezeichnung` varchar(20) NOT NULL,
  `erklaerung` varchar(100) NOT NULL,
  PRIMARY KEY (`element_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `Checklisten_elemente`
--

TRUNCATE TABLE `Checklisten_elemente`;
--
-- Daten für Tabelle `Checklisten_elemente`
--

INSERT INTO `Checklisten_elemente` (`element_id`, `bezeichnung`, `erklaerung`) VALUES
(2, 'PostmanAPIList', 'Checkliste für die Postman App'),
(3, 'PostmanAPIList1', 'Checkliste für die Postman App'),
(4, 'PostmanAPIList', 'Checkliste für die Postman App'),
(5, 'PostmanAPIList', 'Checkliste für die Postman App'),
(6, 'PostmanAPIList', 'Checkliste für die Postman App'),
(7, 'PostmanAPIList', 'Checkliste für die Postman App'),
(8, 'jajaj', 'jajaj'),
(9, 'ppp', 'ppp'),
(10, 'oplo', 'oplo'),
(11, 'networkTest', 'networkTest');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Checkliste_name`
--

CREATE TABLE IF NOT EXISTS `Checkliste_name` (
  `checkliste_name_id` int(3) NOT NULL DEFAULT '0' COMMENT 'ID der Checkliste',
  `bezeichnung` varchar(20) NOT NULL,
  `erklaerung` varchar(255) NOT NULL,
  PRIMARY KEY (`checkliste_name_id`),
  UNIQUE KEY `checkliste_id_2` (`checkliste_name_id`),
  KEY `checkliste_id` (`checkliste_name_id`),
  KEY `checkliste_id_3` (`checkliste_name_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `Checkliste_name`
--

TRUNCATE TABLE `Checkliste_name`;
--
-- Daten für Tabelle `Checkliste_name`
--

INSERT INTO `Checkliste_name` (`checkliste_name_id`, `bezeichnung`, `erklaerung`) VALUES
(1, 'Neue_Checkliste', 'Generiert aus der Postman APP'),
(2, 'My Checklist2', 'Geändert von App2'),
(3, 'ewwe', 'ww'),
(4, 'Ioio', 'ioio'),
(5, 'NoElement', 'Checkliste für die Postman App'),
(6, 'Checklisten Testen', 'Checklisten werden getestet'),
(7, 'test', 'test'),
(8, 'PostmanAPIList', 'Checkliste für die Postman App'),
(9, 'AppChecklist', 'Test'),
(10, 'PostmanAPIList1', 'Checkliste für die Postman App'),
(11, 'PostmanAPIList', 'Checkliste für die Postman App'),
(12, 'PostmanAPIList11', 'Checkliste für die Postman App'),
(13, 'PostmanAPIList11', 'Checkliste für die Postman App'),
(14, 'PostmanAPIList11', 'Checkliste für die Postman App'),
(15, 'PostmanAPIList11', 'Checkliste für die Postman App'),
(16, 'llop', 'llop'),
(17, 'Eier schaukeln', 'Lieblingsbeschäftigung vom Frontend'),
(18, 'networkTest', 'network test');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Checkliste_werte`
--

CREATE TABLE IF NOT EXISTS `Checkliste_werte` (
  `flug_id` int(3) NOT NULL,
  `element_id` int(3) NOT NULL,
  `angekreuzt` bit(1) NOT NULL,
  `kommentar` varchar(255) NOT NULL,
  PRIMARY KEY (`flug_id`,`element_id`),
  KEY `Checkliste_werte_ibfk_3` (`element_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `Checkliste_werte`
--

TRUNCATE TABLE `Checkliste_werte`;
-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Checkliste_zuordnen`
--

CREATE TABLE IF NOT EXISTS `Checkliste_zuordnen` (
  `checkliste_name_id` int(3) NOT NULL,
  `element_id` int(3) NOT NULL,
  PRIMARY KEY (`checkliste_name_id`,`element_id`),
  KEY `Checkliste_zuordnen_ibfk_2` (`element_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `Checkliste_zuordnen`
--

TRUNCATE TABLE `Checkliste_zuordnen`;
--
-- Daten für Tabelle `Checkliste_zuordnen`
--

INSERT INTO `Checkliste_zuordnen` (`checkliste_name_id`, `element_id`) VALUES
(2, 2),
(5, 2),
(11, 2),
(17, 2),
(18, 2),
(2, 3),
(3, 3),
(5, 3),
(7, 3),
(1, 4),
(2, 4),
(16, 4),
(17, 4),
(1, 5),
(17, 6),
(17, 7),
(17, 10);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ci_sessions`
--

CREATE TABLE IF NOT EXISTS `ci_sessions` (
  `id` varchar(128) NOT NULL,
  `ip_address` varchar(45) NOT NULL,
  `timestamp` int(10) unsigned NOT NULL DEFAULT '0',
  `data` blob NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ci_sessions_timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `ci_sessions`
--

TRUNCATE TABLE `ci_sessions`;
--
-- Daten für Tabelle `ci_sessions`
--

INSERT INTO `ci_sessions` (`id`, `ip_address`, `timestamp`, `data`) VALUES
('03ni57ffga4s8kj51j0699bskcu9kdjp', '195.37.236.11', 1549889987, 0x5f5f63695f6c6173745f726567656e65726174657c693a313534393838393938373b),
('deai752pd7vhf6lb754tdn74u3jr31gj', '195.37.236.11', 1549890331, 0x5f5f63695f6c6173745f726567656e65726174657c693a313534393839303332363b75736572737c613a383a7b733a383a2270696c6f745f6964223b733a313a2231223b733a31333a22656d61696c5f61647265737365223b733a31323a227465737440746573742e6465223b733a373a22766f726e616d65223b733a373a2220202074657374223b733a383a226e6163686e616d65223b733a343a2274657374223b733a31303a2270726f66696c62696c64223b733a31393a222f75706c6f6164732f4a6f6e6173352e706e67223b733a31313a227374756469656e67616e67223b733a333a22626d74223b733a353a22726f6c6c65223b733a313a2231223b733a31303a2265696e67656c6f676774223b623a313b7d),
('dj53t263anl1p7v584oduhio14cn8e71', '195.37.236.11', 1549824550, 0x5f5f63695f6c6173745f726567656e65726174657c693a313534393832343535303b),
('edv2v6tetni57gej8dnm7glrp0qi8q60', '195.37.236.11', 1549835030, 0x5f5f63695f6c6173745f726567656e65726174657c693a313534393833353033303b),
('hshq3bq00hmlbuiqr743rgle94ha8cj4', '195.37.236.11', 1549835030, 0x5f5f63695f6c6173745f726567656e65726174657c693a313534393833353033303b),
('kfj3rfn7louie3lm1mb4o7c9giskp7r8', '195.37.236.11', 1549835031, 0x5f5f63695f6c6173745f726567656e65726174657c693a313534393833353033313b),
('uu5j6jc7536262l1fnn4svcridqvcj51', '195.37.236.11', 1549889874, 0x5f5f63695f6c6173745f726567656e65726174657c693a313534393838393837343b);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Drohne`
--

CREATE TABLE IF NOT EXISTS `Drohne` (
  `drohne_id` int(3) NOT NULL AUTO_INCREMENT COMMENT 'ID der Drohne',
  `drohnen_modell` varchar(50) NOT NULL DEFAULT '' COMMENT 'Modell der Drohne',
  `fluggewicht_in_gramm` int(5) NOT NULL COMMENT 'Fluggewicht der Drohne in Gramm',
  `flugzeit_in_min` int(3) NOT NULL COMMENT 'Maximale Flugzeit der Drohne',
  `diagonale_groesse_in_mm` int(4) NOT NULL COMMENT 'Diagonale Groesse (ohne Propeller) der Drohne',
  `maximale_flughoehe_in_m` int(4) NOT NULL COMMENT 'Maximale Flueghoehe ueber den Meeresspiegel in Meter',
  `hoechstgeschwindigkeit_in_kmh` int(3) NOT NULL COMMENT 'Maximale Fluggeschwindigkeit der Drohne',
  `bild` varchar(255) DEFAULT '/assets/images/drohne_quadrat.png' COMMENT 'Bild der Drohne',
  PRIMARY KEY (`drohne_id`),
  UNIQUE KEY `drohne_id` (`drohne_id`),
  UNIQUE KEY `drohnen_modell` (`drohnen_modell`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=15 ;

--
-- TRUNCATE Tabelle vor dem Einfügen `Drohne`
--

TRUNCATE TABLE `Drohne`;
--
-- Daten für Tabelle `Drohne`
--

INSERT INTO `Drohne` (`drohne_id`, `drohnen_modell`, `fluggewicht_in_gramm`, `flugzeit_in_min`, `diagonale_groesse_in_mm`, `maximale_flughoehe_in_m`, `hoechstgeschwindigkeit_in_kmh`, `bild`) VALUES
(2, 'PostmanDrohne3', 50, 50, 50, 50, 50, '/uploads/AK47.jpg'),
(3, 'Vodka Mobil', 420, 40, 69, 8, 187, '/uploads/170px-Vladimir_Putin_(2017-07-08)4.jpg'),
(4, 'Inspire 2', 3290, 28, 605, 2500, 94, '/uploads/inspire21.jpg'),
(5, 'KarenDrone', 55, 99, 45, 99, 1, '/uploads/communist_party_flag.png'),
(6, 'Vodka Mobil2', 1, 1, 1, 1, 1, '/assets/images/drohne_quadrat.png'),
(7, 'JonasDrone', 50, 50, 50, 50, 50, '/uploads/Jonas2.png'),
(8, 'Boeing 767', 279172, 15, 47600, 0, 12, '/uploads/320px-DDLC-Sayuri-early.png'),
(9, 'zasha', 420, 420, 420, 420, 420, '/uploads/Alex_emote31.png'),
(10, 'katze', 7, 7, 7, 7, 7, '/assets/images/drohne_quadrat.png'),
(11, 'fasf', 10, 10, 10, 10, 10, '/assets/images/drohne_quadrat.png'),
(12, 'lollo', 6, 6, 6, 6, 6, '/uploads/MarioCappy01Large.jpg'),
(13, 'NETWORK Test50', 100, 5, 10, 10, 10, '/assets/images/drohne_quadrat.png'),
(14, 'NETWORK Test12', 100, 5, 10, 10, 10, '/assets/images/drohne_quadrat.png');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Fluege`
--

CREATE TABLE IF NOT EXISTS `Fluege` (
  `flug_id` int(10) NOT NULL COMMENT 'Die ID des Fluges',
  `pilot_id` int(3) DEFAULT NULL COMMENT 'ID des Piloten',
  `drohne_id` int(3) DEFAULT NULL COMMENT 'ID der Drohne',
  `checkliste_name_id` int(3) DEFAULT NULL,
  `flugbezeichnung` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT 'Bezeichnung eines fluges',
  `flugdatum` date NOT NULL COMMENT 'Datum des Fluges (YYYY/MM/DD)',
  `einsatzort_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Einsatzort des Fluges',
  `breitengrad` double NOT NULL,
  `laengengrad` double NOT NULL,
  `einsatzbeginn` time DEFAULT NULL COMMENT 'Um wie viel Uhr der Flug geplant ist',
  `einsatzende` time DEFAULT NULL COMMENT 'Zeitpunkt des abgeschlossenen Fluges',
  `flugdauer` int(11) DEFAULT NULL COMMENT 'Wie lang der Flug gedauert hat',
  `start_und_landungen` int(3) DEFAULT NULL COMMENT 'Anzahl der Starts und Landungen',
  `besondere_vorkommnisse` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT 'Kommentar für besondere Vorkommnisse während des Fluges',
  `flug_protokoll` longtext CHARACTER SET utf8,
  PRIMARY KEY (`flug_id`),
  UNIQUE KEY `flug_id` (`flug_id`),
  KEY `drohne_id` (`drohne_id`),
  KEY `pilot_id` (`pilot_id`),
  KEY `einsatzbeginn` (`einsatzbeginn`),
  KEY `Fluege_ibfk_1` (`checkliste_name_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- TRUNCATE Tabelle vor dem Einfügen `Fluege`
--

TRUNCATE TABLE `Fluege`;
--
-- Daten für Tabelle `Fluege`
--

INSERT INTO `Fluege` (`flug_id`, `pilot_id`, `drohne_id`, `checkliste_name_id`, `flugbezeichnung`, `flugdatum`, `einsatzort_name`, `breitengrad`, `laengengrad`, `einsatzbeginn`, `einsatzende`, `flugdauer`, `start_und_landungen`, `besondere_vorkommnisse`, `flug_protokoll`) VALUES
(1, 11, 2, 1, 'RakowFlug', '2019-01-16', 'RäköwTüwn\\\\isödgsdfg', 51.121807098389, 10.400694847107, '10:00:00', '23:00:00', 46800, NULL, '', NULL),
(2, 1, 8, 2, 'App Test', '2019-01-02', 'Oberhausen', 51.469928741455, 6.8551602363586, '10:00:00', '12:00:00', 7200, NULL, '', NULL),
(3, 1, 7, 2, 'App Test', '2019-01-14', 'Bottrop', 51.524700164795, 6.9251999855042, '03:19:00', '04:30:00', 4260, NULL, '', NULL),
(4, 1, 2, 2, 'App Test', '2019-01-19', 'Oberhausen', 51.469928741455, 6.8551602363586, '22:10:00', '23:59:00', 6540, NULL, '', NULL),
(5, 1, 8, 1, '9/11 Test', '2019-01-27', 'neuss', 51.196418762207, 6.6943202018738, '05:05:00', '06:06:00', 3660, NULL, 'olivia stinkt', NULL),
(6, 1, 2, 1, 'huhu2', '2019-01-17', 'Drüpter Straße 72, 46519 Alpen', 51.58947, 6.54572, '19:00:00', '20:00:00', 3600, NULL, '', NULL),
(7, 3, 8, 6, 'wer ist postman?', '2019-01-11', 'Castrop-Rauxel', 51.547660827637, 7.3089499473572, '11:11:00', '11:11:00', 0, NULL, 'dceece', NULL),
(8, 1, 2, 1, 'huhu3', '2019-01-17', 'Alpen', 51.574859619141, 6.512179851532, '10:00:00', '11:00:00', 3600, NULL, '', NULL),
(9, 9, 2, 10, 'ääääääääää', '2019-01-16', 'Hallo', 51.479499816895, 7.0470199584961, '10:00:00', '23:50:00', 49800, NULL, '', NULL),
(10, 7, 2, 1, 'TestHeute', '2019-01-15', 'Moers', 51.452590942383, 6.6252698898315, '10:00:00', '23:00:00', 46800, NULL, '', NULL),
(11, 14, 2, 1, 'NicolesFlug', '2019-01-16', 'Großenbaum', 51.121807098389, 10.400694847107, '08:00:00', '23:59:00', 57540, NULL, '', NULL),
(12, 15, 2, 1, 'Phillip', '2019-01-07', 'Düsseldorf', 51.215629577637, 6.7760400772095, '11:00:00', '23:00:00', 43200, NULL, '', NULL),
(13, 1, 12, 3, 'dadadd', '2019-01-22', 'Hamburger Straße 44, Bremen', 53.07, 8.8377, '14:10:00', '23:59:00', 35340, NULL, 'dada dad', NULL),
(14, 1, 8, 6, 'wow', '8888-01-20', 'wow', 51.121807098389, 10.400694847107, '11:01:00', '11:01:00', 0, NULL, 'wow', NULL),
(15, 1, 8, 1, '1', '2019-01-06', '1', 51.121807098389, 10.400694847107, '11:11:00', '11:11:00', 0, NULL, '', NULL),
(16, 1, 8, 1, '2', '0000-00-00', '2', 51.121807098389, 10.400694847107, '22:22:00', '22:22:00', 0, NULL, '', NULL),
(17, 1, 8, 1, 'troisdorf bayerwerk', '3333-03-31', 'Hauptstraße 56, 53842 Troisdorf', 50.829561, 7.1170258, '03:33:00', '03:33:00', 0, NULL, '', NULL),
(18, 1, 8, 1, 'Haus der Geschichte', '2019-01-04', 'Berliner Straße, Braunschweig', 52.27843, 10.56573, '04:44:00', '06:44:00', 7200, NULL, '', NULL),
(19, 1, 8, 1, 'Wasserwerk Mönchengladbach', '2019-06-06', 'Hindenburgstraße 22, 41061 Mönchengladbach', 51.19385, 6.43296, '06:59:00', '08:59:00', 7200, NULL, '', NULL),
(20, 1, 8, 1, '7', '7777-07-07', '7', 51.121807098389, 10.400694847107, '07:07:00', '07:07:00', 0, NULL, '', NULL),
(21, 1, 8, 2, '8', '8888-08-08', '8', 51.121807098389, 10.400694847107, '08:08:00', '08:08:00', 0, NULL, '', NULL),
(22, 1, 8, 1, 'select vorname from Pilot;', '2019-02-16', 'qsdas', 51.121807098389, 10.400694847107, '12:03:00', '23:12:00', 40140, NULL, '', NULL),
(23, 1, 8, 1, 'zfzjh', '2019-02-07', 'gftz', 51.121807098389, 10.400694847107, '03:45:00', '05:59:00', 8040, NULL, '', NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Flug_zuordnen`
--

CREATE TABLE IF NOT EXISTS `Flug_zuordnen` (
  `flug_id` int(11) NOT NULL,
  `akku_id` int(11) NOT NULL,
  `anzahl` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `Flug_zuordnen`
--

TRUNCATE TABLE `Flug_zuordnen`;
-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Nachrichten`
--

CREATE TABLE IF NOT EXISTS `Nachrichten` (
  `bezeichnung` varchar(50) NOT NULL,
  `nachricht` varchar(200) NOT NULL,
  PRIMARY KEY (`bezeichnung`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `Nachrichten`
--

TRUNCATE TABLE `Nachrichten`;
--
-- Daten für Tabelle `Nachrichten`
--

INSERT INTO `Nachrichten` (`bezeichnung`, `nachricht`) VALUES
('erfolgreicher_login', 'Sie wurden erfolgreich eingeloggt.'),
('erfolgreiche_akku_erstellung', 'Der Akku wurde erfolgreich erstellt.'),
('erfolgreiche_checklisten_erstellung', 'Die Checkliste konnte erfolgreich erstellt werden.'),
('erfolgreiche_drohnen_erstellung', 'Die Drohne wurde erfolgreich erstellt.'),
('erfolgreiche_flug_erstellung', 'Der Flug wurde erfolgreich erstellt.'),
('erfolgreiche_registrierung', 'Ihre Registrierung war erfolgreich.'),
('fehlgeschlagener_login', 'Sie konnten nicht eingeloggt werden. Versuchen Sie es später noch einmal.'),
('fehlgeschlagene_akku_erstellung', 'Der Akku konnte nicht erstellt werden.'),
('fehlgeschlagene_checklisten_erstellung', 'Die Checkliste konnte nicht erstellt werden.'),
('fehlgeschlagene_drohnen_erstellung', 'Die Drohne konnte nicht erstellt werden.'),
('fehlgeschlagene_flug_erstellung', 'Der Flug konnte nicht erstellt werden.'),
('fehlgeschlagene_registrierung', 'Ihre Registrierung ist fehlgeschlagen.');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Pilot`
--

CREATE TABLE IF NOT EXISTS `Pilot` (
  `pilot_id` int(3) NOT NULL DEFAULT '0' COMMENT 'ID des Piloten',
  `profilbild` varchar(255) NOT NULL DEFAULT '/assets/images/muster_pb.png' COMMENT 'Bild des Piloten',
  `email_adresse` varchar(60) NOT NULL,
  `passwort` varchar(255) NOT NULL COMMENT 'Verschluesseltes passwort des Piloten Accounts',
  `vorname` varchar(30) NOT NULL COMMENT 'Vorname des Piloten',
  `nachname` varchar(30) NOT NULL COMMENT 'Nachname des Piloten',
  `studiengang` varchar(30) NOT NULL,
  `rolle` bit(1) NOT NULL COMMENT 'Rolle des Piloten Accounts (Admin oder User)',
  `loeschberechtigung` bit(1) NOT NULL COMMENT 'Ist der User zum loeschen berechtigt?',
  `letzter_login` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `aktivitaet` bit(1) NOT NULL COMMENT 'Ist der User aktiv? Nach 90 Tagen seit dem letzten Login = User inaktiv (0=inaktiv,1=aktiv)',
  PRIMARY KEY (`pilot_id`),
  UNIQUE KEY `pilot_id` (`pilot_id`),
  UNIQUE KEY `email_adresse` (`email_adresse`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `Pilot`
--

TRUNCATE TABLE `Pilot`;
--
-- Daten für Tabelle `Pilot`
--

INSERT INTO `Pilot` (`pilot_id`, `profilbild`, `email_adresse`, `passwort`, `vorname`, `nachname`, `studiengang`, `rolle`, `loeschberechtigung`, `letzter_login`, `aktivitaet`) VALUES
(1, '/uploads/Jonas5.png', 'test@test.de', '$2y$10$DRV5asgGjiPZpTQaARSRtOYeTQVV1B8cOi3qazjaQVBZ.Zu3oGDNu', '   test', 'test', 'bmt', b'1', b'1', '2019-02-11 12:05:31', b'1'),
(2, '/uploads/wuetendes-maedchen.jpg', 'apptest@test.de', '$2y$10$/pO1b1z3hzeCYPbUzLUnp.hc/vy1x9K4Z4//BXJLcyR.bYK5m6L8y', '  app', 'test', 'bmi', b'0', b'0', '2019-01-31 18:20:46', b'0'),
(3, '/uploads/134-cat_funnycat_funny_cat_123.jpg', 'yannikbrgs@gmail.com', '$2y$10$v9EwFqgCXuRvhc8QmPyYQeAqnOM.Y7ALwtCWGlQs33JWg54NbS/I.', 'Yannik', 'Borges', 'BMI', b'0', b'0', '2019-01-21 12:02:19', b'0'),
(4, '/uploads/istockphoto-450137037-1024x1024.jpg', 'big@chungus.de', '$2y$10$sOcrymX/KmQUIwIS0iWHouhgX9omMUr/PoXbEov6Wt6kXlkQoHdyS', ' Big', 'Chungus', 'BMI', b'1', b'0', '2019-01-25 10:56:05', b'0'),
(5, '/assets/images/muster_pb.png', 'oo@oo.oo.oo.de', '$2y$10$NmXlmtfE5NCM2HZ3diZb1uR.ddWTo.lwZN4H81vROUUlDz17dmOfq', '  oo2', 'oo', 'oo', b'1', b'0', '2019-02-08 19:10:53', b'0'),
(6, '/assets/images/muster_pb.png', 'test@zu.zu', '$2y$10$FEKB8r8OlWgm1wPXEbYPiuyeuRunvjiJjsEB9sTn6FetwXZW1RMV.', 'Test', 'test', 'test', b'0', b'0', '2019-01-25 16:46:01', b'0'),
(7, '/uploads/mail_box_nadine.jpg', 'Frankl@test.de', '$2y$10$upf0jp3/.RWJmR2ItSPBx.TCEmJ957yh1IGGR4eDK4CSsqbyjhBP2', ' Frank', 'Test', 'Medieninformatik', b'1', b'0', '2019-01-25 10:54:05', b'0'),
(8, '/assets/images/muster_pb.png', 'til2@test.de', '$2y$10$bELLiyAFQz8.SDCxNfOOE.iH5Nb9Ee/U/BMpjw4wrdQdW6wKUvU7W', 'Til', 'Test2', 'huhu', b'1', b'0', '2019-01-16 10:37:20', b'0'),
(9, '/uploads/mark_zuckerberg_1523440229_-_Kopie.jpg', 'markzuckerberg@facebook.de', '$2y$10$/8fOJjyM5CjaYYtIv5iU4uXuy89mSFQOKoSVSGxaZylX82v2AoeOu', ' Mark', 'Zuckerberg', 'FB', b'1', b'0', '2019-01-16 10:42:46', b'0'),
(10, '/assets/images/muster_pb.png', 'opop', '$2y$10$p0tC7zjNChjW.P8xxFoBo.DKoJ/5loatuJ9cZYnwOR0rULYUuWjMi', 'opop', 'oopop', 'opop', b'1', b'0', '2019-01-16 11:45:00', b'0'),
(11, '/uploads/Thomas_C_Rakow2.jpg', 'thomas@rakow.de', '$2y$10$QSqnG0OtkTovWelwE5F8WemuPOOVC1BQb8vK9fqkdTB19..WSTP6W', 'Thomas', 'Rakow', 'Datenbanken', b'0', b'0', '2019-01-16 11:46:45', b'0'),
(12, '/uploads/cash-cow.jpg', 'cashcow123nippel@yannik4president.org', '$2y$10$Oi.L1BU.M5bolJosyKgAjOo7QX9.UjCxa4rtSPDEF2eJ0STLx9XIG', 'Cash', 'Cow', 'MMI', b'0', b'0', '2019-01-16 13:12:40', b'0'),
(13, '/uploads/jonas.jpg', 'test@test1234.de', '$2y$10$L0OWFUxPtzN0P1h7p51UNe/5K6e4gon.pU9Uq5EUM99FeYmmo4uGy', '  Jonas', 'Baur ', 'BMI', b'0', b'0', '2019-01-18 12:27:47', b'0'),
(14, '/uploads/IMG-20180810-WA0000.jpg', 'nicole@fleichmann.de', '$2y$10$W0THsu6gxqny9a5aqN/1Ku5XWp216/080Sk6EMzcGlkcVPE1D.WN2', ' Nicole', 'Fleischmann', 'Medieninformatik', b'0', b'0', '2019-01-18 13:00:44', b'0'),
(15, '/uploads/IMG-20180119-WA0001.jpg', 'philipprichdale@nix.de', '$2y$10$OB8jKBP.LJSMHW1PraXJE.KJeEVLVVjhftYRpJSV.CTDfxaqpE5s2', ' Phillip', 'Richdale', 'Medieninformatik', b'0', b'0', '2019-01-18 13:03:36', b'0'),
(16, '/assets/images/muster_pb.png', 'checkboxtest@checkboxtest.checkboxtest', '$2y$10$wQOsDAjE/FxSwolpgpj1aOa73fBl79XEHN7CeLaPz8oSGEhp9Jojq', 'checkboxtest', 'checkboxtest', 'checkboxtest', b'1', b'0', '2019-01-22 12:05:31', b'0'),
(17, '/assets/images/muster_pb.png', '1@1.1', '$2y$10$xkURsPM0Lq0iid3z.suqEu1fxc2rKHEN57m37K.lSWvTD0dct/hO.', '1', '1', '1', b'1', b'0', '2019-01-22 13:15:16', b'0'),
(18, '/assets/images/muster_pb.png', '2@2.2', '$2y$10$rX4s3dItAE01/GLkJZSL6OIKXsQyKay2UUXYKtYciBiIMfFtj.bve', '2', '2', '2', b'1', b'0', '2019-01-22 13:15:31', b'0'),
(19, '/uploads/51rBMi-IoyL__SY355_.jpg', '3@3.3', '$2y$10$Ld04SJaNs6SjZVvzYSo41e24UrC5qI/S.859FHn84q72MvFUSRIlG', ' 3', '3', '3', b'1', b'0', '2019-01-25 09:31:08', b'0'),
(20, '/assets/images/muster_pb.png', '4@4.4', '$2y$10$6zMv5eKFari78Bwwfg1f4.KynZZ98kideRJhhFyLVyHAqaNc.fibe', '4', '4', '4', b'1', b'0', '2019-01-22 13:16:00', b'0'),
(21, '/assets/images/muster_pb.png', '5@5.5', '$2y$10$NPp61524albu/71HzGO3hu5UK7gsCiUsuGr7YExRWEymA3xJh4sdO', '5', '5', '5', b'1', b'0', '2019-01-22 13:16:10', b'0'),
(22, '/uploads/61dnWwnbDiL__SL1000_.jpg', '6@6.6', '$2y$10$twFKJM2dOFRxZnbkulz.5uXCzxY9e2cL.ZitMymfMMle1d9u7Y88.', ' 6', '6', '6', b'1', b'0', '2019-01-25 09:31:24', b'0'),
(23, '/assets/images/muster_pb.png', '7@7.7', '$2y$10$TOtxRlm/Lw.XAAMBlxamuerG1NlDrkItbr.PlItp7/B76vX1gceXG', '7', '7', '7', b'1', b'0', '2019-01-22 13:16:53', b'0'),
(24, '/assets/images/muster_pb.png', 'abcdefg@abcdefg.abcdefg', '$2y$10$Zpn1Rj5RYqRsVzhrY0BIp.iyhWpnMzYO1e6OnRPeE19mI/xy2Bnh.', 'abcdefg', 'abcdefg', 'abcdefg', b'1', b'0', '2019-01-23 19:54:31', b'0'),
(25, '/assets/images/muster_pb.png', 'qwer@qwer.qwer', '$2y$10$GudDTbptTSO2b04djav3SOu62POLhwlViEKj3U5HGyFPdC.W0iRwe', 'qwer', 'qwer', 'qwer', b'0', b'0', '2019-01-25 17:47:29', b'0');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `test`
--

CREATE TABLE IF NOT EXISTS `test` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `data` varchar(255) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=94 ;

--
-- TRUNCATE Tabelle vor dem Einfügen `test`
--

TRUNCATE TABLE `test`;
--
-- Daten für Tabelle `test`
--

INSERT INTO `test` (`id`, `data`, `timestamp`) VALUES
(1, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20"}', '2018-11-23 11:43:31'),
(2, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20"}', '2018-11-23 11:43:37'),
(3, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20"}', '2018-11-23 11:43:39'),
(4, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20"}', '2018-11-23 11:43:41'),
(5, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20"}', '2018-11-23 11:43:43'),
(6, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20"}', '2018-11-23 11:43:49'),
(7, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20"}', '2018-11-23 11:43:55'),
(8, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20","drohne_id":2}', '2018-11-23 11:45:20'),
(9, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20","drohne_id":2}', '2018-11-23 11:45:28'),
(10, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20","drohne_id":2}', '2018-11-23 11:45:33'),
(11, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20","drohne_id":2}', '2018-11-23 11:45:58'),
(12, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20","drohne_id":2}', '2018-11-23 11:46:19'),
(13, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20","drohne_id":2}', '2018-11-23 11:46:22'),
(14, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20","drohne_id":2}', '2018-11-23 11:46:24'),
(15, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20","drohne_id":2}', '2018-11-23 11:46:30'),
(16, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20","drohne_id":2}', '2018-11-23 11:46:35'),
(17, '{"drohnen_modell":"appdrone","akku_id":"4","fluggewicht_in_gramm":"20","flugzeit_in_min":"20","diagonale_groesse_in_mm":"20","maximale_flueghoehe_in_m":"20","hoechstgeschwindigkeit_in_kmh":"20","drohne_id":2}', '2018-11-23 11:47:04'),
(18, '{"drohnen_modell":"qweqwe","akku_id":"4","fluggewicht_in_gramm":"12","flugzeit_in_min":"1","diagonale_groesse_in_mm":"1","maximale_flueghoehe_in_m":"1","hoechstgeschwindigkeit_in_kmh":"1","drohne_id":2}', '2018-11-23 11:48:02'),
(19, '{"drohnen_modell":"HalloDrohne","akku_id":"2","fluggewicht_in_gramm":"2","flugzeit_in_min":"2","diagonale_groesse_in_mm":"2","maximale_flueghoehe_in_m":"2","hoechstgeschwindigkeit_in_kmh":"2","drohne_id":2}', '2018-11-23 12:04:36'),
(20, '{"drohnen_modell":"HalloDrohne","akku_id":"2","fluggewicht_in_gramm":"2","flugzeit_in_min":"2","diagonale_groesse_in_mm":"2","maximale_flueghoehe_in_m":"2","hoechstgeschwindigkeit_in_kmh":"2","drohne_id":8}', '2018-11-23 12:04:36'),
(21, '{"drohnen_modell":"XaxXax","akku_id":"2","fluggewicht_in_gramm":"2","flugzeit_in_min":"2","diagonale_groesse_in_mm":"2","maximale_flueghoehe_in_m":"2","hoechstgeschwindigkeit_in_kmh":"2","drohne_id":8}', '2018-11-23 12:04:48'),
(22, '{"drohnen_modell":"oopop","akku_id":"4","fluggewicht_in_gramm":"111","flugzeit_in_min":"11","diagonale_groesse_in_mm":"11","maximale_flueghoehe_in_m":"11","hoechstgeschwindigkeit_in_kmh":"11"}', '2018-11-23 12:09:17'),
(23, '{"data":{"request":[],"server":{"TMPDIR":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHPRC":"\\/opt\\/php-7.0.3\\/lib\\/","TEMP":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHP_DOCUMENT_ROOT":"\\/var\\/www\\/clients\\/client4\\/web59","PATH":"\\/usr\\/local\\/sbin:\\/', '2018-11-28 15:07:00'),
(24, '{"data":{"request":[],"server":{"TMPDIR":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHPRC":"\\/opt\\/php-7.0.3\\/lib\\/","TEMP":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHP_DOCUMENT_ROOT":"\\/var\\/www\\/clients\\/client4\\/web59","PATH":"\\/usr\\/local\\/sbin:\\/', '2018-11-28 15:07:24'),
(25, '{"data":{"request":[],"server":{"TMPDIR":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHPRC":"\\/opt\\/php-7.0.3\\/lib\\/","TEMP":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHP_DOCUMENT_ROOT":"\\/var\\/www\\/clients\\/client4\\/web59","PATH":"\\/usr\\/local\\/sbin:\\/', '2018-11-28 15:07:44'),
(26, '{"data":{"request":[],"server":{"TMPDIR":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHPRC":"\\/opt\\/php-7.0.3\\/lib\\/","TEMP":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHP_DOCUMENT_ROOT":"\\/var\\/www\\/clients\\/client4\\/web59","PATH":"\\/usr\\/local\\/sbin:\\/', '2018-11-28 15:08:27'),
(27, '{"data":{"request":"[]","server":"{\\"TMPDIR\\":\\"\\\\\\/var\\\\\\/www\\\\\\/clients\\\\\\/client4\\\\\\/web59\\\\\\/tmp\\",\\"PHPRC\\":\\"\\\\\\/opt\\\\\\/php-7.0.3\\\\\\/lib\\\\\\/\\",\\"TEMP\\":\\"\\\\\\/var\\\\\\/www\\\\\\/clients\\\\\\/client4\\\\\\/web59\\\\\\/tmp\\",\\"PHP_DOCUMENT_ROOT\\":\\"\\\\\\/var\\\\\\/www\\\\', '2018-11-28 15:08:58'),
(28, '{"data":{"request":[],"server":{"TMPDIR":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHPRC":"\\/opt\\/php-7.0.3\\/lib\\/","TEMP":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHP_DOCUMENT_ROOT":"\\/var\\/www\\/clients\\/client4\\/web59","PATH":"\\/usr\\/local\\/sbin:\\/', '2018-11-28 15:09:09'),
(29, '{"data":{"server":{"TMPDIR":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHPRC":"\\/opt\\/php-7.0.3\\/lib\\/","TEMP":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHP_DOCUMENT_ROOT":"\\/var\\/www\\/clients\\/client4\\/web59","PATH":"\\/usr\\/local\\/sbin:\\/usr\\/local\\/b', '2018-11-28 15:09:27'),
(30, '{"data":{"request":[],"server":{"TMPDIR":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHPRC":"\\/opt\\/php-7.0.3\\/lib\\/","TEMP":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHP_DOCUMENT_ROOT":"\\/var\\/www\\/clients\\/client4\\/web59","PATH":"\\/usr\\/local\\/sbin:\\/', '2018-11-28 15:09:34'),
(31, '{"data":{"request":[],"server":{"TMPDIR":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHPRC":"\\/opt\\/php-7.0.3\\/lib\\/","TEMP":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHP_DOCUMENT_ROOT":"\\/var\\/www\\/clients\\/client4\\/web59","PATH":"\\/usr\\/local\\/sbin:\\/', '2018-11-28 15:12:39'),
(32, '{"data":{"__ci_last_regenerate":1543931758}}', '2018-12-04 13:55:58'),
(33, '{"data":{"__ci_last_regenerate":1543932204,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"session":"3ebu6ro9bh', '2018-12-04 14:03:24'),
(34, '{"data":{"__ci_last_regenerate":1543932207,"session":"14vj0or68n1m3f83112k4bre5gmrq8e4"}}', '2018-12-04 14:03:27'),
(35, '{"data":{"__ci_last_regenerate":1543932204,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"session":"3ebu6ro9bh', '2018-12-04 14:04:38'),
(36, '{"data":{"__ci_last_regenerate":1543932288,"session":"9bmo2ahss9b8b77054p8raj460iopfph"}}', '2018-12-04 14:04:48'),
(37, '{"data":{"__ci_last_regenerate":1543932204,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"session":"3ebu6ro9bh', '2018-12-04 14:04:50'),
(38, '{"data":{"__ci_last_regenerate":1543932204,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"session":"3ebu6ro9bh', '2018-12-04 14:05:25'),
(39, '{"data":{"__ci_last_regenerate":1543932204,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"session":"3ebu6ro9bh', '2018-12-04 14:05:43'),
(40, '{"data":{"__ci_last_regenerate":1543932204,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"session":"3ebu6ro9bh', '2018-12-04 14:05:54'),
(41, '{"data":{"__ci_last_regenerate":1543932361,"session":"54u3q4dejbd5hjs6kbka6ft2d3oal7gt"}}', '2018-12-04 14:06:01'),
(42, '{"data":{"__ci_last_regenerate":1543932673,"session":"khhpbkuciimvu8ltggrc8jb34qlafkh0"}}', '2018-12-04 14:11:13'),
(43, '{"data":{"__ci_last_regenerate":1543932691,"session":"5e7fak9bmsrt4udjivsgo582vuifdor0"}}', '2018-12-04 14:11:31'),
(44, '{"data":{"__ci_last_regenerate":1543932700,"session":"vof2c2emernjvgl3ta1eb7b28gok2oi1"}}', '2018-12-04 14:11:40'),
(45, '{"data":{"__ci_last_regenerate":1543932810,"session":"sahebc6nuc3moc8b0ddd0i5orsdopjjq"}}', '2018-12-04 14:13:30'),
(46, '{"data":{"__ci_last_regenerate":1543933052,"session":"hhua242mks1i3sj3no1ussn07mlvhjl2"}}', '2018-12-04 14:17:32'),
(47, '{"data":{"__ci_last_regenerate":1543933073,"session":"08ql2onthg91difdm1muoj9v7gqpq343"}}', '2018-12-04 14:17:53'),
(48, '{"data":{"__ci_last_regenerate":1543933356,"session":"0c7484183e3h07to7hn9p07hmnrgf7th"}}', '2018-12-04 14:22:36'),
(49, '{"data":{"__ci_last_regenerate":1543933369,"session":"hg7pnic2v97gagsne5toub3uupvf9neq"}}', '2018-12-04 14:22:49'),
(50, '{"data":{"request":[]}}', '2018-12-04 14:38:30'),
(51, '{"data":{"server":{"TMPDIR":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHPRC":"\\/opt\\/php-7.0.3\\/lib\\/","TEMP":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHP_DOCUMENT_ROOT":"\\/var\\/www\\/clients\\/client4\\/web59","PATH":"\\/usr\\/local\\/sbin:\\/usr\\/local\\/b', '2018-12-04 14:38:47'),
(52, '{"data":{"server":{"TMPDIR":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHPRC":"\\/opt\\/php-7.0.3\\/lib\\/","TEMP":"\\/var\\/www\\/clients\\/client4\\/web59\\/tmp","PHP_DOCUMENT_ROOT":"\\/var\\/www\\/clients\\/client4\\/web59","PATH":"\\/usr\\/local\\/sbin:\\/usr\\/local\\/b', '2018-12-04 14:39:18'),
(53, '{"data":{"session":"8k9e8o0ogro0mhd5s1u9rf62chr269bd"}}', '2018-12-04 14:51:26'),
(54, '{"data":{"session":"sl3a0l15sb2kc8uijn78qujp33ijilu7"}}', '2018-12-04 14:51:31'),
(55, '{"data":{"session":"qoqigpq6i20kc9nilbqmufeq4fan469t"}}', '2018-12-04 14:55:12'),
(56, '{"data":{"session":{"__ci_last_regenerate":1544011663,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true}}}}', '2018-12-05 12:12:05'),
(57, '{"data":{"session":{"__ci_last_regenerate":1544011663,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true}}}}', '2018-12-05 12:12:23'),
(58, '{"data":{"session":{"__ci_last_regenerate":1544012022}}}', '2018-12-05 12:13:42'),
(59, '{"data":{"session":{"__ci_last_regenerate":1544012067}}}', '2018-12-05 12:14:27'),
(60, '{"data":{"session":{"__ci_last_regenerate":1544012128,"users":{"pilot_id":"11","email_adresse":"apptest@test.de","vorname":"testVornmae","nachname":"testNachname","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"","rolle":"0","eingeloggt":tru', '2018-12-05 12:15:37'),
(61, '{"data":{"session":{"__ci_last_regenerate":1544013882,"users":{"pilot_id":"11","email_adresse":"apptest@test.de","vorname":"testVornmae","nachname":"testNachname","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"","rolle":"0","eingeloggt":tru', '2018-12-05 12:44:47'),
(62, '{"data":{"session":{"__ci_last_regenerate":1544013878,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"was geht"', '2018-12-05 12:44:54'),
(63, '{"data":{"session":{"__ci_last_regenerate":1544013878,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"was geht"', '2018-12-05 12:45:36'),
(64, '{"data":{"session":{"__ci_last_regenerate":1544013921,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"was geht"', '2018-12-05 12:45:47'),
(65, '{"data":{"session":{"__ci_last_regenerate":1544013878,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"was geht"', '2018-12-05 12:45:48'),
(66, '{"data":{"session":{"__ci_last_regenerate":1544013882,"users":{"pilot_id":"11","email_adresse":"apptest@test.de","vorname":"testVornmae","nachname":"testNachname","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"","rolle":"0","eingeloggt":tru', '2018-12-05 12:45:50'),
(67, '{"data":{"session":{"__ci_last_regenerate":1544013892,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"was geht"', '2018-12-05 12:46:06'),
(68, '{"data":{"session":{"__ci_last_regenerate":1544013882,"users":{"pilot_id":"11","email_adresse":"apptest@test.de","vorname":"testVornmae","nachname":"testNachname","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"","rolle":"0","eingeloggt":tru', '2018-12-05 12:46:36'),
(69, '{"data":{"session":{"__ci_last_regenerate":1544013997,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"hi":"hi",', '2018-12-05 12:46:51'),
(70, '{"data":{"session":{"__ci_last_regenerate":1544013997,"users":{"pilot_id":"1","email_adresse":"test@Test.de","vorname":"   test","nachname":"test","profilbild":"\\/assets\\/images\\/muster_pb.png","studiengang":"set","rolle":"1","eingeloggt":true},"hi":"hi",', '2018-12-05 12:47:03'),
(71, '{"data":{"session":"11"}}', '2018-12-05 12:49:06'),
(72, '{"data":{"session":"1"}}', '2018-12-05 12:49:15'),
(73, '{"data":{"session":"1"}}', '2018-12-05 12:50:20'),
(74, '{"data":{"session":"1"}}', '2018-12-05 12:50:55'),
(75, '{"data":{"session":"11"}}', '2018-12-05 12:51:04'),
(76, '{"data":{"session":"1"}}', '2018-12-05 12:55:14'),
(77, '{"data":{"session":"1"}}', '2018-12-05 12:55:22'),
(78, '{"data":{"session":"11"}}', '2018-12-05 13:19:31'),
(79, '{"data":{"session":"1"}}', '2018-12-05 13:20:26'),
(80, '{"data":{"session":"11"}}', '2018-12-05 13:22:29'),
(81, '{"data":{"session":"1"}}', '2018-12-05 13:49:15'),
(82, '{"data":{"session":"1"}}', '2018-12-05 14:04:36'),
(83, '{"data":{"session":"11"}}', '2018-12-05 14:45:55'),
(84, '{"data":{"session":"11"}}', '2018-12-05 23:19:05'),
(85, '{"data":{"session":"1"}}', '2018-12-09 14:33:52'),
(86, '{"data":{"session":"1"}}', '2018-12-09 14:34:43'),
(87, '{"data":{"session":"1"}}', '2018-12-09 14:48:33'),
(88, '{"data":{"session":"1"}}', '2018-12-09 17:47:50'),
(89, '{"data":{"session":"11"}}', '2018-12-10 18:09:12'),
(90, '{"data":{"session":"1"}}', '2018-12-11 09:52:05'),
(91, '{"data":{"session":"1"}}', '2018-12-11 09:52:40'),
(92, '{"data":{"session":"1"}}', '2018-12-11 10:08:53'),
(93, '{"data":{"session":"1"}}', '2018-12-11 10:09:56');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `User_backup`
--

CREATE TABLE IF NOT EXISTS `User_backup` (
  `passwort` varchar(255) NOT NULL,
  `email_adresse` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `User_backup`
--

TRUNCATE TABLE `User_backup`;
--
-- Daten für Tabelle `User_backup`
--

INSERT INTO `User_backup` (`passwort`, `email_adresse`) VALUES
('Jonas@jonas.de', 'Jonas'),
('a', 'a@a.de'),
('tk', 'tk@tk.de'),
('zu', 'zu@zu.de'),
('a', 'a@b.de'),
('b', 'b@b.de'),
('ba', 'ab@b.de'),
('ba', 'acb@b.de'),
('test', 'appTest1@test.de'),
('test', 'apptest@test.de'),
('chuck', 'chuck@chuck.de'),
('654321', 'max.mustermann@muste'),
('	henrikIstDoof@doofus.de', 'ILoveMinecraft'),
('Idqwe@hotmail.de', 'id'),
('wowlolwow@web.de', 'wow'),
('t.kammann1614@web.de', 'hallo'),
('appder@antes.de', 'antes'),
('der@antes.de', 'antes'),
('test@apptest.de', 'test'),
('derAntes@test.de', 'test'),
('14:00', '16:00'),
('postman@dronelog.de', 'postman'),
('app2test@test.de', 'test'),
('networkTest97@test.de', 'test'),
('networkTest18@test.de', 'test'),
('networkTest65@test.de', 'test'),
('networkTest13@test.de', 'test'),
('networkTest37@test.de', 'test'),
('networkTest88@test.de', 'test'),
('networkTest98@test.de', 'test'),
('networkTest66@test.de', 'test'),
('networkTest57@test.de', 'test'),
('networkTest55@test.de', 'test'),
('networkTest90@test.de', 'test'),
('networkTest92@test.de', 'test'),
('networkTest70@test.de', 'test'),
('networkTest72@test.de', 'test'),
('networkTest36@test.de', 'test'),
('networkTest10@test.de', 'test'),
('networkTest91@test.de', 'test'),
('networkTest63@test.de', 'test'),
('networkTest38@test.de', 'test'),
('networkTest94@test.de', 'test'),
('networkTest52@test.de', 'test'),
('networkTest50@test.de', 'test'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('thomas.kammann@study.hs-duesseldorf.de', 'oo'),
('apptest@test.de', 'test'),
('yannikbrgs@gmail.com', '!Qayxsw2'),
('big@chungus.de', '!Qayxsw2'),
('oo@oo.oo.oo.de', 'oo'),
('postman@dronelog.de', 'postman'),
('til@test.de', '1234'),
('til2@test.de', '1234'),
('markzuckerberg@facebook.de', 'test'),
('opop', 'oopop'),
('thomas@rakow.de', '1234'),
('cashcow123nippel@yannik4president.org', 'cashcow'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@delete.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deletee.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('deleteme@deleteme.de', 'postman'),
('test@test1234.de', 'test'),
('nicole@fleichmann.de', '1234'),
('philipprichdale@nix.de', 'richdale'),
('checkboxtest@checkboxtest.checkboxtest', 'test'),
('checkboxtest@checkboxtest.checkboxtest', 'checkboxtest'),
('1@1.1', '1'),
('2@2.2', '2'),
('3@3.3', '3'),
('4@4.4', '4'),
('5@5.5', '5'),
('6@6.6', '6'),
('7@7.7', '7'),
('abcdefg@abcdefg@.abcdefg', 'abcdefg'),
('abcdefg@abcdefg@.abcdefg', 'abcdefg'),
('abcdefg@abcdefg.abcdefg', 'abcdefg'),
('normie@normie.de', 'testnormie'),
('q@q.q', 'q'),
('test@zu.zu', 'test'),
('qwer@qwer.qwer', 'qwer');

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `Akkus_zuordnen`
--
ALTER TABLE `Akkus_zuordnen`
  ADD CONSTRAINT `Akkus_zuordnen_ibfk_1` FOREIGN KEY (`akku_id`) REFERENCES `Akku` (`akku_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `Akkus_zuordnen_ibfk_2` FOREIGN KEY (`drohne_id`) REFERENCES `Drohne` (`drohne_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints der Tabelle `Checkliste_werte`
--
ALTER TABLE `Checkliste_werte`
  ADD CONSTRAINT `Checkliste_werte_ibfk_3` FOREIGN KEY (`element_id`) REFERENCES `Checklisten_elemente` (`element_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Checkliste_werte_ibfk_4` FOREIGN KEY (`flug_id`) REFERENCES `Fluege` (`checkliste_name_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `Checkliste_zuordnen`
--
ALTER TABLE `Checkliste_zuordnen`
  ADD CONSTRAINT `Checkliste_zuordnen_ibfk_1` FOREIGN KEY (`checkliste_name_id`) REFERENCES `Checkliste_name` (`checkliste_name_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Checkliste_zuordnen_ibfk_2` FOREIGN KEY (`element_id`) REFERENCES `Checklisten_elemente` (`element_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `Fluege`
--
ALTER TABLE `Fluege`
  ADD CONSTRAINT `FK_DrohneFlugplanung` FOREIGN KEY (`drohne_id`) REFERENCES `Drohne` (`drohne_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `Fluege_ibfk_1` FOREIGN KEY (`checkliste_name_id`) REFERENCES `Checkliste_name` (`checkliste_name_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `Fluege_ibfk_2` FOREIGN KEY (`pilot_id`) REFERENCES `Pilot` (`pilot_id`) ON DELETE SET NULL ON UPDATE CASCADE;

SET FOREIGN_KEY_CHECKS=1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
