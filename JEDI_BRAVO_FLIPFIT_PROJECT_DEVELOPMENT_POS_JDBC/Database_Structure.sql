use JEDI_BRAVO_FLIPFIT_PROJECT_DEVELOPMENT_POS_JDBC;

CREATE TABLE `GymUser` (
  `userId` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `address` varchar(45) DEFAULT NULL,
  `password` varchar(45) NOT NULL,
  `role` enum('ADMIN','GYM_OWNER','CUSTOMER') NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `GymOwner` (
  `userId` varchar(45) DEFAULT NULL,
  KEY `userId_idx` (`userId`),
  CONSTRAINT `userId` FOREIGN KEY (`userId`) REFERENCES `GymUser` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `GymCenter` (
  `centerId` varchar(45) NOT NULL,
  `centerLocn` varchar(45) NOT NULL,
  `centerCity` varchar(45) NOT NULL,
  `ownerId` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`centerId`),
  KEY `ownerId_idx` (`ownerId`),
  KEY `userId_idx` (`ownerId`),
  CONSTRAINT `ownerId` FOREIGN KEY (`ownerId`) REFERENCES `GymUser` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `GymSlot` (
  `slotId` varchar(45) NOT NULL,
  `startTime` time NOT NULL,
  `endTime` time NOT NULL,
  `totalSeats` int NOT NULL,
  `availableSeats` int DEFAULT NULL,
  PRIMARY KEY (`slotId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `SlotList` (
  `slotId` varchar(45) DEFAULT NULL,
  `gymId` varchar(45) DEFAULT NULL,
  KEY `slotId` (`slotId`),
  KEY `gymId` (`gymId`),
  CONSTRAINT `gymId` FOREIGN KEY (`gymId`) REFERENCES `GymCenter` (`centerId`),
  CONSTRAINT `slotId` FOREIGN KEY (`slotId`) REFERENCES `GymSlot` (`slotId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `Booking` (
  `BookingId` varchar(45) NOT NULL,
  `gymUser` varchar(45) NOT NULL,
  `gymSlot` varchar(45) NOT NULL,
  `gymCenter` varchar(45) NOT NULL,
  `dateAndTime` varchar(45) NOT NULL,
  `bookingStatus` enum('CONFIRMED','WAITLIST','CANCELLED') NOT NULL,
  PRIMARY KEY (`BookingId`),
  KEY `gymUser_idx` (`gymUser`),
  KEY `gymSlot_idx` (`gymSlot`),
  KEY `gymCenter_idx` (`gymCenter`),
  CONSTRAINT `gymCenter` FOREIGN KEY (`gymCenter`) REFERENCES `GymCenter` (`centerId`),
  CONSTRAINT `gymSlot` FOREIGN KEY (`gymSlot`) REFERENCES `GymSlot` (`slotId`),
  CONSTRAINT `gymUser` FOREIGN KEY (`gymUser`) REFERENCES `GymUser` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
