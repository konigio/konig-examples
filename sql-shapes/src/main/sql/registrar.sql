CREATE TABLE registrar.Person (
	person_id BIGINT PRIMARY KEY NOT NULL,
	first_name VARCHAR(64),
	last_name VARCHAR(64)
);

CREATE TABLE registrar.CourseSection (
	section_id BIGINT PRIMARY KEY NOT NULL,
	section_name VARCHAR(255) NOT NULL,
	start_date DATE NOT NULL,
	end_date DATE NOT NULL
);

CREATE TABLE registrar.CourseSectionPerson (
	person_id BIGINT NOT NULL,
	section_id BIGINT NOT NULL,
	role_id BIGINT NOT NULL,
	CONSTRAINT FK_PERSON FOREIGN KEY (person_id) REFERENCES registrar.Person (person_id),
	CONSTRAINT FK_SECTION FOREIGN KEY (section_id) REFERENCES registrar.CourseSection (section_id),
	CONSTRAINT FK_ROLE FOREIGN KEY (role_id) REFERENCES registrar.Role (role_id)
);

CREATE TABLE registrar.Role (
	role_id BIGINT PRIMARY KEY NOT NULL,
	role_name VARCHAR(16) NOT NULL
);