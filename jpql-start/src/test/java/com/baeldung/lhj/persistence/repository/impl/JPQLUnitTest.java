package com.baeldung.lhj.persistence.repository.impl;

import com.baeldung.lhj.extension.CloseResourcesExtension;
import com.baeldung.lhj.persistence.model.Campaign;
import com.baeldung.lhj.persistence.model.Task;
import com.baeldung.lhj.persistence.model.TaskStatus;
import com.baeldung.lhj.persistence.model.Worker;
import com.baeldung.lhj.persistence.repository.CampaignRepository;
import com.baeldung.lhj.persistence.repository.TaskRepository;
import com.baeldung.lhj.persistence.repository.WorkerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(CloseResourcesExtension.class)
class JPQLUnitTest {

    static CampaignRepository campaignRepository = new DefaultCampaignRepository();
    static TaskRepository taskRepository = new DefaultTaskRepository();
    static WorkerRepository workerRepository = new DefaultWorkerRepository();

    @BeforeAll
    static void setup() {
        createTestData();
    }

    static void createTestData(){
        Campaign newCampaign = new Campaign("C1", "Campaign 1", "Campaign 1 Description");
        campaignRepository.save(newCampaign);        
        Campaign newCampaign2 = new Campaign("C2", "Campaign 2", "Campaign 2 Description");
        campaignRepository.save(newCampaign2);

        Worker newWorker = new Worker("john@test.com", "John", "Doe");
        workerRepository.save(newWorker);

        Task newTask = new Task("Task 1", "Task 1 Description", LocalDate.now(), newCampaign, TaskStatus.TO_DO, newWorker);
        taskRepository.save(newTask);
      
        Task onHoldTask = new Task("Task 2", "Task 2 Description", LocalDate.now(), newCampaign, TaskStatus.ON_HOLD, null);
        taskRepository.save(onHoldTask);

        Task campaign2Task = new Task("Task 3", "Task 3 Description", LocalDate.now(), newCampaign2, TaskStatus.TO_DO, newWorker);
        taskRepository.save(campaign2Task);
  
        Worker activeWorker = new Worker("active.worker@baeldung.com", "Active", "Worker");
        Worker idleWorker = new Worker("idle.worker@worker.com", "Idle", "Worker");
        workerRepository.save(activeWorker);
        workerRepository.save(idleWorker);
       
        Task activeTask = new Task("Active Task", "Active Task Description", LocalDate.now(), newCampaign, TaskStatus.IN_PROGRESS, activeWorker);
        Task idleTask = new Task("Idle Task", "Idle Task Description", LocalDate.now(), newCampaign, TaskStatus.TO_DO, idleWorker);
        taskRepository.save(activeTask);
        taskRepository.save(idleTask);
    }

    @Test
    void givenCampaignRepository_whenFindingAllCampaigns_thenListOfCampaignsReturned() {
        List<Campaign> campaigns = campaignRepository.findAll();

        assertEquals(2, campaigns.size());
    }

    @Test
    void givenTaskRepository_whenFindingTasksByStatus_thenListOfTasksReturned() {
        List<Task> tasks = taskRepository.findByStatuses(List.of(TaskStatus.ON_HOLD));

        assertEquals(1, tasks.size());
        assertEquals(TaskStatus.ON_HOLD, tasks.getFirst().getStatus());
    }

    @Test
    void givenWorkerRepository_whenFindingWorkersWithActiveTasks_thenListOfWorkersReturned() {
        List<Worker> workers = workerRepository.findWorkersWithActiveTasks();

        List<String> workerEmails = workers.stream().map(Worker::getEmail).toList();

        assertTrue(workerEmails.contains("active.worker@baeldung.com"));
    }

    @Test
    void givenTaskRepository_whenFindingTasksByWorkerEmail_thenListOfTasksReturned() {
        List<Task> tasksByWorkerEmail = taskRepository.findByWorkerEmail("john@test.com");

        assertEquals(2, tasksByWorkerEmail.size());
        assertEquals("john@test.com", tasksByWorkerEmail.getFirst().getAssignee().getEmail());
    }

    @Test
    void givenWorkerRepository_whenFindingWorkersInAscNameOrder_thenListOfWorkersReturned() {
        List<Worker> workers = workerRepository.findAllOrderByFirstName();

        List<String> workerNames = workers.stream().map(Worker::getFirstName).toList();
        List<String> workerNamesSorted = new ArrayList<>(workerNames);
        workerNamesSorted.sort((s1, s2) -> -s1.compareTo(s2));

        assertIterableEquals(workerNames, workerNamesSorted);
    }

    @Test
    //@Order(1)
    void givenATask_whenUpdatingStatus_thenTaskStatusGetsUpdated() {
        Optional<Campaign> campaign = campaignRepository.findByCodeAndName("C2", "Campaign 2");
        int numberOfUpdatedTasks = taskRepository.holdTasksByCampaignId(campaign.get().getId());

        assertEquals(1, numberOfUpdatedTasks);
    }
}
