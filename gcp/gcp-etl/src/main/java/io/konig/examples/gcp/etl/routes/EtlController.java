package io.konig.examples.gcp.etl.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author szednik
 */
@Component
public class EtlController extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // listen to events on pubsub subscription {pubsub.projectId}:{pubsub.subsubscription}
        from("pubsub:{{pubsub.projectId}}:{{pubsub.subscription}}?"
                + "maxMessagesPerPoll={{consumer.maxMessagesPerPoll}}&"
                + "concurrentConsumers={{consumer.concurrentConsumers}}")

                // add a route identifier
                .routeId("PersonETLRoute")

                // add the eventType to the message header
                .setHeader("eventType", method("pubsubAttributeReader", "getEventType"))

                // filter events not describing the successful write of a file
                .filter(simple("${header.eventType} == 'OBJECT_FINALIZE'"))

                // convert message payload into a Java Map
                .unmarshal().json(JsonLibrary.Jackson, Map.class)

                // add the filename added to the bucket to the message header
                .setHeader("datafile", simple("${body[name]}"))

                // TODO load extract datafile CSV into bigquery staging table
                // TODO execute transforms

                // log the extract datafile name
                .log("${header.datafile}")

                // write the message payload (the notification from the S3 bucket) to standard out
                .to("stream:out");

    }
}
