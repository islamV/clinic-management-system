DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email_address` varchar(255) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `gender` enum('male','female') NOT NULL,
  `age` int NOT NULL,
  `role` enum('Admin','Doctor','Patient') NOT NULL DEFAULT 'Patient',
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_address` (`email_address`),
  UNIQUE KEY `phone_number` (`phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `users` (`name`, `email_address`, `phone_number`, `gender`, `age`, `role`, `password`)
VALUES ('admin', 'admin@admin.com', NULL, 'male', 30, 'Admin', '123456');



DROP TABLE IF EXISTS `doctors`;
CREATE TABLE `doctors` (
  `doctor_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `specialty` varchar(255) NOT NULL,
  PRIMARY KEY (`doctor_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `doctors_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



DROP TABLE IF EXISTS `schedules`;
CREATE TABLE `schedules` (
  `schedule_id` int NOT NULL AUTO_INCREMENT,
  `doctor_id` int NOT NULL,
  PRIMARY KEY (`schedule_id`),
  KEY `doctor_id` (`doctor_id`),
  CONSTRAINT `schedules_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`doctor_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `specification`;
CREATE TABLE `specification` (
  `specialty_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`specialty_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


DROP TABLE IF EXISTS `appointments`;
CREATE TABLE `appointments` (
  `appointment_id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int NOT NULL,
  `doctor_id` int NOT NULL,
  `schedule_id` int NOT NULL,
  `queue_number` int NOT NULL,
  `status` enum('Pending','Confirmed','Cancelled','Completed') NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`appointment_id`),
  KEY `patient_id` (`patient_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `schedule_id` (`schedule_id`),
  CONSTRAINT `appointments_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `appointments_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `appointments_ibfk_3` FOREIGN KEY (`schedule_id`) REFERENCES `schedules` (`schedule_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

