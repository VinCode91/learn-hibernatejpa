package com.baeldung.lhj;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import jakarta.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baeldung.lhj.persistence.model.Campaign;
import com.baeldung.lhj.persistence.model.Task;
import com.baeldung.lhj.persistence.util.JpaUtil;

public class LhjApp {

    static Logger logger = LoggerFactory.getLogger(LhjApp.class);
    public static void main(final String... args) {
        try {
            //Logger logger = LoggerFactory.getLogger(LhjApp.class);
            logger.info("Running Learn Hibernate and JPA App");

            createCampaign2WithTasks34();
        } finally {
            JpaUtil.closeEntityManagerFactory();
        }
    }

    // will be used throughout the lesson
    private static void createCampaign2WithTasks34() {
        // persist campaign, worker, and tasks
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            Campaign campaign2 = new Campaign();
            campaign2.setName("Baeldung CS Article Marketing");
            campaign2.setCode("BAEL_CS_ARTICLE_MARKETING");

            Task task3 = new Task();
            task3.setName("Write a post");
            task3.setCampaign(campaign2);

            Task task4 = new Task();
            task4.setName("Share on LinkedIn");
            task4.setCampaign(campaign2);

            Set<Task> tasks = new HashSet<>();
            tasks.add(task3);
            tasks.add(task4);

            campaign2.setTasks(tasks);

            entityManager.persist(campaign2);

            // With CascadeType.PERSIST trying to persist task5 with task4 id throws EntityExistsException : detached entity
            // passed to persist
            //
            // CascadeType.PERSIST only takes effect on new (transient) entities—those that haven’t been
            // persisted yet and don’t have an identifier. It won’t work properly if the tasks collection contains
            // detached entities (for example, setting the ID on a new Task in the hope that it updates an existing one)
//            Task task5 = new Task();
//            task5.setId(2L);
//            task5.setName("Share on Youtube");
//            task5.setCampaign(campaign2);
//            campaign2.getTasks().add(task5);

            transaction.commit();

            // Without CascadeType.REMOVE or orphanRemoval = true attribute on Campaign @OneToMany association to Task
            // following sample code throws org.hibernate.TransientObjectException: persistent instance references
            // an unsaved transient...  The transient instance is campaign, while the persistent is task4 or task5
            EntityTransaction transaction2 = entityManager.getTransaction();

            transaction2.begin();

            Campaign campaignFromDB = entityManager.find(Campaign.class, 1L);
            assert campaignFromDB != null;


            // Test CascadeType.PERSIST
            Task persistedTask3 = entityManager.find(Task.class, 1);
            Task persistedTask4 = entityManager.find(Task.class, 2);

            assert persistedTask3 != null;
            assert persistedTask4 != null;
            if (campaign2.equals(persistedTask4.getCampaign()) && campaign2.equals(persistedTask3.getCampaign())) {
                logger.info("Task3 and task4 affected to campaign2");
            }

            entityManager.remove(campaignFromDB);

            transaction2.commit();

            // Test CascadeType.REMOVE
            Task retrievedTask3 = entityManager.find(Task.class, 1);
            Task retrievedTask4 = entityManager.find(Task.class, 2);

            Campaign retrievedCampaign2 = entityManager.find(Campaign.class, 1);
            assert retrievedCampaign2 == null;
            assert retrievedTask3 == null;
            assert retrievedTask4 == null;
        }
    }

}
