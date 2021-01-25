package io.micronaut.jms.example.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.jms.example.model.ObjectMessage;
import io.micronaut.jms.example.producer.MessageProducer;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Controller("/messages")
public class MessageController {

    @Inject
    private MessageProducer producer;

    @Post(value = "/map", produces = MediaType.TEXT_PLAIN, consumes = MediaType.APPLICATION_JSON)
    public String send(Map<String, Object> message) {
        String id = getId();
        producer.send(id, message);
        return id;
    }

    @Post(value = "/string", produces = MediaType.TEXT_PLAIN, consumes = MediaType.APPLICATION_JSON)
    public String send(String message) {
        String id = getId();
        producer.send(id, message);
        return id;
    }

    @Post(value = "/object", produces = MediaType.TEXT_PLAIN, consumes = MediaType.APPLICATION_JSON)
    public String send(ObjectMessage message) {
        String id = getId();
        producer.send(id, message);
        return id;
    }

    @Post(value = "/bytes", produces = MediaType.TEXT_PLAIN)
    public String send() {
        String id = getId();
        byte[] message = id.getBytes();
        producer.send(id, message);
        return id;
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
