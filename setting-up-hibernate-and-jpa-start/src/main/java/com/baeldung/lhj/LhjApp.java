package com.baeldung.lhj;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LhjApp {

    public static void main(final String... args) {
        EntityManagerFactory emf = null;
        try {
            Logger log = LoggerFactory.getLogger(LhjApp.class);
            log.info("Running Learn Hibernate and JPA App");

            // Persistence unit name must match name defined in persistence.xml
            emf = Persistence.createEntityManagerFactory("LHJ");
        } finally {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }

    }

}
