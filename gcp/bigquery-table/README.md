# bigquery-table

In this example, we show how you can can generate BigQuery tables definitions
for `Product` and `ProductCategory` entities.

The `Product` table holds records like this one:

```
  {
    "id" : "http://example.com/products/49274803",
  	"name" : "iPhone 7S",
  	"category" : "Electronics",
  	"offers" : [{
  		"price" : 749.0,
  		"priceCurrency" : "USD"
  	}]
  }
```

The `ProductCategory` table holds values like this one:

```
	{
		"id" : "HomeGarden",
		"name" : "Home & Garden"
	}
```

The table schemas are generated from a data model that is defined in  the
[bigquery-table.xlsx](src/bigquery-table.xlsx) spreadsheet.
You can [view this spreadsheet](https://docs.google.com/spreadsheets/d/15hh8U6EVx-zZTPAGC5WqDP2K3mrJRoaHMsssVDYQ0RE/edit?usp=sharing) online in Google Drive.

We'll have more to say about the data model spreadsheet later.  But first, we'll
explain how to run the Maven build, and we'll discuss the artifacts that are
generated.

## Running the Maven build

Execute the following commands to run the Maven build.

```

```


## Output from the Maven Build

If you clone this example from Github and run the Maven build, five different kinds
of artifacts will be generated:

 * Reference Data
 * BigQuery Dataset Definitions
 * BigQuery Schema Definition
 * OWL Ontologies
 * SHACL Shapes

 Here's a listing of the generated artifacts:

![artifacts](images/artifacts.png)

We discuss the generated artifacts in detail below.

### BigQuery Schema Definitions

The BigQuery schemas can be found in the folder at:

```
  target/generated/bigquery/schema
```


Here's a listing of the generated schema definitions:

________________________________________________________________________________
File: `ex.ProductCategory.json`
```json
{
  "schema" : {
    "fields" : [ {
      "mode" : "REQUIRED",
      "name" : "id",
      "type" : "STRING"
    }, {
      "mode" : "NULLABLE",
      "name" : "name",
      "type" : "STRING"
    } ]
  },
  "tableReference" : {
    "datasetId" : "ex",
    "projectId" : "{gcpProjectId}",
    "tableId" : "ProductCategory"
  }
}
```
________________________________________________________________________________
File: `ex.ProductCategory.json`
```json
{
  "schema" : {
    "fields" : [ {
      "mode" : "REQUIRED",
      "name" : "id",
      "type" : "STRING"
    }, {
      "mode" : "NULLABLE",
      "name" : "name",
      "type" : "STRING"
    }, {
      "mode" : "REPEATED",
      "name" : "category",
      "type" : "STRING"
    }, {
      "fields" : [ {
        "mode" : "REQUIRED",
        "name" : "price",
        "type" : "FLOAT"
      }, {
        "mode" : "REQUIRED",
        "name" : "priceCurrency",
        "type" : "STRING"
      } ],
      "mode" : "REPEATED",
      "name" : "offers",
      "type" : "RECORD"
    } ]
  },
  "tableReference" : {
    "datasetId" : "schema",
    "projectId" : "{gcpProjectId}",
    "tableId" : "Product"
  }
}
```
________________________________________________________________________________

These schema definitions have a placeholder for the `projectId` attribute.  The
value of the `projectId` attribute is given as `{gcpProjectId}`.  The curly braces
indicate that the value is a variable. This practice
allows you to substitute different values for the `projectId` depending on the environment
you are working in.  You might have different `projectId` values for development, testing, and
production environments.  When you are ready to deploy the tables to a given environment, just
perform a search-and-replace operation on the `{gcpProjectId}` placeholder.

## The Data Model spreadsheet

The Ontologies tab specifies the following ontologies:

* `Schema.org`:  An ontology developed by Google, Yahoo, Microsoft, and others to describe all kinds of things on the web.  We use the `Product` and `Offer` classes from this ontology.

* `Shapes`: A custom ontology that contains our [SHACL Shape](https://www.w3.org/TR/shacl/) definitions.

* `Product Categories`: A taxonomy of product Categories

* `Example Ontology`:  A custom ontology that contains the definition of the `ProductCategory` class.

There are three shapes in this example:

* `BqProductShape`
* `BqOfferShape`
* `BqProductCategoryShape`


The [Property Constraints](https://docs.google.com/spreadsheets/d/15hh8U6EVx-zZTPAGC5WqDP2K3mrJRoaHMsssVDYQ0RE/edit#gid=1522633637) tab declares the properties of the `Product` and `Offer` entities. Notice
that a `Product` may have multiple offers, and those offers are embedded within the
`Product` description.

The [pom.xml](pom.xml) includes the Konig Schema Generator plugin with the following
configuration:

```
<configuration>
  <defaults>
    <rootDir>${basedir}/target/generated</rootDir>
  </defaults>
  <workbook>
    <workbookFile>${basedir}/src/bigquery-table.xlsx</workbookFile>
  </workbook>
  <googleCloudPlatform>
    <enumShapeNameTemplate>http://example.com/shapes/Bq{targetClass.localName}Shape</enumShapeNameTemplate>
  </googleCloudPlatform>
</configuration>
```
