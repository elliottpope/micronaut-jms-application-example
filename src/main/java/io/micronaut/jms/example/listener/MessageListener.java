package io.micronaut.jms.example.listener;

import io.micronaut.http.annotation.Head;
import io.micronaut.jms.annotations.Header;
import io.micronaut.jms.annotations.JMSListener;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.jms.example.producer.MessageProducer;
import io.micronaut.jms.example.repository.MessageRepository;
import io.micronaut.jms.example.repository.model.Message;
import io.micronaut.jms.model.JMSHeaders;
import io.micronaut.jms.model.MessageHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@JMSListener("activeMqConnectionFactory")
public class MessageListener {

    private static final Integer MESSAGES_PER_LEVEL = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    @Inject
    private MessageRepository repository;

    @Inject
    private ExecutorService executorService;

    @Inject
    private MessageProducer producer;

    @Queue(destination = "${example.jms.destination}")
    public void receive(
            String message,
            @Header("X-Message-ID") String messageID,
            @Header("X-Parent-ID") String parentID,
            @Header("X-Current-Depth") Integer currentDepth,
            @Header("X-Max-Depth") Integer maxDepth) {
        LOGGER.info(
                "Received message with ID {} and content {}.",
                messageID,
                message);

        Message messageToSave = new Message();
        messageToSave.setParentID(parentID);
        messageToSave.setMessageID(messageID);
        messageToSave.setReceived(new Date());
        messageToSave.setSent(new Date());
        if (repository.save(messageToSave) == null) {
            System.err.println("Failed to save message " + message);
        }

        if (currentDepth < maxDepth) {
            final int newDepth = currentDepth + 1;
            for (int i = 0; i < MESSAGES_PER_LEVEL; i++) {
                executorService.submit(() -> {
                    producer.sendWithHeaders(
                            UUID.randomUUID(),
                            message,
                            new MessageHeader("X-Parent-ID", messageID),
                            new MessageHeader("X-Current-Depth", Integer.toString(newDepth)),
                            new MessageHeader("X-Max-Depth", maxDepth.toString()));
                });
            }
        }
    }
}
