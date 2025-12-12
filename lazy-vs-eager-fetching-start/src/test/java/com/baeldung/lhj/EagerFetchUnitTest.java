package com.baeldung.lhj;

import java.time.LocalDate;

import jakarta.persistence.EntityManager;

import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.baeldung.lhj.extension.CloseResourcesExtension;
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
import com.baeldung.lhj.persistence.util.JpaUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(CloseResourcesExtension.class)
class EagerFetchUnitTest {
    private EntityManager em;
    private Statistics stats;

    private CampaignRepository campaignRepository;
    private TaskRepository taskRepository;
    private WorkerRepository workerRepository;
    private Worker newWorker;

    @BeforeEach
    void setup() {
        em = JpaUtil.getEntityManager();

        campaignRepository = new DefaultCampaignRepository();
        taskRepository = new DefaultTaskRepository();
        workerRepository = new DefaultWorkerRepository();

        em.getTransaction()
            .begin();

        newWorker = new Worker("johnsmith@baeldung.com", "John", "Smith");
        workerRepository.save(newWorker);

        createCampaignAndTasks(10);

        em.getTransaction()
            .commit();

        stats = JpaUtil.getStatistics();
        stats.clear();
    }

    @AfterEach
    void tearDown() {
        em.getTransaction()
            .begin();
        em.createQuery("delete from Task")
            .executeUpdate();
        em.createQuery("delete from Campaign")
            .executeUpdate();
        em.createQuery("delete from Worker")
            .executeUpdate();
        em.getTransaction()
            .commit();

        em.close();
    }

    private void createCampaignAndTasks(int count) {
        for (int i = 1; i <= count; i++) {
            Campaign campaign = new Campaign("C" + i, "Campaign " + i, "Campaign " + i + " Description");
            campaignRepository.save(campaign);

            Task task = new Task("Task " + i, "Task " + i + " Description", LocalDate.now(), campaign, TaskStatus.TO_DO, newWorker);
            taskRepository.save(task);
        }
    }

    @Test
    void whenMappingIsEager_thenSingleSelectExecutes() {
        // because tasks() is EAGER, this call fetches campaign + tasks in one round‑trip
        Campaign campaign = em.find(Campaign.class, 1L);
        assertFalse(campaign.getTasks().isEmpty());

        // one SQL statement – either a join or an immediate secondary select
        assertEquals(1, stats.getPrepareStatementCount());
    }

}
