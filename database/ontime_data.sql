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
INSERT INTO `train` (`id`, `capacity`, `scheduleID`) VALUES (NULL, '50', '1');
INSERT INTO `train` (`id`, `capacity`, `scheduleID`) VALUES (NULL, '30', '2');
INSERT INTO `train` (`id`, `capacity`, `scheduleID`) VALUES (NULL, '30', '3');
INSERT INTO `train` (`id`, `capacity`, `scheduleID`) VALUES (NULL, '5', '4');
--Destination São Bento
INSERT INTO `train` (`id`, `capacity`, `scheduleID`) VALUES (NULL, '50', '5');
INSERT INTO `train` (`id`, `capacity`, `scheduleID`) VALUES (NULL, '40', '6');
INSERT INTO `train` (`id`, `capacity`, `scheduleID`) VALUES (NULL, '35', '7');
INSERT INTO `train` (`id`, `capacity`, `scheduleID`) VALUES (NULL, '10', '8');