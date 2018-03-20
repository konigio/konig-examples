package io.konig.examples.gcp.etl.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

/**
 * @author szednik
 */
@Component
public class EtlController extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("pubsub:{{pubsub.projectId}}:{{pubsub.subscription}}?"
                + "maxMessagesPerPoll={{consumer.maxMessagesPerPoll}}&"
                + "concurrentConsumers={{consumer.concurrentConsumers}}")
                .routeId("fromGooglePubSub")
                .filter(method("pubsubAttributeReader", "getEventType").isEqualTo("OBJECT_FINALIZE"))
                .unmarshal().json(JsonLibrary.Jackson)
                .log("FILE ${body[name]}")
                .to("stream:out");

    }
}
