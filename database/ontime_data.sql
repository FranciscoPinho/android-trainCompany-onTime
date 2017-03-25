--Stations
INSERT INTO `station` (`id`, `name`) VALUES (NULL, 'São Bento');
INSERT INTO `station` (`id`, `name`) VALUES (NULL, 'Gaia');
INSERT INTO `station` (`id`, `name`) VALUES (NULL, 'Aveiro');
INSERT INTO `station` (`id`, `name`) VALUES (NULL, 'Coimbra');
INSERT INTO `station` (`id`, `name`) VALUES (NULL, 'Oriente');
INSERT INTO `station` (`id`, `name`) VALUES (NULL, 'Santa Apolónia');

--Schedules
--Ordered by departure Times
INSERT INTO `schedule` (`id`,`direction`) VALUES (NULL, 'Santa Apolónia'); --06:25  06:35	7:21  07:46   9:16    9:25
INSERT INTO `schedule` (`id`,`direction`) VALUES (NULL, 'Santa Apolónia'); --08:25  08:35	9:21  09:46   11:16   11:25
INSERT INTO `schedule` (`id`,`direction`) VALUES (NULL, 'Santa Apolónia'); --10:25  10:35   11:21 11:46   13:16   13:25
INSERT INTO `schedule` (`id`,`direction`) VALUES (NULL, 'Santa Apolónia'); --12:25  12:35   13:21 13:46   15:16   15:25
INSERT INTO `schedule` (`id`,`direction`) VALUES (NULL, 'São Bento'); -- 7:00  7:09	 8:45  9:20  9:50   10
INSERT INTO `schedule` (`id`,`direction`) VALUES (NULL, 'São Bento'); -- 9:00  9:09	 10:45 11:20 11:50  12
INSERT INTO `schedule` (`id`,`direction`) VALUES (NULL, 'São Bento'); -- 11:00 11:09 12:45 13:20 13:50  14
INSERT INTO `schedule` (`id`,`direction`) VALUES (NULL, 'São Bento'); -- 13:00 13:09 14:45 15:20 15:50  16

--StationSchedule
--Sao Bento -> Santa Apolónia
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '1', '1', '2', '7.5', '06:25:00','06:35:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '1', '2', '3', '75', '06:35:00', '07:21:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '1', '3', '4', '58.6', '07:21:00','07:46:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '1', '4', '5', '200', '07:46:00', '09:16:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '1', '5', '6', '7', '09:16:00','09:25:00');

INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '2', '1', '2', '7.5', '08:25:00','08:35:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '2', '2', '3', '75', '08:35:00', '09:21:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '2', '3', '4', '58.6', '09:21:00','09:46:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '2', '4', '5', '200', '09:46:00', '11:16:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '2', '5', '6', '7', '11:16:00','11:25:00');

INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '3', '1', '2', '7.5', '10:25:00','10:35:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '3', '2', '3', '75', '10:35:00', '11:21:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '3', '3', '4', '58.6', '11:21:00','11:46:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '3', '4', '5', '200', '11:46:00', '13:16:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '3', '5', '6', '7', '13:16:00','13:25:00');

INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '4', '1', '2', '7.5', '12:25:00','12:35:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '4', '2', '3', '75', '12:35:00', '13:21:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '4', '3', '4', '58.6', '13:21:00','13:46:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '4', '4', '5', '200', '13:46:00', '15:16:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '4', '5', '6', '7', '15:16:00','15:25:00');

--Santa Apolónia -> Sao Bento                                                             
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '5', '6', '5', '7', '07:00:00','07:09:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '5', '5', '4', '200', '07:09:00', '08:45:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '5', '4', '3', '58.6', '08:45:00','09:20:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '5', '3', '2', '75', '09:20:00', '09:50:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '5', '2', '1', '7.5', '09:50:00','10:00:00');

INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '6', '6', '5', '7', '09:00:00','09:09:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '6', '5', '4', '200', '09:09:00', '10:45:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '6', '4', '3', '58.6', '10:45:00','11:20:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '6', '3', '2', '75', '11:20:00', '11:50:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '6', '2', '1', '7.5', '11:50:00','12:00:00');

INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '7', '6', '5', '7', '11:00:00','11:09:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '7', '5', '4', '200', '11:09:00', '12:45:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '7', '4', '3', '58.6', '12:45:00','13:20:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '7', '3', '2', '75', '13:20:00', '13:50:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '7', '2', '1', '7.5', '13:50:00','14:00:00');

INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '8', '6', '5', '7', '13:00:00','13:09:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '8', '5', '4', '200', '13:09:00', '14:45:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '8', '4', '3', '58.6', '14:45:00','15:20:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '8', '3', '2', '75', '15:20:00', '15:50:00');
INSERT INTO `stationschedule` (`id`, `scheduleID`, `station`, `nextstation`, `distance`, `DepartureTime`, `ArrivalTime`) VALUES (NULL, '8', '2', '1', '7.5', '15:50:00','16:00:00');

--Train
--Destination Santa Apolónia
INSERT INTO `train` (`id`,'designation', `capacity`, `scheduleID`) VALUES (NULL,'L1', '50', '1');
INSERT INTO `train` (`id`,'designation', `capacity`, `scheduleID`) VALUES (NULL,'L2', '30', '2');
INSERT INTO `train` (`id`,'designation', `capacity`, `scheduleID`) VALUES (NULL,'L3', '30', '3');
INSERT INTO `train` (`id`,'designation', `capacity`, `scheduleID`) VALUES (NULL,'L4', '5', '4');
--Destination São Bento
INSERT INTO `train` (`id`,'designation', `capacity`, `scheduleID`) VALUES (NULL,'P1', '50', '5');
INSERT INTO `train` (`id`,'designation', `capacity`, `scheduleID`) VALUES (NULL,'P2', '40', '6');
INSERT INTO `train` (`id`,'designation', `capacity`, `scheduleID`) VALUES (NULL,'P3', '35', '7');
INSERT INTO `train` (`id`,'designation', `capacity`, `scheduleID`) VALUES (NULL,'P4', '10', '8');



--Keys

--
-- Dumping data for table `encryption_keys`
--

INSERT INTO `encryption_keys` (`private`, `public`) VALUES
('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');



