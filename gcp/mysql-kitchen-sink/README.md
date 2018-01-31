This example illustrates the ability to generate Google Cloud SQL
resources from a semantic model.

The semantic model in this example is somewhat contrived. There is a single [Node Shape](https://www.w3.org/TR/shacl/#node-shapes) with a separate property for each kind of data type that is supported.  

The spreadsheet that describes the semantic model is found at
[src/sql-kitchen-sink.xlsx](src/sql-kitchen-sink), and it is also
available online in [Google Drive](https://docs.google.com/spreadsheets/d/1BXylu2uDJaVoz9dTXR1_95rX8reI-huurHCznMpIxOg/edit#gid=1522633637).

## How to Describe Cloud SQL Resources

There are three kinds of Cloud SQL resources:

1. Instances
2. Databases
3. Tables

### Modeling with a workbook

#### Cloud SQL Instances

Your workbook should contain a sheet that describes the Cloud SQL
Instances in your project.

This sheet should have the following columns:

* `Instance Name`
* `Instance Type`
* `Backend Type`
* `Region`
* `Database Version`

Consult the [Cloud SQL API documentation](https://cloud.google.com/sql/docs/mysql/admin-api/v1beta4/instances#resource)
for information about the allowed values for these properties.

You can view the [Cloud SQL Instances](https://docs.google.com/spreadsheets/d/1BXylu2uDJaVoz9dTXR1_95rX8reI-huurHCznMpIxOg/edit#gid=1185516053)
sheet for this example in Google Drive.

As a best practice, you should specify the name of the default Instance
in the `Settings` sheet using a property named `gcpSqlInstanceId`.

If you don't specify a default, you will need to provide the Instance
name as a Data Source parameter for each Node Shape that generates a table.  We discuss Data Source parameters below.

You can view the [Settings](https://docs.google.com/spreadsheets/d/1BXylu2uDJaVoz9dTXR1_95rX8reI-huurHCznMpIxOg/edit#gid=1185516053) sheet for this example in Google Drive.

#### Cloud SQL Databases

You do not need to model Cloud SQL Databases explicitly. When describing
a Cloud SQL Table, you merely need to provide a name for the Database
that contains the table, and an appropriate Database descriptor will
be generated automatically.

As a best practice, you should specify the name of the default Database
in the `Settings` sheet using a property named `gcpSqlDatabaseId`.  If you don't specify a default, you will need to provide the Database name as Data Source parameter for each Node Shape that generates a table.

#### Cloud SQL Tables

Tables are generated from Node Shapes that declare a `Data Source`
based on the `GoogleCloudSqlTable` template.  This template accepts the
following parameters:

| Parameter Name | Default Value |
|----------------|----------------------------------------|
| gcpProjectId   | None.  This value is typically left as a variable whose value is supplied at deployment time. |
| gcpSqlInstanceId | User provided on the `Settings` sheet       |
| gcpDatabaseId    | User provided on the `Settings' sheet       |
| gcpSqlTableName  | The local name of the associated Node Shape |

You can see how the `Data Source` is declared on the [Shapes](https://docs.google.com/spreadsheets/d/1BXylu2uDJaVoz9dTXR1_95rX8reI-huurHCznMpIxOg/edit#gid=1535616737)
sheet for this example.


### Project Structure

When generating resources from a spreadsheet, the output can be found
in the following directory structure.

```
_ target
└──_generated
   ├──_gcp
   |  ├── database
   |  ├── instances
   |  └── tables
   |
   └──_rdf
      ├── gcp
      ├── owl
      └── shapes
```


### RDF Descriptions

The RDF descriptions of the Cloud SQL resources can be found under
the `rdf` directory.  

This example produces the following RDF files (among others):

#### File: `target/generated/rdf/shapes/shape_ThingShape.ttl`
```
@prefix as: <http://www.w3.org/ns/activitystreams#> .
@prefix ex: <http://example.com/core/> .
@prefix gcp: <http://www.konig.io/ns/gcp/> .
@prefix konig: <http://www.konig.io/ns/core/> .
@prefix prov: <http://www.w3.org/ns/prov#> .
@prefix schema: <http://schema.org/> .
@prefix sh: <http://www.w3.org/ns/shacl#> .
@prefix shape: <http://example.com/shapes/> .
@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .

shape:ThingShape a sh:Shape ;
	prov:wasGeneratedBy <http://www.konig.io/activity/9Hca3YQcdzcAAAFhSL4HGw> ;
	sh:targetClass schema:Thing ;
	sh:property  [
		sh:path ex:booleanProperty ;
		sh:datatype xsd:boolean ;
		sh:minCount 0 ;
		sh:maxCount 1
	 ]  ,  [
		sh:path ex:bitProperty ;
		sh:datatype xsd:int ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:minInclusive 0 ;
		sh:maxInclusive 8
	 ]  ,  [
		sh:path ex:unsignedTinyInt ;
		sh:datatype xsd:int ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:minInclusive 0 ;
		sh:maxInclusive 255
	 ]  ,  [
		sh:path ex:signedTinyInt ;
		sh:datatype xsd:int ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:minInclusive -128 ;
		sh:maxInclusive 127
	 ]  ,  [
		sh:path ex:unsignedSmallInt ;
		sh:datatype xsd:int ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:minInclusive 0 ;
		sh:maxInclusive 65535
	 ]  ,  [
		sh:path ex:signedSmallInt ;
		sh:datatype xsd:int ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:minInclusive -32768 ;
		sh:maxInclusive 32767
	 ]  ,  [
		sh:path ex:unsignedMediumInt ;
		sh:datatype xsd:int ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:minInclusive 0 ;
		sh:maxInclusive 16777215
	 ]  ,  [
		sh:path ex:signedMediumInt ;
		sh:datatype xsd:int ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:minInclusive -8388608 ;
		sh:maxInclusive 8388607
	 ]  ,  [
		sh:path ex:unsignedInt ;
		sh:datatype xsd:int ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:minInclusive 0 ;
		sh:maxInclusive "4294967295"^^xsd:long
	 ]  ,  [
		sh:path ex:signedInt ;
		sh:datatype xsd:int ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:minInclusive "-2147483648"^^xsd:long ;
		sh:maxInclusive "2147483647"^^xsd:long
	 ]  ,  [
		sh:path ex:date ;
		sh:datatype xsd:date ;
		sh:minCount 0 ;
		sh:maxCount 1
	 ]  ,  [
		sh:path ex:dateTime ;
		sh:datatype xsd:dateTime ;
		sh:minCount 0 ;
		sh:maxCount 1
	 ]  ,  [
		sh:path ex:text ;
		sh:datatype xsd:string ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:maxLength 100000
	 ]  ,  [
		sh:path ex:char ;
		sh:datatype xsd:string ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:minLength 32 ;
		sh:maxLength 32
	 ]  ,  [
		sh:path ex:varchar ;
		sh:datatype xsd:string ;
		sh:minCount 0 ;
		sh:maxCount 1 ;
		sh:maxLength 200
	 ]  ,  [
		sh:path ex:float ;
		sh:datatype xsd:float ;
		sh:minCount 0 ;
		sh:maxCount 1
	 ]  ,  [
		sh:path ex:double ;
		sh:datatype xsd:double ;
		sh:minCount 0 ;
		sh:maxCount 1 ]  ;
	konig:shapeDataSource <https://www.googleapis.com/sql/v1beta4/projects/${gcpProjectId}/instances/kitchen/databases/sink/tables/ThingShape> .

<http://www.konig.io/activity/9Hca3YQcdzcAAAFhSL4HGw> a konig:LoadModelFromSpreadsheet ;
	as:endTime "2018-01-30T15:24:28.832-05:00"^^xsd:dateTime .

<https://www.googleapis.com/sql/v1beta4/projects/${gcpProjectId}/instances/kitchen/databases/sink/tables/ThingShape> a konig:GoogleCloudSqlTable , konig:DataSource ;
	gcp:instance "kitchen" ;
	gcp:database "sink" ;
	gcp:name "ThingShape" .
```
-----------------------------------------------------------------
#### File: `target/generated/rdf/gcp/kitchen.ttl`

```
@prefix gcp: <http://www.konig.io/ns/gcp/> .

<https://www.googleapis.com/sql/v1beta4/projects/${gcpProjectId}/instances/kitchen> a gcp:GoogleCloudSqlInstance ;
	gcp:name "kitchen" ;
	gcp:backendType gcp:SECOND_GEN ;
	gcp:instanceType gcp:CLOUD_SQL_INSTANCE ;
	gcp:databaseVersion gcp:MYSQL_5_7 ;
	gcp:region gcp:us-central .
```  

### GCP JSON Descriptions
The Google cloud Platform uses JSON descriptions for its resources.

This example generates the following files.

--------------------------------------------------
#### File: `target/generated/gcp/cloudsql/instances/kitchen.json`
```
{
  "selfLink" : "https://www.googleapis.com/sql/v1beta4/projects/${gcpProjectId}/instances/kitchen",
  "name" : "kitchen",
  "backendType" : "SECOND_GEN",
  "databaseVersion" : "MYSQL_5_7",
  "instanceType" : "CLOUD_SQL_INSTANCE",
  "region" : "us-central"
}
```
-------------------------------------------------------------------
#### File: `target/generated/gcp/cloudsql/databases/kitchen_sink.json`
```
{
  "selfLink" : "https://www.googleapis.com/sql/v1beta4/projects/${gcpProjectId}/instances/kitchen/databases/sink",
  "name" : "sink",
  "instance" : "kitchen"
}
```
-------------------------------------------------------------------
#### File: `target/generated/gcp/cloudsql/tables/kitchen_sink_ThingShape.json`
```
{
  "selfLink" : "https://www.googleapis.com/sql/v1beta4/projects/${gcpProjectId}/instances/kitchen/databases/sink/tables/ThingShape",
  "instance" : "kitchen",
  "database" : "sink",
  "name" : "ThingShape",
  "ddlFile" : "kitchen_sink_ThingShape.sql"
}
```
-------------------------------------------------------------------
#### File: `target/generated/gcp/cloudsql/tables/kitchen_sink_ThingShape.sql`
```
CREATE TABLE IF NOT EXISTS ThingShape (
   booleanProperty BOOLEAN,
   bitProperty BIT UNSIGNED,
   unsignedTinyInt TINYINT UNSIGNED,
   signedTinyInt TINYINT,
   unsignedSmallInt SMALLINT UNSIGNED,
   signedSmallInt SMALLINT,
   unsignedMediumInt MEDIUMINT UNSIGNED,
   signedMediumInt MEDIUMINT,
   unsignedInt INT UNSIGNED,
   signedInt INT,
   date DATE,
   dateTime DATETIME,
   text TEXT(100000),
   char CHAR(32),
   varchar VARCHAR(200),
   float FLOAT,
   double DOUBLE
)
```
