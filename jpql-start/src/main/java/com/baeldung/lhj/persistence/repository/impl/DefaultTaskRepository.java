package com.baeldung.lhj.persistence.repository.impl;

import java.util.List;
import java.util.Optional;

import com.baeldung.lhj.persistence.model.Task;
import com.baeldung.lhj.persistence.model.TaskStatus;
import com.baeldung.lhj.persistence.repository.TaskRepository;
import com.baeldung.lhj.persistence.util.JpaUtil;

import jakarta.persistence.EntityManager;

public class DefaultTaskRepository implements TaskRepository {

    public DefaultTaskRepository() {
    }

    @Override
    public Optional<Task> findById(Long id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            Task retrievedTask = entityManager.find(Task.class, id);
            return Optional.ofNullable(retrievedTask);
        }
    }

    @Override
    public Task save(Task task) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(task);
            entityManager.getTransaction().commit();
            return task;
        }
    }

    @Override
    public List<Task> findAll() {
        try(EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager.createQuery("select t from Task t", Task.class).getResultList();
        }
    }

    @Override
    public List<Task> findByStatuses(List<TaskStatus> statuses) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return entityManager
                    .createQuery("select t from Task t where t.status in (:statuses)", Task.class) // works also without parenthesis
                    .setParameter("statuses", statuses).getResultList();
        }
    }

    @Override
    public List<Task> findByWorkerEmail(String email) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            // Use of the dot operator here eliminates need for JOIN clause
            return entityManager
                    .createQuery("select t FROM Task t WHERE t.assignee.email = ?1", Task.class)
                    .setParameter(1, email).getResultList();

            // Following sample also works but verbose
//            return entityManager
//                    .createQuery("select t FROM Task t JOIN t.assignee w WHERE w.email = ?1", Task.class)
//                    .setParameter(1, email).getResultList();
        }
    }

    @Override
    public int holdTasksByCampaignId(Long campaignId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            int updateCount = entityManager.createQuery("update Task t set t.status = ?1 WHERE t.campaign.id = ?2")
                            .setParameter(1, TaskStatus.ON_HOLD).setParameter(2, campaignId)
                            .executeUpdate();
            entityManager.getTransaction().commit();
            return updateCount;
        }
    }

}