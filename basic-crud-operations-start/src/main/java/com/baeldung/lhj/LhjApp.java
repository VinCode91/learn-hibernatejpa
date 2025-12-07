package com.baeldung.lhj;

import com.baeldung.lhj.persistence.model.Worker;
import com.baeldung.lhj.persistence.repository.WorkerRepository;
import com.baeldung.lhj.persistence.repository.impl.DefaultWorkerRepository;
import com.baeldung.lhj.persistence.repository.util.JpaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LhjApp {

    public static void main(final String... args) {
        try {
            Logger logger = LoggerFactory.getLogger(LhjApp.class);
            logger.info("Running Learn Hibernate and JPA App");

            // create Worker
            WorkerRepository workerRepository = new DefaultWorkerRepository();
            Worker worker = new Worker("john.doe@baeldung.com", "John", "Doe");
            workerRepository.save(worker);
            logger.info("Saved worker with id: {}", worker.getId());

            // Retrieve created Worker
            Worker retrievedWorker = workerRepository.findById(worker.getId())
                    .orElseThrow(IllegalArgumentException::new);
            logger.info("Retrieved worker: {}", retrievedWorker);

            // Update Worker
            worker.setFirstName("Johnny");
            worker.setLastName("Doeson");
            workerRepository.update(worker.getId(), worker);
            logger.info("Updated worker with id: {}", worker.getId());

            // Retrieve updated Worker
            retrievedWorker = workerRepository.findById(worker.getId())
                    .orElseThrow(IllegalArgumentException::new);
            logger.info("Retrieved updated worker: {}", retrievedWorker);

            // Delete Worker
            workerRepository.deleteById(worker.getId());
            logger.info("Deleted worker with id: {}", worker.getId());

            // Validate successful Worker deletion
            boolean isWorkerDeleted = workerRepository.findById(worker.getId()).isEmpty();
            if (isWorkerDeleted) {
                logger.info("Verified worker with id: {} is deleted", worker.getId());
            }
        } finally {
            JpaUtil.closeEntityManagerFactory();
        }
    }

}