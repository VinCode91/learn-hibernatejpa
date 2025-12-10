package com.baeldung.lhj.persistence.repository.impl;

import java.util.List;
import java.util.Optional;

import com.baeldung.lhj.persistence.model.Campaign;
import com.baeldung.lhj.persistence.repository.CampaignRepository;
import com.baeldung.lhj.persistence.util.JpaUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class DefaultCampaignRepository implements CampaignRepository {

    public DefaultCampaignRepository() {
        super();
    }

    @Override
    public Optional<Campaign> findById(Long id) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            Campaign retrievedCampaign = entityManager.find(Campaign.class, id);
            return Optional.ofNullable(retrievedCampaign);
        }
    }

    @Override
    public Campaign save(Campaign campaign) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(campaign);
            entityManager.getTransaction().commit();
            return campaign;
        }
    }

    @Override
    public List<Campaign> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Campaign> criteriaQuery = criteriaBuilder.createQuery(Campaign.class);
            Root<Campaign> root = criteriaQuery.from(Campaign.class);
            criteriaQuery.select(root);

            return entityManager
                    .createQuery(criteriaQuery)
                    .getResultList();
        }
    }

    @Override
    public List<Campaign> findByNameOrDescriptionContaining(String text) {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Campaign> criteriaQuery = criteriaBuilder.createQuery(Campaign.class);
            Root<Campaign> root = criteriaQuery.from(Campaign.class);
            criteriaQuery
                    .select(root)
                    .where(criteriaBuilder.or(
                    criteriaBuilder.like(root.get("name"), "%"+text+"%"),
                    criteriaBuilder.like(root.get("description"), "%"+ text + "%")
            ));

            return entityManager.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public int deleteCampaignsWithoutTasks() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaDelete<Campaign> criteriaDelete = criteriaBuilder.createCriteriaDelete(Campaign.class);
            Root<Campaign> root = criteriaDelete.from(Campaign.class);
            criteriaDelete.where(criteriaBuilder.isEmpty(root.get("tasks")));

            entityManager.getTransaction().begin();
            int deleteCount = entityManager.createQuery(criteriaDelete).executeUpdate();
            entityManager.getTransaction().commit();
            return deleteCount;
        }
    }

}