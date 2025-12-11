package com.baeldung.lhj;

import com.baeldung.lhj.persistence.model.Campaign;
import com.baeldung.lhj.persistence.repository.CampaignRepository;
import com.baeldung.lhj.persistence.repository.impl.DefaultCampaignRepository;
import com.baeldung.lhj.persistence.util.JpaUtil;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LhjApp {

    public static void main(final String... args) {
        try {
            Logger logger = LoggerFactory.getLogger(LhjApp.class);
            logger.info("Running Learn Hibernate and JPA App");

            // ... persisting the Campaign entity
            CampaignRepository campaignRepository = new DefaultCampaignRepository();
            Campaign campaign = new Campaign("Test Campaign Code", "Test Campaign Name", "Test Campaign Description");
            campaignRepository.save(campaign);


            Statistics statistics = JpaUtil.getStatistics();
            statistics.clear();

            logger.info("Fetching Campaign From First EntityManager");
            try (EntityManager entityManager1 = JpaUtil.getEntityManager()) {
                entityManager1.find(Campaign.class, campaign.getId());
            }
            logger.info("Cache Miss Count : {}", statistics.getSecondLevelCacheMissCount());
            logger.info("Cache Hit Count : {}", statistics.getSecondLevelCacheHitCount());

            logger.info("Fetching Campaign From Second EntityManager");
            try (EntityManager entityManager2 = JpaUtil.getEntityManager()) {
                entityManager2.find(Campaign.class, campaign.getId());
            }
            logger.info("Fetching Campaign From Third EntityManager");
            try (EntityManager entityManager3 = JpaUtil.getEntityManager()) {
                entityManager3.find(Campaign.class, campaign.getId());
            }
            logger.info("Cache Miss Count : {}", statistics.getSecondLevelCacheMissCount());
            logger.info("Cache Hit Count : {}", statistics.getSecondLevelCacheHitCount());

            Cache cache = JpaUtil.getCache();
            boolean isCached = cache.contains(Campaign.class, campaign.getId());
            if (isCached) {
                logger.info("Campaign with id {} is present in the second-level cache", campaign.getId());
            }

            logger.info("Clearing Second-Level Cache");
            cache.evict(Campaign.class, campaign.getId()); // eliminates one campaign instance
            cache.evict(Campaign.class); // eliminates all campaign instances
            cache.evictAll(); // clears 2nd level cache

            isCached = cache.contains(Campaign.class, campaign.getId());
            if (!isCached) {
                logger.info("Campaign with id {} is not present in the second-level cache", campaign.getId());
            }


            // ... previous first-level cache checks
            EntityManager entityManager = JpaUtil.getEntityManager();

            logger.info("Fetching Campaign - 1st Attempt => triggers query");
            entityManager.find(Campaign.class, campaign.getId());

            logger.info("Fetching Campaign - 2nd Attempt => no query");
            entityManager.find(Campaign.class, campaign.getId());

            logger.info("Fetching Campaign - 3rd Attempt => no query");
            entityManager.find(Campaign.class, campaign.getId());

            logger.info("Clearing First-Level Cache");
            entityManager.clear();

            logger.info("Fetching Campaign - 4th Attempt => new query since 1st level cache (persistence context) was cleared");
            entityManager.find(Campaign.class, campaign.getId());
        } finally {
            JpaUtil.closeEntityManagerFactory();
        }
    }

}