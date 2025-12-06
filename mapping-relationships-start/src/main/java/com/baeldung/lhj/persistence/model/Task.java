package com.baeldung.lhj.persistence.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Task {

    @Id
    private Long id;

    private String name;

    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    private TaskStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker assignee;

    @ManyToMany
    @JoinTable(
            name = "Task_Label",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<Label> labels = new HashSet<>();

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

    @Override
    public String toString() {
        return "Task [id=" + id + ", name=" + name + ", description=" + description + ", dueDate=" + dueDate + ", status=" + status + "]";
    }
}
