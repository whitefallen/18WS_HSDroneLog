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
