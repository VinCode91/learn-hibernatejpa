package com.baeldung.lhj;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityManager;

import org.hibernate.LazyInitializationException;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@Disabled
@ExtendWith(CloseResourcesExtension.class)
class LazyFetchUnitTest {
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
            Campaign newCampaign = new Campaign("C" + i, "Campaign " + i, "Campaign " + i + " Description");
            campaignRepository.save(newCampaign);

            Task newTask = new Task("Task " + i, "Task " + i + " Description", LocalDate.now(), newCampaign, TaskStatus.TO_DO, newWorker);
            taskRepository.save(newTask);
        }
    }

    @Test
    void whenAccessingTasksLazily_thenTwoSelectsExecute() {
        // when
        Campaign campaign = em.find(Campaign.class, 1L); // first SELECT (campaign)
        campaign.getTasks().size(); // second SELECT (tasks)

        // then
        assertEquals(2, stats.getPrepareStatementCount());
    }


    @Test
    void whenAccessingTasksLazilyOutsidePersistenceContext_throwsLazyInitializationException() {
        Campaign campaign;
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            // when
            campaign = entityManager.find(Campaign.class, 12L); // first SELECT (campaign)
        }

        // then
        assertThrows(LazyInitializationException.class, () -> campaign.getTasks().size());
    }

    @Test
    public void whenAccessingTasksLazily_thenNPlus1ProblemOccurs() {
        // when
        // Using JOIN FETCH forces fetch
        //String selectQuery = "SELECT c FROM Campaign c JOIN FETCH c.tasks t JOIN FETCH t.assignee";

        String selectQuery = "SELECT c FROM Campaign c";

        List<Campaign> campaigns = em.createQuery(selectQuery, Campaign.class)
                .getResultList(); // first SELECT (campaigns)

        for (Campaign c : campaigns) {
            c.getTasks().size(); // each line emits a SELECT tasks where campaign_id = ?
        }

        // then
        assertEquals(11, stats.getPrepareStatementCount());
    }

}
