package io.micronaut.jms.example.producer;

import io.micronaut.jms.annotations.Header;
import io.micronaut.jms.annotations.JMSProducer;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.jms.model.JMSHeaders;

import java.util.UUID;

@JMSProducer("activeMqConnectionFactory")
public interface MessageProducer {

    default <T> void send(UUID id, T message) {
        send(id, message, id.toString());
    }

    @Queue(
            destination = "${example.jms.destination}")
    <T> void send(@Header(JMSHeaders.JMS_MESSAGE_ID) UUID id, T message, @Header("X-Message-ID") String messageId);

    default <T> void sendWithHeaders(
            UUID id,
            T message,
            String parentId,
            Integer currentDepth,
            Integer maxDepth) {
        sendWithHeaders(id, message, id.toString(), parentId, currentDepth, maxDepth);
    }

    @Queue(
            destination = "${example.jms.destination}",
            transacted = true)
    <T> void sendWithHeaders(
            @Header(JMSHeaders.JMS_MESSAGE_ID) UUID id,
            T message,
            @Header("X-Message-ID") String messageId,
            @Header("X-Parent-ID") String parentId,
            @Header("X-Current-Depth") Integer currentDepth,
            @Header("X-Max-Depth") Integer maxDepth);
}
