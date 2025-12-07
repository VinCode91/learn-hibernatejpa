package com.baeldung.lhj.persistence.model;

import com.baeldung.lhj.LhjApp;
import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

@Entity
public class Worker {

    @Id
    @TableGenerator(
            name = "campaign_gen", // name's scope limited to class even if identic to name defined for Campaign class
            table = "id_generator", // Can use same table for different id generation with distinct pkColumnValue
            pkColumnName = "gen_name",
            valueColumnName = "gen_value",
            pkColumnValue = "worker_id",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "campaign_gen")
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name = "email", unique = true, nullable = false, updatable = false)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public Worker(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Worker() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Worker [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}
