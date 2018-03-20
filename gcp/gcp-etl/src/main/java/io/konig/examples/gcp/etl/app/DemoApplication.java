package io.konig.examples.gcp.etl.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author szednik
 */

@SpringBootApplication
@ComponentScan(basePackages = "io.konig.examples.gcp.etl")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
