package io.micronaut.jms.example.repository;

import io.micronaut.jms.example.repository.model.Message;
import io.micronaut.transaction.annotation.ReadOnly;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Singleton
public class MessageRepository {
    @Inject
    private EntityManager entityManager;

    @ReadOnly
    public Message findById(String id) {
        String queryString = "SELECT m from Message as m WHERE m.messageID = '" + id + "'";
        Message message = entityManager.createQuery(queryString, Message.class)
                .getSingleResult();
        System.err.println("Retrieved message from DB: " + message);
        return message;
    }

    @Transactional
    public Message save(Message message) {
        System.err.println("Saving message with ID: " + message);
        entityManager.persist(message);
        entityManager.flush();
        System.err.println("Message saved.");
        return findById(message.getMessageID());
    }
}
