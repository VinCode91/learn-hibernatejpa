package com.baeldung.lhj.persistence.model;

import com.baeldung.lhj.LhjApp;
import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;

@Entity
public class Campaign {

    // We should be cautious when using IDENTITY GenerationType in high-throughput applications,
    // as Hibernate cannot batch-insert entities with this strategy – it must execute
    // each insert individually to retrieve the generated ID from the database

    // UUIDs consume relatively more space than numeric IDs (typically 128 bits vs. 64 bits for a long)
    // and can impact database/query performance because of their randomness

    // SEQUENCE separates ID generation from row insertion. Using sequences provides better support for batch inserts
    // since Hibernate can pre-allocate IDs using the defined sequence.
    // Hence, the SEQUENCE strategy proves handy for high-throughput applications where sequences offer performance benefits

    // With Table strategy, Hibernate uses a separate table, id_generator, to track and increment ID values using the
    // campaign_id value for the gen_name column.
    // Although this strategy seems flexible and portable, performance may degrade under high insert rates due to
    // frequent reads and writes to the id_generator table.
    // So, we can conclude that it’s more portable across databases but comes with a performance cost due to the
    // additional lookup
    @Id
    @TableGenerator(
            name = "campaign_gen",
            table = "id_generator",
            pkColumnName = "gen_name",
            valueColumnName = "gen_value",
            pkColumnValue = "campaign_id",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "campaign_gen")
    @Column(name = "id")
    private Long id;

    @NaturalId
    @Column(name = "code", unique = true, nullable = false, updatable = false)
    private String code;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "description")
    private String description;

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

    @Override
    public String toString() {
        return "Campaign [id=" + id + ", code=" + code + ", name=" + name + ", description=" + description + "]";
    }

}
