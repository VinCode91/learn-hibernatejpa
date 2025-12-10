package com.baeldung.lhj.persistence.repository.impl;

import java.util.List;
import java.util.Optional;

import com.baeldung.lhj.persistence.model.Task;
import com.baeldung.lhj.persistence.model.TaskStatus;
import com.baeldung.lhj.persistence.model.Worker;
import com.baeldung.lhj.persistence.repository.TaskRepository;
import com.baeldung.lhj.persistence.util.JpaUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

public class DefaultTaskRepository implements TaskRepository {

    public DefaultTaskRepository() {
        super();
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
        return List.of();
    }

    @Override
    public List<Task> findAndOrderByFields(String filterField, Object filterValue,
                                           String sortField, boolean sortAscending) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
            Root<Task> root = criteriaQuery.from(Task.class);

            Order order;
            if (sortAscending) {
                order = criteriaBuilder.asc(root.get(sortField));
            } else {
                order = criteriaBuilder.desc(root.get(sortField));
            }

            criteriaQuery
                    .where(criteriaBuilder.equal(root.get(filterField), filterValue))
                    .orderBy(order)
                    .select(root);

            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public List<Task> findByWorkerEmailImplicitJoin(String email) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
            Root<Task> root = criteriaQuery.from(Task.class);
            criteriaQuery.select(root)
                    .where(criteriaBuilder.equal(root.get("assignee").get("email"), email));

            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public List<Task> findByWorkerEmailExplicitJoin(String email) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
            Root<Task> root = criteriaQuery.from(Task.class);

            Join<Task, Worker> assigneeJoin = root.join("assignee");
            criteriaQuery
                    .select(root)
                    .where(criteriaBuilder.equal(assigneeJoin.get("email"), email));

            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public int holdTasksByCampaignId(Long campaignId) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            CriteriaUpdate<Task> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Task.class);
            Root<Task> root = criteriaUpdate.from(Task.class);
            criteriaUpdate
                    .set(root.get("status"), TaskStatus.ON_HOLD)
                    .where(criteriaBuilder.equal(root.get("campaign").get("id"), campaignId));

            int updateCount = entityManager.createQuery(criteriaUpdate).executeUpdate();
            entityManager.getTransaction().commit();

            return updateCount;
        }
    }

}