package com.baeldung.lhj.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class WorkerPerformance {

    @Id
    private Long id;

    private Integer rating;

    @Column(name = "tasks_completed")
    private Integer tasksCompleted;

    @Column(name = "completion_rate")
    private Double completionRate;

    @OneToOne(mappedBy = "performance")
    private Worker worker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(Integer tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    @Override
    public String toString() {
        return "WorkerPerformance [" +
            "id=" + id +
            ", rating=" + rating +
            ", tasksCompleted=" + tasksCompleted +
            ", completionRate=" + completionRate +
            "]";
    }

}