package io.konig.examples.gcp.etl.app;

import org.apache.camel.CamelContext;
import org.apache.camel.component.google.pubsub.GooglePubsubComponent;
import org.apache.camel.component.google.pubsub.GooglePubsubConnectionFactory;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author szednik
 */

@Configuration
public class DemoApplicationConfig {

    @Autowired
    private CamelContext context;

    @Value("${credentials.account:}")
    private String credentialsAccount;

    @Value("${credentials.key:}")
    private String credentialsKey;

    @Bean
    CamelContextConfiguration contextConfiguration() {

        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                GooglePubsubComponent googlePubsub = createComponent();
                camelContext.addComponent("pubsub", googlePubsub);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {

            }
        };
    }

    private GooglePubsubComponent createComponent() {
        GooglePubsubComponent component = new GooglePubsubComponent();
        GooglePubsubConnectionFactory connectionFactory = createConnectionFactory();
        component.setConnectionFactory(connectionFactory);
        return component;
    }

    private GooglePubsubConnectionFactory createConnectionFactory() {
        GooglePubsubConnectionFactory connectionFactory = new GooglePubsubConnectionFactory();

        if(!credentialsAccount.isEmpty()) {
            connectionFactory.setServiceAccount(credentialsAccount);
        }

        if(!credentialsKey.isEmpty()) {
            connectionFactory.setServiceAccountKey(credentialsKey);
        }

        return connectionFactory;
    }
}
