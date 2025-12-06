package com.baeldung.lhj.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Skill {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "skill")
    private Set<WorkerSkill> workerSkills = new HashSet<>();

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

    @Override
    public String toString() {
        return "Skill [" +
            "id=" + id +
            ", name=" + name +
            "]";
    }

}