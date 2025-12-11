package com.baeldung.lhj;

import com.baeldung.lhj.persistence.model.Campaign;
import com.baeldung.lhj.persistence.model.Task;
import com.baeldung.lhj.persistence.model.TaskStatus;
import com.baeldung.lhj.persistence.repository.CampaignRepository;
import com.baeldung.lhj.persistence.repository.impl.DefaultCampaignRepository;
import com.baeldung.lhj.persistence.util.JpaUtil;

import org.hibernate.PropertyValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class LhjApp {

    public static void main(final String... args) {
        try {
            Logger logger = LoggerFactory.getLogger(LhjApp.class);
            logger.info("Running Learn Hibernate and JPA App");

            CampaignRepository campaignRepository = new DefaultCampaignRepository();
            Campaign campaign1 = new Campaign("Campaign 1", "Campaign 1 Name", "Campaign 1 Description");
            Task task1 = new Task("Task 1", "Task 1 Description", LocalDate.now(), null, TaskStatus.TO_DO, null);
            Task task2 = new Task("Task 2", "Task 2 Description", LocalDate.now(), null, TaskStatus.TO_DO, null);

            campaignRepository.createCampaignWithTasks(campaign1, List.of(task1, task2));
            logger.info("{} campaign(s) present in the database", campaignRepository.findAll().size());

            Campaign campaign2 = new Campaign("Campaign 2", "Campaign 2 Name", "Campaign 2 Description");
            Task taskWithNoName = new Task(null, "Task Description", LocalDate.now(), null, TaskStatus.TO_DO, null);

            try {
                campaignRepository.createCampaignWithTasks(campaign2, List.of(taskWithNoName));
            } catch (PropertyValueException exception) {
                logger.error("{}", exception.getMessage());
            }
            logger.info("{} campaign(s) present in the database", campaignRepository.findAll().size());
        } finally {
            JpaUtil.closeEntityManagerFactory();
        }
    }

}