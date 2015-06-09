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


# Dump of table Dictionnaire
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Dictionnaire`;

CREATE TABLE `Dictionnaire` (
  `id_dictionnaire` int(11) NOT NULL AUTO_INCREMENT,
  `id_lang` int(11) NOT NULL,
  PRIMARY KEY (`id_dictionnaire`),
  KEY `id_lang` (`id_lang`),
  CONSTRAINT `dictionnaire_ibfk_1` FOREIGN KEY (`id_lang`) REFERENCES `Lang` (`id_lang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table Grille
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Grille`;

CREATE TABLE `Grille` (
  `id_grille` int(11) NOT NULL AUTO_INCREMENT,
  `id_type` int(11) NOT NULL,
  `valeurs_cases` text NOT NULL,
  `score_max` int(11) NOT NULL,
  PRIMARY KEY (`id_grille`,`id_type`),
  KEY `id_type` (`id_type`),
  CONSTRAINT `grille_ibfk_1` FOREIGN KEY (`id_type`) REFERENCES `TypeJeux` (`id_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table Lang
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Lang`;

CREATE TABLE `Lang` (
  `id_lang` int(11) NOT NULL AUTO_INCREMENT,
  `valeur` varchar(255) NOT NULL,
  PRIMARY KEY (`id_lang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table Mot
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Mot`;

CREATE TABLE `Mot` (
  `id_mot` int(11) NOT NULL AUTO_INCREMENT,
  `id_dictionnaire` int(11) NOT NULL,
  `mot` varchar(255) NOT NULL,
  PRIMARY KEY (`id_mot`,`id_dictionnaire`),
  KEY `id_dictionnaire` (`id_dictionnaire`),
  CONSTRAINT `mot_ibfk_1` FOREIGN KEY (`id_dictionnaire`) REFERENCES `Dictionnaire` (`id_dictionnaire`)
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
  `id_mot` int(11) NOT NULL,
  PRIMARY KEY (`id_grille`,`id_mot`),
  KEY `id_mot` (`id_mot`),
  CONSTRAINT `solutiongrille_ibfk_2` FOREIGN KEY (`id_grille`) REFERENCES `Grille` (`id_grille`),
  CONSTRAINT `solutiongrille_ibfk_1` FOREIGN KEY (`id_mot`) REFERENCES `Mot` (`id_mot`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table TypeJeux
# ------------------------------------------------------------

DROP TABLE IF EXISTS `TypeJeux`;

CREATE TABLE `TypeJeux` (
  `id_type` int(11) NOT NULL,
  `valeur` varchar(255) NOT NULL,
  PRIMARY KEY (`id_type`)
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
  PRIMARY KEY (`id_utilisateur`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
