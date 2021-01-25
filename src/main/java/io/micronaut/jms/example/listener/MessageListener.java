package io.micronaut.jms.example.listener;

import io.micronaut.jms.annotations.JMSListener;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.jms.example.producer.MessageProducer;
import io.micronaut.jms.example.repository.MessageRepository;
import io.micronaut.messaging.annotation.Body;
import io.micronaut.messaging.annotation.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

@JMSListener("activeMqConnectionFactory")
public class MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    private final MessageRepository repository;
    private final MessageProducer producer;

    @Inject
    public MessageListener(MessageRepository repository, MessageProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    @Queue(value = "${example.jms.destination}", concurrency = "1-1")
    public void receive(
            @Body String message,
            @Nullable @Header("X-Message-ID") String messageID,
            @Nullable @Header("X-Parent-Message-ID") String parentMessageID,
            @Nullable @Header("X-Sent") String sent,
            @Nullable @Header("X-Current-Depth") Integer depth,
            @Nullable @Header("X-Max-Depth") Integer maxDepth) {

        throw new RuntimeException("This is a loud exception!!");

        /*LOGGER.info(
                "Received message with ID {} and content {} sent at {}",
                messageID,
                message,
                sent);

        Message messageToSave = new Message();
        messageToSave.setMessageID(messageID);
        messageToSave.setParentID(parentMessageID);
        messageToSave.setReceived(new Date());
        messageToSave.setSent(
                sent == null ? new Date() :
                Date.from(
                LocalDateTime.parse(sent).toInstant(
                        ZoneOffset.of(ZoneOffset.systemDefault().getId()))));
        if (repository.save(messageToSave) == null) {
            LOGGER.error("Failed to save message {}", message);
        }

        if (depth == null) {
            depth = 1;
        }

        if (maxDepth == null) {
            maxDepth = 10;
        }

        if (depth < maxDepth) {
            String newId = UUID.randomUUID().toString();
            LOGGER.info("Sending message {} with ID {} and parent ID {}", message, newId, messageID);
            producer.sendWithParent(newId, messageID, depth + 1, maxDepth, message);
        }*/
    }
}
