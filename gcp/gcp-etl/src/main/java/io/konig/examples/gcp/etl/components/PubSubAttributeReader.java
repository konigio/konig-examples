package io.konig.examples.gcp.etl.components;

import org.apache.camel.Exchange;
import org.apache.camel.component.google.pubsub.GooglePubsubConstants;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author szednik
 */

@Component("pubsubAttributeReader")
public class PubSubAttributeReader {

    public Map getAttributes(Exchange exchange) {
        return exchange.getIn().getHeader(GooglePubsubConstants.ATTRIBUTES, Map.class);
    }

    public String getEventType(Exchange exchange) {
        Map attributes = exchange.getIn().getHeader(GooglePubsubConstants.ATTRIBUTES, Map.class);
        return attributes.get("eventType").toString();
    }
}
