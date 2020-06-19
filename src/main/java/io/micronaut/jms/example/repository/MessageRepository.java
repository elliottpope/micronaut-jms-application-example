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
        String queryString = "SELECT message from Message as m WHERE m.message_id = " + id;
        return entityManager.createQuery(queryString, Message.class).getSingleResult();
    }

    @Transactional
    public Message save(Message message) {
        System.err.println("Saving message with ID: " + message);
        entityManager.persist(message);
        System.err.println("Message saved.");
        return findById(message.getMessageID());
    }
}
