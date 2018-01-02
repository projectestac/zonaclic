-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Temps de generació: 02-01-2018 a les 10:53:18
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
CREATE DATABASE IF NOT EXISTS `jclicrepo` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `jclicrepo`;

-- --------------------------------------------------------

--
-- Estructura de la taula `codes`
--

CREATE TABLE IF NOT EXISTS `codes` (
  `path` varchar(256) NOT NULL,
  `type` varchar(10) NOT NULL,
  `code` varchar(10) NOT NULL,
  UNIQUE KEY `codes` (`path`,`type`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de la taula `descriptions`
--

CREATE TABLE IF NOT EXISTS `descriptions` (
  `path` varchar(256) NOT NULL,
  `lang` varchar(3) NOT NULL,
  `title` text NOT NULL,
  `description` text,
  `languages` text,
  `areas` text,
  `levels` text,
  `descriptors` text,
  UNIQUE KEY `key` (`path`,`lang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de la taula `projects`
--

CREATE TABLE IF NOT EXISTS `projects` (
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
  `files` text,
  PRIMARY KEY (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de la taula `log`
--

CREATE TABLE `log` (
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `type` tinytext COLLATE latin1_general_ci NOT NULL,
  `msg` text COLLATE latin1_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Indexos per taules bolcades
--

--
-- Index de la taula `descriptions`
--
ALTER TABLE `descriptions`
  ADD UNIQUE KEY `key` (`path`,`lang`);
ALTER TABLE `descriptions` ADD FULLTEXT KEY `description` (`title`,`description`,`languages`,`areas`,`levels`,`descriptors`);

--
-- Index de la taula `projects`
--
ALTER TABLE `projects`
  ADD PRIMARY KEY (`path`);
ALTER TABLE `projects` ADD FULLTEXT KEY `author` (`author`);
ALTER TABLE `projects` ADD FULLTEXT KEY `school` (`school`);
ALTER TABLE `projects` ADD FULLTEXT KEY `title` (`title`);

--
-- Index de la taula `log`
--
ALTER TABLE `log`
  ADD PRIMARY KEY (`date`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;