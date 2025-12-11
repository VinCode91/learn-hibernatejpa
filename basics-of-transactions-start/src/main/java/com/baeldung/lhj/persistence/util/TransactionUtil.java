package com.baeldung.lhj.persistence.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class TransactionUtil {

    private static final Logger logger = LoggerFactory.getLogger(TransactionUtil.class);
    private final EntityManager entityManager;

    public TransactionUtil(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void inTransaction(Consumer<EntityManager> action) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            action.accept(entityManager);
            entityTransaction.commit();
            logger.info("Transaction completed successfully");
        } catch (RuntimeException e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
                logger.error("Transaction failed", e);
            }
            throw e;
        }
    }
}
