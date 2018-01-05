# Model Merge

This example shows you how to use the *Konig Model Merge* Maven Plugin.
This plugin takes as input OWL and SHACL models from multiple projects 
and merges them into a single set of source files -- removing any redundancy.

The primary use case for merging models is to create a single project that can
be used to publish a global data catalog containing ontologies and shapes from 
the various contributing projects.

Over time,  we'll add the ability to perform validation checks to ensure 
model consistency across the contributing projects.

Here's a summary of the maven projects in this example.

| Folder             | Description |
|--------------------|-------------|
| merge-organization | A contributing project that generates the RDF Model and Data Catalog for Organization entities |
| merge-person       | A contributing project that generates the RDF Model and Data Catalog for Person entities |
| merge-product      | A contributing project that generates the RDF Model and Data Catalog for Person entities |
| merge-model        | The main project that is responsible for merging the contributing projects into a combined model |

The contributing projects follow the pattern of a multi-project.

The `merge-model` project contains a `pom.xml` with the following plugin configuration:

```
<plugin>
	<groupId>io.konig</groupId>
	<artifactId>konig-model-merge-maven-plugin</artifactId>
	<version>${konig.version}</version>
	<configuration>
		<artifactItems>
			<artifactItem>
				<groupId>com.example</groupId>
				<artifactId>model-merge-organization-rdf-model</artifactId>
				<version>1.0.0</version>
			</artifactItem>
			<artifactItem>
				<groupId>com.example</groupId>
				<artifactId>model-merge-person-rdf-model</artifactId>
				<version>1.0.0</version>
			</artifactItem>
			<artifactItem>
				<groupId>com.example</groupId>
				<artifactId>model-merge-product-rdf-model</artifactId>
				<version>1.0.0</version>
			</artifactItem>
		</artifactItems>
	</configuration>
	<executions>
		<execution>
			<phase>generate-sources</phase>
			<goals>
				<goal>merge</goal>
			</goals>
		</execution>
	</executions>
</plugin>
```

The key part of the configuration is the list of `<artifactItem>` elements, 
each of which identifies an artifact from a Maven repository (local or remote).
These are zip-file artifacts that contain OWL and SHACL documents, produced by 
the contributed projects.

The following table describes the full set of available configuration parameters.

| Parameter Name                        | Description |
|---------------------------------------|-------------|
| artifactItems                         | A list of artifact descriptors |
| artifactItems/artifactItem            | A single artifact descriptor within the list |
| artifactItems/artifactItem/groupId    | The Group ID of the artifact |
| artifactItems/artifactItem/artifactId | The identifier for the artifact |
| artifactItems/artifactItem/version    | The version of the artifact |
| unpackDir                             | The directory to which artifacts will be fetched and unpacked for processing. <br> Default: ${project.basedir}/target/unpack |
| outDir                                | The directory that will hold the merged model produced by the plugin. <br> Default: ${project.basedir}/target/model/merged |


The Konig Merge plugin performs the following actions:

1. Fetch the zip-files from the Maven Repository.
2. Expand the zip-files into the directory specified by the `unpackDir`
3. Merge the models given by the supplied artifacts and store the result in `outDir`

To build this example, run the following command in the same directory as this README file:

```
mvn install
```

The local pom.xml file will build the contributing maven projects and then build the `model-merge` project.

When using the *Konig Model Merge* plugin, you don't necessarily have to have a parent POM that builds everything.  
It's more common for the contributing projects to be completely independent.  In that case, you can simply run the model merge project periodically rather than part of some master build process.
