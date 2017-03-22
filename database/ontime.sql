--
-- Database: `ontime`
--
DROP DATABASE IF EXISTS `ontime`;
CREATE DATABASE IF NOT EXISTS `ontime`;
USE `ontime`;



-- --------------------------------------------------------

--
-- Table structure for table `users`
--

 
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(150) NOT NULL,
  `password_hash` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
);

-- --------------------------------------------------------

--
-- Table structure for table `creditcard`
--

CREATE TABLE IF NOT EXISTS `creditcard` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `userID` int(11) NOT NULL,
  `number` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);


ALTER TABLE  `creditcard` ADD FOREIGN KEY (  `userID` ) REFERENCES  `ontime`.`users` (
`id`
) ON DELETE CASCADE ON UPDATE CASCADE ;


-- --------------------------------------------------------

--
-- Table structure for table `station`
--

CREATE TABLE IF NOT EXISTS `station` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

CREATE TABLE IF NOT EXISTS `schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `direction` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE  `schedule` ADD FOREIGN KEY (  `direction` ) REFERENCES  `ontime`.`station` (
`name`
) ON DELETE CASCADE ON UPDATE CASCADE ;

-- --------------------------------------------------------

--
-- Table structure for table `stationschedule`
--

CREATE TABLE IF NOT EXISTS `stationschedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scheduleID` int(11) NOT NULL,
  `station` varchar(100) NOT NULL,
  `nextstation` varchar(100),
  `DepartureTime` TIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `stationCombo` (`station`,`nextstation`)
);


ALTER TABLE  `stationschedule` ADD FOREIGN KEY (  `scheduleID` ) REFERENCES  `ontime`.`schedule` (
`id`
) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE  `stationschedule` ADD FOREIGN KEY (  `station` ) REFERENCES  `ontime`.`station` (
`name`
) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE  `stationschedule` ADD FOREIGN KEY (  `nextstation` ) REFERENCES  `ontime`.`station` (
`name`
) ON DELETE CASCADE ON UPDATE CASCADE ;

-- --------------------------------------------------------

--
-- Table structure for table `ticket`
--

CREATE TABLE IF NOT EXISTS `ticket` (
  `id` varchar(36) NOT NULL,
  `trainID` int(11) NOT NULL,
  `validation` BOOLEAN NOT NULL,
  `origin` varchar(100) NOT NULL,
  `destination` varchar(100) NOT NULL,
  `departureTime` TIME NOT NULL,
  `arrivalTime` TIME NOT NULL,
  PRIMARY KEY (`id`)
);


ALTER TABLE  `ticket` ADD FOREIGN KEY (  `trainID` ) REFERENCES  `ontime`.`train` (
`id`
) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE  `ticket` ADD FOREIGN KEY (  `origin` ) REFERENCES  `ontime`.`station` (
`name`
) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE  `ticket` ADD FOREIGN KEY (  `destination` ) REFERENCES  `ontime`.`station` (
`name`
) ON DELETE CASCADE ON UPDATE CASCADE ;

-- --------------------------------------------------------

--
-- Table structure for table `train`
--

CREATE TABLE IF NOT EXISTS `train` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `capacity` smallint NOT NULL,
  `scheduleID` int(11) NOT NULL,
  PRIMARY KEY (`id`)
);


ALTER TABLE  `train` ADD FOREIGN KEY (  `scheduleID` ) REFERENCES  `ontime`.`schedule` (
`id`
) ON DELETE CASCADE ON UPDATE CASCADE ;



