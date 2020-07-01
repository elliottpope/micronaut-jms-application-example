package io.micronaut.jms.example.repository.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "messages", schema = "messages")
@Data
@Introspected
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "message_id")
    @NotNull
    private String messageID;

    @Column(name = "parent_id")
    @Nullable
    private String parentID;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date received;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date sent;

    @Column
    private String thread;

    @Column
    private Integer depth;
}
