package com.cloudworkers.cloudworker.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Output.
 */
@Entity
@Table(name = "output")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Output implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private Node node;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @Column(name = "date")
    private ZonedDateTime date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Output output = (Output) o;
        return Objects.equals(id, output.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Output{" +
            "id=" + id +
            ", message='" + message + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
