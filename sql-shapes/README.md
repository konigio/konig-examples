# SQL Shapes
This example shows how to generate SHACL Shapes for a collection of SQL Tables.

The entities in this example include:

- *Person*
- *Course Section*
- *Role*
- *Membership*

where the *Membership* entity is an association class which specifies that some
*Person* is a member of some *Course Section* with a certain *Role*
(`Instructor` or `Learner`).

For each entity, we generate two shapes which represent (a) the
entity at rest in the RDBMS, and (b) the entity in flight.

We start with a SQL Table definition for each entity and map those entities to
OWL classes as shown in the following diagram:

![SQL Tables to OWL Classes](images/sql-to-rdf-mapping.png)

Notice that the names for classes and properties can be different in the RDBMS
model and the semantic model.

# Prerequisites
Make sure you have installed Maven and Git.  See [Getting Started](../README.md#GetStarted) for details.

You should have a basic understanding of RDF, SHACL Data Shapes, and the Turtle
serialization format as discussed in the following articles:
   * [RDF in a nutshell](http://developers.konig.io/docs/RDF_in_a_nutshell/)
   * [SHACL Basics](http://kcoyle.blogspot.com/2015/10/shacl-shapes-constraint-language.html)
   * [Graph Serialization](http://developers.konig.io/docs/Graph_Serialization/)

# Procedure

The procedure consists of the following steps:

1. Add semantic markup to the SQL Table definitions
2. Create a Maven POM that will generate the SHACL Shapes
3. Run the Maven build

We discuss these steps in detail below.

## Add semantic markup to SQL Table definitions

Save your SQL Table definitions to a file (or a collection of files).
As a best practice, these files should be placed in the following directory
(relative to the root of your Maven project):

```
    src/sql
```

Table definitions must be expressed in
[SQL-2003 CREATE TABLE](http://savage.net.au/SQL/sql-2003-2.bnf.html#table definition)
syntax enhanced with semantic annotations.

More precisely, you need to use a subset of SQL-2003.  Only the following kinds of
constraints are supported:

* NULL
* NOT NULL
* UNIQUE
* PRIMARY KEY
* FOREIGN KEY

A table that contains a `PRIMARY KEY` constraint will be mapped
to a Shape whose `sh:nodeKind` is `sh:IRI`.  This implies that every instance of
the associated class must be identified by an IRI.
If you don't want this result, simply remove the `PRIMARY KEY` constraint from
the table definition.

The SQL file names must end with the ".sql" suffix.

In this example, our table definitions are contained in the file [`src/sql/registrar.sql`](src/sql/registrar.sql)

Let's examine the semantic markup in this file.

## Namespace prefixes
The `registrar.sql` file starts with a collection of namespace prefixes in Turtle syntax:

```
@prefix org: <http://www.w3.org/ns/org#> .
@prefix schema : <http://schema.org/> .
@prefix reg : <http://example.com/ns/registrar/> .
@prefix org1 : <http://example.com/shapes/v1/org/> .
@prefix org2 : <http://example.com/shapes/v2/org/> .
@prefix schema1 : <http://example.com/shapes/v1/schema/> .
@prefix schema2 : <http://example.com/shapes/v2/schema/> .
@prefix alias : <http://example.com/ns/alias/> .
```

The following table explains the purpose of each namespace

| Prefix | Namespace Description                                                |
|--------|----------------------------------------------------------------------|
| org    | The [W3C Organization Ontology](https://www.w3.org/TR/vocab-org/).  We use it to model Membership and Role entities |
| schema | The  [Schema.org](http://schema.org/) Ontology. We use it to model Person and Organization entities |
| reg    | A custom namespace for this example.  We use it to define application-specific identifiers for entities |
| org1 | A custom namespace for SHACL shapes that represent `org:Membership`  and `org:Role` entities at rest in the RDBMS |
| org2 | A custom namespace for SHACL shapes that represent `org:Membership`  and `org:Role` entities in flight |
| schema1 | A custom namespace for SHACL shapes that represent Schema.org entities at rest in the RDBMS |
| schema2 | A custom namespace for SHACL shapes that represent Schema.org entities in flight |
| alias | A custom namespace for the RDBMS column names. |

## Target Class
Each SQL Table represents a certain type of entity described by
an OWL Class.  We map each table to the corresponding OWL class using the `targetClass` annotation like this:

```
CREATE TABLE registrar.Person (...)
SEMANTICS
  targetClass schema:Person
.

CREATE TABLE registrar.CourseSection (...)
SEMANTICS
  targetClass schema:CourseInstance
.

CREATE TABLE registrar.CourseSectionPerson (...)
SEMANTICS
  targetClass org:Membership
.

CREATE TABLE registrar.Role (...)
SEMANTICS
  targetClass org:Role
.
```

## Table Shape
Each SQL Table has a SHACL `Shape` that
describes the Table structure.  These Shape definitions will be generated
automatically.

We could have defined the Shape IRI for each Table individually with the `hasShape`
annotation like this:

```
CREATE TABLE registrar.Person (...)
SEMANTICS
  targetClass schema:Person,
  hasShape schema1:PersonShape
.

CREATE TABLE registrar.CourseSection (...)
SEMANTICS
  targetClass schema:CourseInstance,
  hasShape schema1:CourseInstanceShape
.

CREATE TABLE registrar.CourseSectionPerson (...)
SEMANTICS
  targetClass org:Membership,
  hasShape org1:MembershipShape
.

CREATE TABLE registrar.Role (...)
SEMANTICS
  targetClass org:Role,
  hasShape org1:RoleShape
.
```

But the Shape IRI values follow a common pattern, so it is easier to define
them once using an IRI template like this:

```
@tableShapeIriTemplate <http://example.com/shapes/v1/{targetClassNamespacePrefix}/{targetClassLocalName}Shape> .
```

With this template, we don't need the individual `hasShape` annotations.

## Column Predicate
Each column in a SQL Table is assigned an IRI which appears as the
`sh:predicate` property in the SHACL shape for the entity at rest in the RDBMS.


We could have defined the `sh:predicate` for each column individually
like this:

```
CREATE TABLE registrar.Person (
	person_id BIGINT PRIMARY KEY NOT NULL
		SEMANTICS predicate alias:person_id,

	first_name VARCHAR(64)
		SEMANTICS predicate alias:first_name,

	last_name VARCHAR(64)
		SEMANTICS predicate alias:last_name
)
```

But it is easier to just define a template once like this:

```
@columnPredicateIriTemplate <{alias}{columnName}/> .
```  

We are using the same namespace (http://example.com/ns/alias/) for all
columns, regardless of which Table the column comes from.  We could have defined a
different template that uses a distinct namespace for each table.  For instance, we might have defined the template like this:

```
@columnPredicateIriTemplate <http://example.com/rdbms/{schemaName}/{tableName}/{columnName}/> .

```

But there is really no benefit to defining a separate namespace for each table.
These predicates truly are aliases, i.e. alternative names for other
properties in standard OWL ontologies.  We might as well lump all aliases into a
single namespace.

# Target Shape

The *Target Shape* describes the same information found in the SQL Tables, but it uses
property names from the target semantic model instead of the raw column names.  
This shape is suitable for use as contract for the data in flight.

We could define the IRI for the Target Shape on each table with the `targetShape`
annotation like this:

```
CREATE TABLE registrar.Person (...)
SEMANTICS
  targetClass schema:Person,
  targetShape schema2:PersonShape
.

CREATE TABLE registrar.CourseSection (...)
SEMANTICS
  targetClass schema:CourseInstance,
  targetShape schema2:CourseInstanceShape
.

CREATE TABLE registrar.CourseSectionPerson (...)
SEMANTICS
  targetClass org:Membership,
  targetShape org2:MembershipShape
.

CREATE TABLE registrar.Role (...)
SEMANTICS
  targetClass org:Role,
  targetShape org2:RoleShape
.
```

But the *Target Shape* IRI follows a consistent pattern, and so it is easier to
define using a template like this:

```
@tableTargetShapeIriTemplate <http://example.com/shapes/v2/{targetClassNamespacePrefix}/{targetClassLocalName}Shape> .
```

## Property Paths

We map each column to a corresponding property in the target semantic model using
the `path` annotation.  Here's how those annotations appear in the `registrar.Person`
Table:

```
CREATE TABLE registrar.Person (
	person_id BIGINT PRIMARY KEY NOT NULL
		SEMANTICS path /reg:registrarId,

	first_name VARCHAR(64)
		SEMANTICS path /schema:givenName,

	last_name VARCHAR(64)
		SEMANTICS path /schema:familyName
)
```

The `path` syntax is based on SPARQL paths.  In most cases, the path includes
a single property, as shown in the example above.

But it is possible for a path to be defined as a sequence of properties as illustrated in the
`registrar.CourseSectionPerson` Table:

```
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
```

In this example, the `person_id` column is mapped to the path given by

```
  path /org:member/reg:registrarId
```

This means that the `person_id` column is equivalent to the value you would obtain
by dereferencing the `org:member` property of the entity and then finding the value
of the `reg:registrar` property.

The `section_id` and `role_id` properties are defined in a similar manner.

## Create a Maven POM
Now that we have annotated our SQL Table definitions with semantic markup, we need
a POM that will generate the SHACL Shapes.  Our POM file is

   [pom.xml](pom.xml)

The important element within this file is the plugin listed below:

```
<plugin>
	  		<groupId>io.konig</groupId>
	  		<artifactId>konig-sql-shape-maven-plugin</artifactId>
	  		<version>${konig.version}</version>
	  		<configuration>
	  			<sourceDir>src/sql/</sourceDir>
	  			<outDir>target/generated/src/rdf/shapes</outDir>
	  		</configuration>
	  		<executions>
	  			<execution>
		  			<phase>generate-sources</phase>
		  			<goals>
		  				<goal>generate</goal>
		  			</goals>
	  			</execution>
	  		</executions>
  		</plugin>
```  
The `sourceDir` parameter tells the plugin where to find the `*.sql` files.

The `outDir` parameter tells the plugin where to put the generated SHACL files.

## Run the maven build

To run the maven build, execute the following command:

```
  mvn clean installed
```

The plugin will create the following files:

```
    +-- target/generated/src/rdf/shapes/origin
    |   +-- registrar_CourseSection.ttl
    |   +-- registrar_CourseSectionPerson.ttl
    |   +-- registrar_Person.ttl
    |   +-- registrar_Role
    |
    +-- target/generated/src/rdf/shapes/target
    |   +-- org2_MembershipShape.ttl
    |   +-- org2_RoleShape.ttl
    |   +-- schema2_CourseInstanceShape.ttl
    |   +-- schemas_PersonShape.ttl
```

The `origin` folder holds shapes for the data at rest in the RDBMS.
The `target` folder holds shapes for the data in flight in accordance with the
target semantic model.
