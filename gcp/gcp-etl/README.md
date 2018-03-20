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
