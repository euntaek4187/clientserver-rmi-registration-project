setting

# MySQL ddl
CREATE TABLE `courses` (
  `course_id` int NOT NULL,
  `professor_name` varchar(255) NOT NULL,
  `course_name` varchar(255) NOT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `course_prerequisites` (
  `course_id` int NOT NULL,
  `prerequisite_id` int NOT NULL,
  PRIMARY KEY (`course_id`,`prerequisite_id`),
  KEY `prerequisite_id` (`prerequisite_id`),
  CONSTRAINT `course_prerequisites_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `students` (
  `student_id` int NOT NULL,
  `password` varchar(255) NOT NULL,
  `student_name` varchar(255) NOT NULL,
  `department` varchar(255) DEFAULT 'Unknown',
  PRIMARY KEY (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `student_courses` (
  `student_id` int NOT NULL,
  `course_id` int NOT NULL,
  PRIMARY KEY (`student_id`,`course_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `student_courses_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `student_courses_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

# first setting
INSERT INTO courses (course_id, professor_name, course_name) VALUES
(12345, 'Park', 'Java_Programming'),
(23456, 'Park', 'C++_Programming'),
(17651, 'Kim', 'Models_of_Software_Systems'),
(17652, 'Ko', 'Methods_of_Software_Development'),
(17653, 'Kim', 'Managing_Software_Development'),
(17654, 'Ahn', 'Analysis_of_Software_Artifacts'),
(17655, 'Lee', 'Architectures_of_Software_Systems');

INSERT INTO course_prerequisites (course_id, prerequisite_id) VALUES
(17651, 12345),
(17652, 23456),
(17654, 17651),
(17655, 12345),
(17655, 17651);