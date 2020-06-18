package io.micronaut.jms.example.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.jms.example.model.ObjectMessage;
import io.micronaut.jms.example.producer.MessageProducer;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

@Controller("/messages")
public class MessageController {

    @Inject
    private MessageProducer producer;

    @Post(value = "/map", produces = MediaType.TEXT_PLAIN, consumes = MediaType.APPLICATION_JSON)
    public String send(Map<String, Object> message) {
        UUID id = UUID.randomUUID();
        producer.send(id, message);
        return id.toString();
    }

    @Post(value = "/string", produces = MediaType.TEXT_PLAIN, consumes = MediaType.APPLICATION_JSON)
    public String send(String message) {
        UUID id = UUID.randomUUID();
        producer.send(id, message);
        return id.toString();
    }

    @Post(value = "/object", produces = MediaType.TEXT_PLAIN, consumes = MediaType.APPLICATION_JSON)
    public String send(ObjectMessage message) {
        UUID id = UUID.randomUUID();
        producer.send(id, message);
        return id.toString();
    }

    @Post(value = "/bytes/{name}", produces = MediaType.TEXT_PLAIN)
    public String send() {
        UUID id = UUID.randomUUID();
        byte[] message = id.toString().getBytes();
        producer.send(id, message);
        return id.toString();
    }
}
