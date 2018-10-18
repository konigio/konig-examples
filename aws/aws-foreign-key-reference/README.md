This example shows how to describe a foreign key reference using a semantic workbook.

The workbook used in this example is [foreign-key-reference](https://docs.google.com/spreadsheets/d/1mMZ2LzpaZ77M7PKMWBWSD8ggNzTRlBwXg082hg3zSKo/edit?usp=sharing), 
found in the examples folder of the Konig Google Drive.

In the workbook, we have two shapes with property constraints defined as follows:

|Shape Id | Property Id | Value Type | Min Count | Max Count | Stereotype | Max Length | References Shape |
|---------|-------------|------------|-----------|-----------|------------|------------|------------------|
|shape:PersonShape |alias:KEY|xsd:long|1|1|konig:syntheticKey|||
|shape:PersonShape |alias:GIVEN_NAME|xsd:string|0|1||32||
|shape:PersonShape |alias:FAMILY_NAME|xsd:string|0|1||32||
|shape:PersonShape |alias:WORKS_FOR|xsd:long|1|1|||shape:OrganizationShape|
|shape:OrganizationShape |alias:KEY|xsd:long|1|1|konig:syntheticKey|||
|shape:OrganizationShape |alias:NAME|xsd:string|0|1||64||


Notice that the `WORKS_FOR` property has the value `shape:OrganizationShape` in the `References Shape` column.  This is how you tell Konig to treat the `WORKS_FOR` column as a foreign key reference.

Here's the DDL that gets generated...

```sql
CREATE TABLE IF NOT EXISTS example.Person (
   `KEY` INT NOT NULL AUTO_INCREMENT, 
   GIVEN_NAME VARCHAR(32), 
   FAMILY_NAME VARCHAR(32) NOT NULL, 
   WORKS_FOR INT, 
   PRIMARY KEY (`KEY`),

   FOREIGN KEY (WORKS_FOR)
      REFERENCES Organization(`KEY`)
);
```

To see this project in action, run 

```
mvn clean install
```

and then look for the output in 

```
target/generated/aws/aurora
```