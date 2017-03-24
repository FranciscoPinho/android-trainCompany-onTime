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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;


ALTER TABLE  `creditcard` ADD CONSTRAINT creditcard_fk FOREIGN KEY (  `userID` ) REFERENCES  `ontime`.`users` (
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
) ENGINE=InnoDB;

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

CREATE TABLE IF NOT EXISTS `schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `direction` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE  `schedule` ADD CONSTRAINT schedule_fk FOREIGN KEY (  `direction` ) REFERENCES  `ontime`.`station` (
`name`
) ON DELETE CASCADE ON UPDATE CASCADE ;

-- --------------------------------------------------------

--
-- Table structure for table `stationschedule`
--

CREATE TABLE IF NOT EXISTS `stationschedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scheduleID` int(11) NOT NULL,
  `station` int(11) NOT NULL,
  `nextstation` int(11) NOT NULL,
  `distance` float,
  `DepartureTime` TIME,
  `ArrivalTime` TIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `stationCombo` (`scheduleID`,`station`,`nextstation`)
) ENGINE=InnoDB;


ALTER TABLE `stationschedule` ADD CONSTRAINT stationschedule_schedule_fk FOREIGN KEY (  `scheduleID` ) REFERENCES  `ontime`.`schedule` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE `stationschedule` ADD CONSTRAINT stationschedule_station_fk FOREIGN KEY (`station`) REFERENCES `ontime`.`station`(`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `stationschedule` ADD CONSTRAINT stationschedule_nextstation_fk FOREIGN KEY (  `nextstation` ) REFERENCES  `ontime`.`station`(`id`) 
ON DELETE CASCADE ON UPDATE CASCADE ;

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
  `price` decimal(6,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;


ALTER TABLE  `ticket` ADD CONSTRAINT stationschedule_train_fk FOREIGN KEY (  `trainID` ) REFERENCES  `ontime`.`train` (
`id`
) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE  `ticket` ADD CONSTRAINT stationschedule_origin_fk FOREIGN KEY (  `origin` ) REFERENCES  `ontime`.`station` (
`name`
) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE  `ticket` ADD CONSTRAINT stationschedule_destination_fk FOREIGN KEY (  `destination` ) REFERENCES  `ontime`.`station` (
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
) ENGINE=InnoDB;


ALTER TABLE  `train` ADD CONSTRAINT train_schedule_fk FOREIGN KEY (  `scheduleID` ) REFERENCES  `ontime`.`schedule` (
`id`
) ON DELETE CASCADE ON UPDATE CASCADE ;



