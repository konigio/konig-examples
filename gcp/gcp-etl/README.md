# Google Could ETL Demo

An example which shows how to integrate Camel with Google Pubsub notifications for Google Cloud Storage.

# Setup

This demo requires a Google Cloud account with a Pubsub subscription that has been configured to listen to a topic that receives notifications when a changes is made to a GCP bucket.

A topic and subscription need to be created and the topic and subscription name set in the application.properties file.

Additionally, this project requires a google cloud bucket that has been configured to send object changes notifications to the topic.  To configure a bucket to send notifications to a pubsub topic, see https://cloud.google.com/storage/docs/reporting-changes.

For authenticating against Google Cloud one of the following options can be used:

**_Using your default credentials_**

Comment out `credentials.account` and `credentials.key` in application.properties.

**_Using an account and key_**

Set `credentials.account` to the service account email and `credentials.key` to the service account key.

# Run

To run execute the following command:

```bash
$ mvn spring-boot:run
```

# What it does

Spring-boot is used to start an Apache Camel route that 
- listens for notifications on the pubsub topic via the ``pubsub.subscription`` subscription
- filters out messages that do not have eventType `OBJECT_FINALIZE` in their header attributes
- converts the message body to a Java Map
- logs the value of the ``name`` field from the message body
- prints the entire message body to std:out

```java
from("pubsub:{{pubsub.projectId}}:{{pubsub.subscription}}?"
    + "maxMessagesPerPoll={{consumer.maxMessagesPerPoll}}&"
    + "concurrentConsumers={{consumer.concurrentConsumers}}")
    .routeId("fromGooglePubSub")
    .filter(method("pubsubAttributeReader", "getEventType").isEqualTo("OBJECT_FINALIZE"))
    .unmarshal().json(JsonLibrary.Jackson, Map.class)
    .log("FILE ${body[name]}")
    .to("stream:out");
```

note - see https://cloud.google.com/storage/docs/pubsub-notifications for more information on attributes used with Google Cloud Storage Notifications.

# Docker

This project can be used to build docker images for running a containerized version of the spring boot application.

To build a new docker image with maven
```bash
mvn install dockerfile:build
```

running the application with docker:
```bash
docker run konig-examples/gcp-etl
```

Additional documentation for building docker images for running spring boot applications is available at https://spring.io/guides/gs/spring-boot-docker/ .