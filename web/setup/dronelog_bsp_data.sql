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
-- TRUNCATE Tabelle vor dem Einfügen `Checkliste_werte`
--

TRUNCATE TABLE `Checkliste_werte`;
-- --------------------------------------------------------

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

SET FOREIGN_KEY_CHECKS=1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
