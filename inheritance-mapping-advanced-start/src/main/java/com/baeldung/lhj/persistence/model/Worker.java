package com.baeldung.lhj.persistence.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;



// TABLE_PER_CLASS strategy is relatively simple and straightforward to implement and can be used when the
// subclasses have a significant number of specific fields that require constraints. The query performance is also
// good when individual subclasses are queried. However, in the case of polymorphic queries, the query performance
// will be relatively slow as JPA will perform UNION operations behind the scenes to combine data from all subclasses
//@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)


// For JOINED strategy, JPA uses JOIN statements to retrieve the complete entity data, which slows down query performance.
// When querying for the base entity or a subclass entity, Hibernate performs a join between the base class table and
// the corresponding subclass tables to fetch the necessary data. The performance impact becomes more significant with
// deeper inheritance hierarchies, as more joins are needed.
//
// Despite this drawback, the joined table strategy is still a viable option when data normalization and data integrity
// are important factors in the project’s architectural design, and the inheritance hierarchy is not very deep
@Entity
@Inheritance(strategy = InheritanceType.JOINED)

// This strategy instructs JPA that Worker is not an entity itself but a class whose mappings should be inherited by its
// entity subclasses
// An exception will occur in this scenario as the Task entity has a many-to-one association with the Worker class
// through the assignee field. However, with the mapped superclass strategy, the Worker class is no longer an entity.
// It’s now a mapped superclass, which means it cannot be used as a target for associations
// Another limitation of this strategy is that polymorphic queries are not natively supported by JPA. Since the base
// class is not an entity, we cannot directly query for instances of the base class. However, queries on individual
// subclasses still perform well, as each subclass is mapped to its own table, and no joins are required to fetch the
// data
//@MappedSuperclass
public abstract class Worker {

    @Id
    //@GeneratedValue(strategy = GenerationType.TABLE, generator = "workerId_gen")
    // We can reuse this id_generator sequence table for other inheritance hierarchies in our project,
    // and the only thing we’ll need to update is the unique generator name and the pkColumnValue
    // Table or sequence generation are necessary for TABLE_PER_CLASS inheritance type
//    @TableGenerator(
//            name = "workerId_gen",
//            table = "id_generator",
//            pkColumnName = "generator_name",
//            pkColumnValue = "worker_id",
//            valueColumnName = "generator_value"
//    )
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false, updatable = false)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "assignee")
    private Set<Task> assignedTasks = new HashSet<>();

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

    public Set<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(Set<Task> tasks) {
        this.assignedTasks = tasks;
    }

    @Override
    public String toString() {
        return "Worker [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }

}