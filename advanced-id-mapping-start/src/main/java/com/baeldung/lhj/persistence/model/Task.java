package com.baeldung.lhj.persistence.model;

import java.time.LocalDate;
import java.util.UUID;

import com.baeldung.lhj.LhjApp;
import jakarta.persistence.*;

@Entity
public class Task {

    @Id
    @SequenceGenerator(
            name = "shared_seq", // name's scope limited to class
            sequenceName = "shared_sequence",
            allocationSize = LhjApp.SHARED_SEQ_INCREMENT
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shared_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private final String uuid = UUID.randomUUID()
        .toString();

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "status")
    private TaskStatus status;

    public Task() {
    }

    public Task(String name, String description, LocalDate dueDate, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Task(String name, String description, LocalDate dueDate) {
        this(name, description, dueDate, TaskStatus.TO_DO);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", name=" + name + ", description=" + description + ", dueDate=" + dueDate + ", status=" + status + "]";
    }
}
