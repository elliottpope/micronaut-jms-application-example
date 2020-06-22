package io.micronaut.jms.example.producer;

import io.micronaut.context.annotation.Value;
import io.micronaut.jms.model.JMSDestinationType;
import io.micronaut.jms.model.JMSHeaders;
import io.micronaut.jms.model.MessageHeader;
import io.micronaut.jms.pool.JMSConnectionPool;
import io.micronaut.jms.serdes.DefaultSerializerDeserializer;
import io.micronaut.jms.templates.JmsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public class MessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

    @Inject
    @Named("activeMqConnectionFactory")
    private JMSConnectionPool connectionPool;

    @Value("${example.jms.destination}")
    private String destination;

    private final JmsProducer producer = new JmsProducer(JMSDestinationType.QUEUE);

    public <T> void send(UUID id, T message) {
        LOGGER.info("Sent message {} of type {} to queue {} with id {}",
                message,
                message.getClass(),
                destination,
                id);
        producer.send(
                destination,
                message,
                new MessageHeader("X-Message-ID", id.toString()));
    }

    public <T> void sendWithHeaders(UUID id, T message, MessageHeader... headers) {
        LOGGER.info("Sent message {} of type {} to queue {} with id {}",
                message,
                message.getClass(),
                destination,
                id);
        List<MessageHeader> newHeaders =
                Arrays.asList(headers);
        newHeaders.add(new MessageHeader("X-Message-ID", id.toString()));
        producer.send(
                destination,
                message,
                (MessageHeader[]) newHeaders.toArray());
    }

    @PostConstruct
    public void initialize() {
        producer.setConnectionPool(connectionPool);
        producer.setSerializer(new DefaultSerializerDeserializer());
    }
}
