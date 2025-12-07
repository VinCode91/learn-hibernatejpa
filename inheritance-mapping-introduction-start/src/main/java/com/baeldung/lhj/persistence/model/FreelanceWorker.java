package com.baeldung.lhj.persistence.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FREELANCE")
public class FreelanceWorker extends Worker {

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