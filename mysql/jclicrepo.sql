-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Temps de generació: 04-12-2017 a les 17:11:11
-- Versió del servidor: 5.7.20-0ubuntu0.16.04.1
-- Versió de PHP: 7.0.22-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de dades: `jclicrepo`
--

-- --------------------------------------------------------

--
-- Estructura de la taula `codes`
--

CREATE TABLE `codes` (
  `path` varchar(256) NOT NULL,
  `type` varchar(10) NOT NULL,
  `code` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de la taula `descriptions`
--

CREATE TABLE `descriptions` (
  `path` varchar(256) NOT NULL,
  `lang` varchar(3) NOT NULL,
  `description` text,
  `languages` text,
  `areas` text,
  `levels` text,
  `descriptors` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de la taula `projects`
--

CREATE TABLE `projects` (
  `path` varchar(256) NOT NULL,
  `title` text NOT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date` date DEFAULT NULL,
  `author` text,
  `school` text,
  `mainFile` tinytext,
  `cover` tinytext,
  `thumbnail` tinytext,
  `zipFile` tinytext,
  `instFile` tinytext,
  `clicZoneId` int(11) DEFAULT NULL,
  `orderId` int(11) DEFAULT NULL,
  `files` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexos per taules bolcades
--

--
-- Index de la taula `codes`
--
ALTER TABLE `codes`
  ADD UNIQUE KEY `codes` (`path`,`type`,`code`);

--
-- Index de la taula `descriptions`
--
ALTER TABLE `descriptions`
  ADD UNIQUE KEY `key` (`path`,`lang`);
ALTER TABLE `descriptions` ADD FULLTEXT KEY `description` (`description`,`languages`,`areas`,`levels`,`descriptors`);

--
-- Index de la taula `projects`
--
ALTER TABLE `projects`
  ADD PRIMARY KEY (`path`);
ALTER TABLE `projects` ADD FULLTEXT KEY `author` (`author`);
ALTER TABLE `projects` ADD FULLTEXT KEY `school` (`school`);
ALTER TABLE `projects` ADD FULLTEXT KEY `title` (`title`);

--- Crea usuari 'clic'
CREATE USER 'clic'@'%' IDENTIFIED BY 'clic';
GRANT ALL PRIVILEGES ON *.* TO 'clic'@'%' REQUIRE NONE WITH GRANT OPTION MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


