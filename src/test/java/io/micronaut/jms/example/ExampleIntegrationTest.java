package io.micronaut.jms.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;

import javax.inject.Inject;
import java.util.Collections;

@MicronautTest
public class ExampleIntegrationTest {

    @Inject
    @Client("/")
    private RxHttpClient client;

//    @Test
    public void testStringEndpoint() {
        client.exchange(HttpRequest.POST("/messages/string", Collections.singletonMap("message", "test")), String.class)
                .blockingFirst();
    }
}
