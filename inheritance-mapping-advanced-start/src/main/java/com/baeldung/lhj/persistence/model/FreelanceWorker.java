package com.baeldung.lhj.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class FreelanceWorker extends Worker {

    @Column(name = "hourly_rate", nullable = false)
    private Double hourlyRate;

    public FreelanceWorker() {
    }

    public FreelanceWorker(String email, String firstName, String lastName, Double hourlyRate) {
        super(email, firstName, lastName);
        this.hourlyRate = hourlyRate;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

}