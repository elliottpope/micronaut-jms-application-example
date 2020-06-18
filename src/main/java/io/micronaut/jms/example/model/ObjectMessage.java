package io.micronaut.jms.example.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Data
public class ObjectMessage implements Serializable {
    private String name;
    private boolean flag;
    private int number;
    private Date date;
    private Collection<NestedObject> nested;
}

@Data
class NestedObject {
    private Integer number;
}