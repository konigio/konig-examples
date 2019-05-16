This example presents a recipe for generating SHACL NodeShape descriptions from a collection of SQL CREATE TABLE statements.

For a gentle information to SHACL, see [SHACL Basics](http://kcoyle.blogspot.com/2015/10/shacl-shapes-constraint-language.html).
For a deep dive into the SHACL specification, see the [W3C SHACL Recommendation](https://www.w3.org/TR/shacl/).

# Overview
The following list summarizes the key elements within this recipe

1. Create a new project folder.
2. Add the `*.sql` files containing your DDL to the folder at `src/main/sql`.  Make sure that your DDL files end with the `.sql` suffix.
3. Create an RDF description (in Turtle syntax) of the namespace that will hold the generated `NodeShape` descriptions.
4. Create a `pom.xml` file like the one in this project.
5. Execute the maven build

We discuss these steps in more detail below.

## Create a new project folder

```
mkdir {YOUR_PROJECT_NAME}
cd {YOUR_PROJECT_NAME}
```

## Add the `*.sql` files

```
mkdir -p src/main/sql
cp {YOUR_SQL_DDL_FILES} src/main/sql
```

## Create an RDF Description of your Shapes namespace

1.  Copy the file `src/main/rdf/owl/ontologies.ttl` into your project at the same relative path.
2.  Customize the file.

The `ontologies.ttl` file in this example is the one used by Pearson Education.  If you are building a solution for
Pearson, you don't need to customize the file.  Otherwise, edit the file so that it is appropriate for your organization.

## Create your POM file

Start by copying the `pom.xml` file in this example.
There are three sections that you might want to customize.

1. Project Identifier and Name
2. Regular expression pattern for generating identifiers for NodeShapes
3. Namespace for the RDF Properties assigned to each table column.

We discuss these elements of the POM below.

### Project Identifier and Name

This example declares the following elements...

```
  <groupId>io.konig.examples</groupId>
  <artifactId>sql-to-shacl</artifactId>
  <version>1.0.0</version>
  <name>SQL DDL to SHACL</name>
```

You should specify an appropriate `groupId`, `artifactId`, `version`, and `name` for your project.

### Regular expression pattern for generating identifiers for NodeShapes

This example uses the following elements...

```
  <tableIriTemplate>
    <iriPattern>(^.*$)</iriPattern>
    <iriReplacement>https://schema.pearson.com/shapes/$1Shape</iriReplacement>
  </tableIriTemplate>
```

The `iriPattern` element is a regex pattern, and the `iriReplacement` is a regex replacement string.

The regular expression operates on the name of the table in the SQL CREATE Statement.
In our example, the regex pattern matches the entire table name.

The SQL file at `src/main/sql/Person-ddl.sql` contains the following text...

```
CREATE TABLE Person (
  given_name VARCHAR(64),
  family_name VARCHAR(64)
);
```

In this CREATE statement, the table name is `Person`.  Thus, based on the regular expression replacement in our configuration, 
the IRI for the generated `NodeShape` will be

```
   https://schema.pearson.com/shapes/PersonShape
```
### Namespace for the RDF Properties assigned to each table column

The `pom.xml` file contains the following element within the Konig plugin configuration...

```
  <propertyNamespace>https://schema.pearson.com/ns/alias/</propertyNamespace>
```

This is the namespace that will be used for RDF Properties associated with each table column.

To see how this property is used, take a look at the generated SHACL description of the table.
For your convenience, here's a copy of the generated SHACL description...

The generated SHACL description of the Person table is shown below.

```
@prefix alias: <https://schema.pearson.com/ns/alias/> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix sh: <http://www.w3.org/ns/shacl#> .
@prefix shape: <https://schema.pearson.com/shapes/> .

shape:PersonShape sh:property  [ 
    sh:path alias:given_name ; 
    sh:minCount 0 ; 
    sh:maxCount 1 ; 
    sh:datatype <http://www.w3.org/2001/XMLSchema#string> ; 
    sh:minLength 0 ; 
    sh:maxLength 64
   ]  ,  [ 
    sh:path alias:family_name ; 
    sh:minCount 0 ; 
    sh:maxCount 1 ; 
    sh:datatype <http://www.w3.org/2001/XMLSchema#string> ; 
    sh:minLength 0 ; 
    sh:maxLength 64 ]  ; 
  a sh:Shape . 
```

Since there are two columns in the `Person` table, we see two `PropertyShape` instances within `shape:PersonShape`.
Each `PropertyShape` has a `PredicatePath` that assigns an RDF Property for the column.  As shown above, the predicates
are `alias:given_name` and `alias:family_name`.

In this example, the `alias` prefix is mapped to the `propertyNamespace` element within the POM.





