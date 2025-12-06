package com.baeldung.lhj.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Campaign {

    @Id
    private Long id;

    private String code;

    private String name;

    private String description;

    @OneToMany(mappedBy = "campaign")
    private Set<Task> tasks = new HashSet<>();

    public Campaign(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Campaign() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Campaign [id=" + id + ", code=" + code + ", name=" + name + ", description=" + description + "]";
    }

}
