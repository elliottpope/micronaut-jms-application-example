package io.micronaut.jms.example.listener;

import io.micronaut.jms.annotations.Header;
import io.micronaut.jms.annotations.JMSListener;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.jms.model.JMSHeaders;

@JMSListener("activeMqConnectionFactory")
public class MessageListener {
    @Queue(destination = "${example.jms.destination}")
    public void receive(
            String message,
            @Header(JMSHeaders.JMS_MESSAGE_ID) String jmsMessageID) {
        System.err.println("Message received.");
        System.err.printf(
                "Received message with ID %s and content %s.",
                jmsMessageID,
                message);
    }
}
