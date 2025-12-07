package com.baeldung.lhj.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class FullTimeWorker extends Worker {

    @Column(nullable = false)
    private Double salary;

    public FullTimeWorker() {
    }

    public FullTimeWorker(String email, String firstName, String lastName, Double salary) {
        super(email, firstName, lastName);
        this.salary = salary;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

}