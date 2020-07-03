package io.micronaut.jms.example.repository;

import io.micronaut.jms.example.repository.model.Message;
import io.micronaut.transaction.annotation.ReadOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Singleton
public class MessageRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRepository.class);

    @Inject
    private EntityManager entityManager;

    @ReadOnly
    public Message findById(String id) {
        String queryString = "SELECT m from Message as m WHERE m.messageID = '" + id + "'";
        Message message = entityManager.createQuery(queryString, Message.class)
                .getSingleResult();
        LOGGER.debug("Retrieved message from DB: {}", message);
        return message;
    }

    @Transactional
    public Message save(Message message) {
        LOGGER.trace("Saving message with ID: {}", message);
        entityManager.persist(message);
        entityManager.flush();
        LOGGER.trace("Message saved.");
        return findById(message.getMessageID());
    }
}
