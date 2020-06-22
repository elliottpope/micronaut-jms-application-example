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

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@JMSListener("activeMqConnectionFactory")
public class MessageListener {

    private static final Integer MESSAGES_PER_LEVEL = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);

    @Inject
    private MessageRepository repository;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Inject
    private MessageProducer producer;

    @Queue(
            destination = "${example.jms.destination}",
            concurrency = "5-10")
    public void receive(
            String message,
            @Header("X-Message-ID") @Nullable String messageID,
            @Header("X-Parent-ID") @Nullable String parentID,
            @Header("X-Current-Depth") @Nullable Integer currentDepth,
            @Header("X-Max-Depth") @Nullable Integer maxDepth) {
        LOGGER.debug(
                "Received message with ID {} and content {}." +
                        " Current Depth: {}. Parent ID: {}",
                messageID,
                message,
                currentDepth == null ? 0 : currentDepth,
                parentID);

        Message messageToSave = new Message();
        messageToSave.setParentID(parentID);
        messageToSave.setMessageID(messageID);
        messageToSave.setReceived(new Date());
        messageToSave.setSent(new Date());
        messageToSave.setThread(Thread.currentThread().getName());
        if (repository.save(messageToSave) == null) {
            System.err.println("Failed to save message " + message);
        }

        if (currentDepth == null) {
            currentDepth = 0;
        }

        if (maxDepth == null || currentDepth < maxDepth) {
            final int newDepth = currentDepth + 1;
            for (int i = 0; i < MESSAGES_PER_LEVEL; i++) {
                executorService.submit(() -> {
                    UUID id = UUID.randomUUID();
                    LOGGER.debug("Sending message with ID: {} at depth: {}",
                            id,
                            newDepth);
                    producer.sendWithHeaders(
                            id,
                            message,
                            messageID,
                            newDepth,
                            maxDepth == null ? MESSAGES_PER_LEVEL : maxDepth);
                });
            }
        }
    }
}
