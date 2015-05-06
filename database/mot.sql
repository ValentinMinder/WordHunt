-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mer 06 Mai 2015 à 14:58
-- Version du serveur :  5.6.17
-- Version de PHP :  5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `wordhunt`
--

-- --------------------------------------------------------

--
-- Structure de la table `mot`
--

CREATE TABLE IF NOT EXISTS `mot` (
  `id_mot` int(11) NOT NULL AUTO_INCREMENT,
  `id_dictionnaire` int(11) NOT NULL,
  `mot` varchar(255) NOT NULL,
  PRIMARY KEY (`id_mot`,`id_dictionnaire`),
  KEY `id_dictionnaire` (`id_dictionnaire`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10640 ;

--
-- Contenu de la table `mot`
--

INSERT INTO `mot` (`id_mot`, `id_dictionnaire`, `mot`) VALUES
(10639, 1, 'ZYGOTE'),
(10638, 1, 'ZOZOTER'),
(10637, 1, 'ZONES'),
(10636, 1, 'ZOMBIE'),
(10635, 1, 'ZIP'),
(10634, 1, 'ZINC'),
(10633, 1, 'ZIGZAGUER'),
(10632, 1, 'ZÉZAYER'),
(10631, 1, 'ZÉRO'),
(10630, 1, 'ZÉLÉ'),
(10629, 1, 'ZÉBRÉ'),
(10628, 1, 'ZAÏROIS'),
(10627, 1, 'YVETTE'),
(10626, 1, 'YOUGOSLAVES'),
(10625, 1, 'YOGA'),
(10624, 1, 'YEUX'),
(10623, 1, 'YEN'),
(10622, 1, 'YANNICK'),
(10621, 1, 'YAK'),
(10620, 1, 'YACHT'),
(10619, 1, 'XÉNOPHOBIE'),
(10618, 1, 'XAVIER'),
(10617, 1, 'WILLIAMS'),
(10616, 1, 'WILFRIED'),
(10615, 1, 'WESTERN');

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `mot`
--
ALTER TABLE `mot`
  ADD CONSTRAINT `mot_ibfk_1` FOREIGN KEY (`id_dictionnaire`) REFERENCES `dictionnaire` (`id_dictionnaire`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
