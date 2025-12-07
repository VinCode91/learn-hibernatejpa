package com.baeldung.lhj.persistence.repository.impl;

import com.baeldung.lhj.persistence.model.Worker;
import com.baeldung.lhj.persistence.repository.WorkerRepository;
import com.baeldung.lhj.persistence.repository.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultWorkerRepository implements WorkerRepository {

    private Set<Worker> workers;

    public DefaultWorkerRepository() {
        this.workers = new HashSet<>();
    }

    @Override
    public Optional<Worker> findById(Long id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            return Optional.ofNullable(entityManager.find(Worker.class, id));
        }
    }

    @Override
    public Worker save(Worker worker) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(worker);
            entityManager.getTransaction().commit();
            return worker;
        }
    }

    @Override
    public void update(Long id, Worker worker) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();

            Worker retrievedWorker = entityManager.find(Worker.class, id);
            retrievedWorker.setFirstName(worker.getFirstName());
            retrievedWorker.setLastName(worker.getLastName());

            entityManager.getTransaction().commit();
            // No need to call entityManager.persist()
        }
    }

    @Override
    public void deleteById(Long id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();

            Worker retrievedWorker = entityManager.find(Worker.class, id);
            entityManager.remove(retrievedWorker);

            entityManager.getTransaction().commit();
        }
    }

}