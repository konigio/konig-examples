@prefix reg : <http://example.com/ns/registrar/> .
@prefix org: <http://www.w3.org/ns/org#> .
@prefix schema : <http://schema.org/> .
@prefix alias : <http://example.com/ns/alias/> .
@prefix schema1 : <http://example.com/shapes/v1/schema/> .
@prefix schema2 : <http://example.com/shapes/v2/schema/> .
@prefix org1 : <http://example.com/shapes/v1/org/> .
@prefix org2 : <http://example.com/shapes/v2/org/> .

@tableShapeIriTemplate <http://example.com/shapes/v1/{targetClassNamespacePrefix}/{targetClassLocalName}Shape> .
@columnPredicateIriTemplate <{alias}{columnName}/> .
@tableTargetShapeIriTemplate <http://example.com/shapes/v2/{targetClassNamespacePrefix}/{targetClassLocalName}Shape> .

CREATE TABLE registrar.Person (
	person_id BIGINT PRIMARY KEY NOT NULL
		SEMANTICS path /reg:registrarId,
		
	first_name VARCHAR(64)
		SEMANTICS path /schema:givenName,
		
	last_name VARCHAR(64)
		SEMANTICS path /schema:familyName
)
SEMANTICS
	targetClass schema:Person
.

CREATE TABLE registrar.CourseSection (
	section_id BIGINT PRIMARY KEY NOT NULL
		SEMANTICS path /reg:registrarId,
		
	section_name VARCHAR(255) NOT NULL
		SEMANTICS path /schema:name,
		
	start_date DATE NOT NULL
		SEMANTICS path /schema:startDate,
		
	end_date DATE NOT NULL
		SEMANTICS path /schema:endDate
)
SEMANTICS
	targetClass schema:CourseInstance
.	

CREATE TABLE registrar.CourseSectionPerson (
	person_id BIGINT NOT NULL 
		SEMANTICS path /org:member/reg:registrarId,

	section_id BIGINT NOT NULL
		SEMANTICS path /org:organization/reg:registrarId,
		
	role_id BIGINT NOT NULL
		SEMANTICS path /org:role/reg:registrarId,
		
	CONSTRAINT FK_PERSON FOREIGN KEY (person_id) REFERENCES registrar.Person (person_id),
	CONSTRAINT FK_SECTION FOREIGN KEY (section_id) REFERENCES registrar.CourseSection (section_id),
	CONSTRAINT FK_ROLE FOREIGN KEY (role_id) REFERENCES registrar.Role (role_id)
)
SEMANTICS
	targetClass org:Membership
.

CREATE TABLE registrar.Role (
	role_id BIGINT PRIMARY KEY NOT NULL
		SEMANTICS path /reg:registrarId,
		
	role_name VARCHAR(16) NOT NULL
		SEMANTICS path /schema:name
)
SEMANTICS
	targetClass org:Role
.