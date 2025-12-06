package com.baeldung.lhj.persistence.repository;

import com.baeldung.lhj.persistence.model.WorkerSkill;

import java.util.Set;

public interface WorkerSkillRepository {

    Set<WorkerSkill> findByWorkerId(Long workerId);

    Set<WorkerSkill> findBySkillId(Long skillId);

    WorkerSkill save(WorkerSkill workerSkill);
}
