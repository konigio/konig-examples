# alpha-project 


## The Semantic Model

The semantic model is defined in the alpha-project which can be found at [src/multi-project.xlsx](src/aws-etl-alpha.xlsx).

## The Master POM

The Master POM is a Maven POM file that controls the generation of the child project.
You can find it at [pom.xml](pom.xml).

The key element of this POM file is the Konig Schema generator plugin, which is configured as follows:

```
<plugin>
	<groupId>io.konig</groupId>
	<artifactId>konig-schemagen-maven-plugin</artifactId>
	<version>${konig.version}</version>
	<configuration>
		<multiProject>
		
			<baseDir>${basedir}/../demo</baseDir>
			<groupId>com.example</groupId>
			<artifactId>demo</artifactId>
			<version>1.0.0</version>
			<name>Demo</name>
			<konigVersion>${konig.version}</konigVersion>
			
			<workbook>
				<workbookFile>${basedir}/src/aws-etl-alpha.xlsx</workbookFile>
			</workbook>
			<jsonSchema>
	  		<uriTemplate>http://example.com/json-schema/{shapeLocalName}</uriTemplate>
			</jsonSchema>
			<amazonWebServices />
		</multiProject>
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
### Install the alpha-project

```
  mvn install
```

### Child Projects

- demo-rdf-model
- demo-aws-model

In addition there is a parent project that ties them all together...

- demo-parent

```
  mvn install
```
We discuss these projects in more detail below.

### RDF Model

RDF Graph that describes the dependency relationships between these shapes

By default, this output directory shall be located at target/generated/rdf/shape-dependencies

The Turtle file for a given target shape shall have a name of the form:

```
  {shapeNamespacePrefix}_{shapeLocalName}.ttl
```

### AWS Model

The AWS Model will generate the below resources in the output directory target/generated/aws

1. CloudFormation Template
	
	The stacks which includes the below
	
	VPC
	Subnet
	ECSCluster
	LoadBalancer
	RDS DBCluster
	RDS DBInstance
	S3 Bucket
	SNS Topic
	SQS Queue
	ECS TaskDefinition
	ECS	Service 
	
	By default, this output directory shall be located at target/generated/aws/cloudformationtemplate
	
	```
  	{stackName}_template.yml
	```
	
2. Aurora Table Schema
	
	The schema will be generated for shapes which has the Datasource as AwsAurora(awsAuroraHost : {HostName})

	By default, this output directory shall be located at target/generated/aws/tables
	
	```
  	{hostName}_{schemaName}_{shapeLocalName}.sql
	```
	
	Metafile will be generated in the same location to have the details of the host name, schema name, table name and reference to the generated SQL file.
	
	```
  	{hostName}_{schemaName}_{shapeLocalName}.json
	```
  
  	Name of the table will be 
  	
  	```
  	{shapeLocalName}
	```
 
3. Camel Routes
 
	The Camel routes will be generated as XML for each routes
	
	By default, this output directory shall be located at target/generated/aws/camel-etl
	
	```
  	Route{shapeLocalName}.sql
	```
	
4. Aurora Transform
 
	The transform will be generated based on the sematic data model
	
	By default, this output directory shall be located at target/generated/aws/aurora/transform
	
	```
  	{schemaName}_{shapeLocalName}.sql
	```
	
5. S3 Bucket 	
	
	The S3 bucket resources will also generate the notification configuration like SNS and SQS 
	
	By default the eventType will be s3:ObjectCreated:*
		
	By default, this output directory shall be located at target/generated/aws/s3buckets
	
	```
  	{shapeLocalName}.json
	```
	
	The bucket name will be like the below and ${environmentName} will be pass as System property at runtime
	
	```
  	${environmentName}-{shapeLocalName}
	```
	
	The SNS and SQS name will be
	
	```
  	{shapeLocalName}_CreateEvent
	```

6. Groovy Script
 
 	The groovy script will be generated to deploy the below resources into the cloud
 	
 	By default, this output directory shall be located at target/generated/aws/scripts
	
	```
  	deploy.groovy
	```
	
 	Aws Aurora Table
 	Aws Sns Topic
 	Aws Sqs Queue
 	Aws S3 Bucket
 	
### Docker Images
For each routes the demo-parent will generate separate maven project
	```
  	etl-{shapeLocalName}
	```	
	
### How to deploy

1. To push the base image to AWS ECR
	
 	Module : konig-docker-aws-etl-base 
 	
 	```
 	mvn docker:build --DenvirontmentVariables
 	```
 	
 	```
 	mvn io.konig:konig-aws-ecs-image-push-plugin:0.0.1-SNAPSHOT:push 
 	```
 	
 It will generate repository name konig-docker-aws-etl-base with image tag as latest
 	
 2. To push the child image to AWS ECR
 	
 As mentioned in the "Docker Images" for each routes the demo-parent will generate separate maven project.
 	
 	```
 	mvn docker:build
 	```
 	
 	```
 	mvn io.konig:konig-aws-ecs-image-push-plugin:0.0.1-SNAPSHOT:push 
 	```
 	
 3. Run the groovy script in demo-aws-model to deploy the cloud formation template and deploy the tables to the aws aurora
 	
 Configure the property file either in System Enviroment as KONIG_DEPLOY_CONFIG or System property as 'konig.deploy.config' 
 	
 	```
 	groovy:execute
 	```
 	
### Known Errors
 
 Still the CloudFormation failed to run the ECS Services
 	
 Check the logs in the below
 	
 1. Please login in to the AWS Cloud
 2. Search for ECS and Click the Clusters
 3. Select the create cluster and click the created service 
 4. Click the Events tab to the logs
 	
 	Error : service {serviceName} (port 80) is unhealthy in target-group {targetGroupName} due to (reason Request timed out).
 	
 Run the images in local
 	
 	```
 	docker run ${aws-account-id}.dkr.ecr.${aws-region}.amazonaws.com/{repositoryName}:{imageTag}
 	```
 	
The repository name will the shape name and image tag will be "latest" as default
	
	
