package io.micronaut.jms.example.listener;

import io.micronaut.jms.annotations.Header;
import io.micronaut.jms.annotations.JMSListener;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.jms.example.repository.MessageRepository;
import io.micronaut.jms.example.repository.model.Message;
import io.micronaut.jms.model.JMSHeaders;

import javax.inject.Inject;
import java.util.Date;

@JMSListener("activeMqConnectionFactory")
public class MessageListener {

    @Inject
    private MessageRepository repository;

    @Queue(destination = "${example.jms.destination}")
    public void receive(
            String message,
            @Header("X-Message-ID") String messageID) {
        System.err.printf(
                "Received message with ID %s and content %s.\n",
                messageID,
                message);

        Message messageToSave = new Message();
        messageToSave.setMessageID(messageID);
        messageToSave.setReceived(new Date());
        messageToSave.setSent(new Date());
        if (repository.save(messageToSave) == null) {
            System.err.println("Failed to save message " + message);
        }
    }
}
