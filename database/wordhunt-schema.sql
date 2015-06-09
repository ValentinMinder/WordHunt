# ************************************************************
# Sequel Pro SQL dump
# Version 4135
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.5.38)
# Database: wordhunt
# Generation Time: 2015-05-06 09:41:23 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

# DROP DATABASE IF EXISTS `wordhunt`;

CREATE SCHEMA `wordhunt`;
USE `wordhunt`;


# Dump of table Lang
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Lang`;

CREATE TABLE `Lang` (
  `id_lang` int(11) NOT NULL AUTO_INCREMENT,
  `valeur` varchar(255) NOT NULL,
  PRIMARY KEY (`id_lang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# creates basic languages
INSERT INTO `Lang` (`id_lang`,`valeur`)
VALUES
(0, 'French'),
(1, 'English'),
(2, 'Frenglish');

# Dump of table Grille
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Grille`;

CREATE TABLE `Grille` (
  `id_grille` int(11) NOT NULL AUTO_INCREMENT,
  `id_lang` int(11) NOT NULL,
  `valeurs_cases` text NOT NULL,
  `score_max` int(11) NOT NULL,
  `hashs` varchar(65535) NOT NULL,
  PRIMARY KEY (`id_grille`),
  CONSTRAINT `grille_ibfk_1` FOREIGN KEY (`id_lang`) REFERENCES `Lang` (`id_lang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table Score
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Score`;

CREATE TABLE `Score` (
  `id_grille` int(11) NOT NULL,
  `id_utilisateur` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  PRIMARY KEY (`id_grille`,`id_utilisateur`),
  KEY `id_utilisateur` (`id_utilisateur`),
  CONSTRAINT `score_ibfk_1` FOREIGN KEY (`id_utilisateur`) REFERENCES `Utilisateur` (`id_utilisateur`),
  CONSTRAINT `score_relation` FOREIGN KEY (`id_grille`) REFERENCES `Grille` (`id_grille`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table SolutionGrille
# ------------------------------------------------------------

DROP TABLE IF EXISTS `SolutionGrille`;

CREATE TABLE `SolutionGrille` (
  `id_grille` int(11) NOT NULL,
  `mots` varchar(65535) NOT NULL,
  PRIMARY KEY (`id_grille`),
  CONSTRAINT `solutiongrille_ibfk_2` FOREIGN KEY (`id_grille`) REFERENCES `Grille` (`id_grille`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table Utilisateur
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Utilisateur`;

CREATE TABLE `Utilisateur` (
  `id_utilisateur` int(11) NOT NULL AUTO_INCREMENT,
  `nom_utilisateur` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `salt` char(32) NOT NULL,
  `mot_de_passe` char(40) NOT NULL,
  `token` char(32),
  PRIMARY KEY (`id_utilisateur`),
  KEY `nom_utilisateur` (`nom_utilisateur`),
  KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
