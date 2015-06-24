-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- 主机: 127.0.0.1
-- 生成日期: 2014 �?06 �?02 �?12:36
-- 服务器版本: 5.6.11
-- PHP 版本: 5.5.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `app_yibaosysu`
--
CREATE DATABASE IF NOT EXISTS `app_yibaosysu` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `app_yibaosysu`;

-- --------------------------------------------------------

--
-- 表的结构 `bikeinfo`
--

CREATE TABLE IF NOT EXISTS `bikeinfo` (
  `bikeId` int(4) NOT NULL AUTO_INCREMENT,
  `authorId` int(4) NOT NULL,
  `authorName` text COLLATE utf8_bin NOT NULL,
  `time` datetime NOT NULL,
  `bikeName` text COLLATE utf8_bin NOT NULL,
  `bikePicture` char(100) COLLATE utf8_bin NOT NULL DEFAULT 'images/defualt.jpg',
  `content` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`bikeId`),
  UNIQUE KEY `bikeId` (`bikeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `commentId` int(4) NOT NULL AUTO_INCREMENT,
  `bikeId` int(4) NOT NULL,
  `authorId` int(4) NOT NULL,
  `content` text COLLATE utf8_bin NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`commentId`),
  UNIQUE KEY `commentId` (`commentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `label` (
  `labelId` int(4) NOT NULL AUTO_INCREMENT,
  `bikeId` int(4) NOT NULL,
  `content` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`labelId`),
  UNIQUE KEY `labelId` (`labelId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;
-- --------------------------------------------------------

--
-- 表的结构 `userinfo`
--

CREATE TABLE IF NOT EXISTS `userinfo` (
  `userId` int(4) NOT NULL AUTO_INCREMENT,
  `userName` char(16) COLLATE utf8_bin NOT NULL,
  `password` char(16) COLLATE utf8_bin NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `userId` (`userId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `userinfo`
--

INSERT INTO `userinfo` (`userId`, `userName`, `password`) VALUES
(1, 'wyl', 'wyl');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
