package io.micronaut.jms.example.producer;

import io.micronaut.jms.annotations.JMSProducer;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.messaging.annotation.Body;
import io.micronaut.messaging.annotation.Header;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static io.micronaut.jms.activemq.classic.configuration.ActiveMqClassicConfiguration.CONNECTION_FACTORY_BEAN_NAME;

@JMSProducer(CONNECTION_FACTORY_BEAN_NAME)
public interface MessageProducer {

    default <T> void send(@NotNull @Header("X-Message-ID") String uuid, @Body T message) {
        send(uuid, LocalDateTime.now().toString(), message);
    }

    @Queue(value = "${example.jms.destination}")
    <T> void send(@NotNull @Header("X-Message-ID") String uuid, @NotNull @Header("X-Sent") String sent, @Body T message);

    @Queue(value = "${example.jms.destination}")
    <T> void sendWithParent(
            @NotNull @Header("X-Message-ID") String uuid,
            @Nullable @Header("X-Parent-Message-ID") String parentId,
            @Nullable @Header("X-Current-Depth") Integer currentDepth,
            @Nullable @Header("X-Max-Depth") Integer maxDepth,
            @Body T message);
}
