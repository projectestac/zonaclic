-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Temps de generació: 03-12-2017 a les 21:50:43
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
CREATE DATABASE IF NOT EXISTS `jclicrepo` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `jclicrepo`;

-- --------------------------------------------------------

--
-- Estructura de la taula `codes`
--

CREATE TABLE `codes` (
  `path` varchar(256) COLLATE utf8_bin NOT NULL,
  `type` varchar(10) COLLATE utf8_bin NOT NULL,
  `code` varchar(10) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de la taula `descriptions`
--

CREATE TABLE `descriptions` (
  `path` varchar(256) COLLATE utf8_bin NOT NULL,
  `lang` varchar(3) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin,
  `languages` text COLLATE utf8_bin,
  `areas` text COLLATE utf8_bin,
  `levels` text COLLATE utf8_bin,
  `descriptors` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Estructura de la taula `projects`
--

CREATE TABLE `projects` (
  `path` varchar(256) COLLATE utf8_bin NOT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date` date DEFAULT NULL,
  `author` text COLLATE utf8_bin,
  `school` text COLLATE utf8_bin,
  `mainFile` tinytext COLLATE utf8_bin,
  `cover` tinytext COLLATE utf8_bin,
  `thumbnail` tinytext COLLATE utf8_bin,
  `zipFile` tinytext COLLATE utf8_bin,
  `instFile` tinytext COLLATE utf8_bin,
  `clicZoneId` int(11) DEFAULT NULL,
  `orderId` int(11) DEFAULT NULL,
  `files` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


CREATE USER 'jclicrepo'@'%' IDENTIFIED WITH mysql_native_password AS 'clic';
GRANT ALL PRIVILEGES ON `jclicrepo`.* TO 'jclicrepo'@'%';

