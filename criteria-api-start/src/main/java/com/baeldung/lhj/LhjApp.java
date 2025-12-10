package com.baeldung.lhj;

import com.baeldung.lhj.persistence.model.Campaign;
import com.baeldung.lhj.persistence.model.Task;
import com.baeldung.lhj.persistence.model.TaskStatus;
import com.baeldung.lhj.persistence.model.Worker;
import com.baeldung.lhj.persistence.repository.CampaignRepository;
import com.baeldung.lhj.persistence.repository.TaskRepository;
import com.baeldung.lhj.persistence.repository.WorkerRepository;
import com.baeldung.lhj.persistence.repository.impl.DefaultCampaignRepository;
import com.baeldung.lhj.persistence.repository.impl.DefaultTaskRepository;
import com.baeldung.lhj.persistence.repository.impl.DefaultWorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baeldung.lhj.persistence.util.JpaUtil;

import java.time.LocalDate;
import java.util.List;

public class LhjApp {

    static CampaignRepository campaignRepository = new DefaultCampaignRepository();
    static TaskRepository taskRepository = new DefaultTaskRepository();
    static WorkerRepository workerRepository = new DefaultWorkerRepository();

    public static void main(final String... args) {
        try {
            Logger logger = LoggerFactory.getLogger(LhjApp.class);
            logger.info("Running Learn Hibernate and JPA App");

            createTestData();

            List<Campaign> retrievedCampaigns = campaignRepository.findAll();
            logger.info("Campaigns retrieved: {}", retrievedCampaigns);

            retrievedCampaigns = campaignRepository.findByNameOrDescriptionContaining("2");
            logger.info("Campaigns retrieved: {}", retrievedCampaigns);

            List<Task> retrievedTasks = taskRepository.findAndOrderByFields("status", TaskStatus.TO_DO, "name", true);
            logger.info("Tasks retrieved: {}", retrievedTasks);

            retrievedTasks = taskRepository.findByWorkerEmailImplicitJoin("john.doe@baeldung.com");
            logger.info("Tasks retrieved: {}", retrievedTasks);

            retrievedTasks = taskRepository.findByWorkerEmailExplicitJoin("john.doe@baeldung.com");
            logger.info("Tasks retrieved: {}", retrievedTasks);

            int numberOfTasksPutOnHold = taskRepository.holdTasksByCampaignId(1L);
            logger.info("{} tasks put on hold", numberOfTasksPutOnHold);

            int numberOfCampaignsDeleted = campaignRepository.deleteCampaignsWithoutTasks();
            logger.info("{} campaigns without any tasks deleted", numberOfCampaignsDeleted);
        } finally {
            JpaUtil.closeEntityManagerFactory();
        }
    }

    static void createTestData() {
        Campaign campaign1 = new Campaign("C1", "Campaign 1", "Campaign 1 Description");
        Campaign campaign2 = new Campaign("C2", "Campaign 2", "Campaign 2 Description");
        campaignRepository.save(campaign1);
        campaignRepository.save(campaign2);

        Worker worker = new Worker("john.doe@baeldung.com", "John", "Doe");
        workerRepository.save(worker);

        Task task1 = new Task("Task 1", "Task 1 Description", LocalDate.now(), campaign1, TaskStatus.TO_DO, worker);
        Task task2 = new Task("Task 2", "Task 2 Description", LocalDate.now(), campaign1, TaskStatus.TO_DO, worker);
        taskRepository.save(task1);
        taskRepository.save(task2);
    }

}