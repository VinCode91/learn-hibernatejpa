package com.baeldung.lhj.persistence.repository.impl;

import com.baeldung.lhj.persistence.model.Worker;
import com.baeldung.lhj.persistence.util.JpaUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityLifecycleUnitTest {

    @Test
    void whenANewEntityIsInstantiated_thenIsNotManagedByJpa_andHasNoID() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            Worker john = new Worker("john.doe1@gmail.com", "John", "Doe");
            boolean isManaged = entityManager.contains(john);

            assertFalse(isManaged);
            assertNull(john.getId());
        }
    }

    @Test
    void whenANewEntityIsPersisted_thenIsManagedByJpa_andHasAnID() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            Worker john = new Worker("john.doe2@gmail.com", "John", "Doe");
            entityManager.persist(john);
            entityManager.getTransaction().commit();

            boolean isManaged = entityManager.contains(john);
            assertTrue(isManaged);
            assertNotNull(john.getId()); // After persist() is called, john receives an id
            // Test assertions still work if transaction not yet commited, entity is managed as soon as persist() is called
            //entityManager.getTransaction().commit();
        }
    }

    @Test
    void givenAPersistedEntity_whenWeFetchIt_thenIsManagedByJpa_andHasAnID() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            Worker john = new Worker("john.doe3@gmail.com", "John", "Doe");
            entityManager.persist(john);
            entityManager.getTransaction().commit();

            //entityManager.detach(john);

            Worker johnFromDb = entityManager.find(Worker.class, john.getId());

            boolean isManaged = entityManager.contains(johnFromDb);
            assertTrue(isManaged);
            assertEquals(john, johnFromDb);

            // The assertions work even when equals method is not overriden in Worker class. EntityManager find()
            // retrieves the exact same object (same reference in debug mode)
            // This is no longer true if john is detached, find() method will return a new instance of Worker which
            // is obviously managed
        }
    }

    @Test
    void givenAManagedEntity_whenItsDetached_thenItsNoLongerManagedByJpa() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            Worker john = new Worker("john.doe4@gmail.com", "John", "Doe");
            entityManager.persist(john);
            entityManager.getTransaction().commit();

            assertTrue(entityManager.contains(john));

            entityManager.detach(john);

            assertFalse(entityManager.contains(john));

            // Can't persist detached entity
            assertThrows(EntityExistsException.class, () -> entityManager.persist(john));

            // Debug shows managedJohn is a different instance from john, because john remains detached after merge call
            Worker managedJohn = entityManager.merge(john);
            assertTrue(entityManager.contains(managedJohn));
        }
    }

    @Test
    void givenADetachedEntity_whenItsMerged_thenItsManagedByJpa() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            Worker john = new Worker("john.doe5@gmail.com", "John", "Doe");
            entityManager.persist(john);
            entityManager.getTransaction().commit();

            Worker detachedJohn = new Worker("john.doe5@gmail.com", "John", "Doe");
            detachedJohn.setId(john.getId());
            detachedJohn.setLastName("Shepard"); // this change is ignored
            assertFalse(entityManager.contains(detachedJohn));

            Worker managedJohn = entityManager.merge(detachedJohn); // merge return reference to john instance

            // merge(detachedJohn) returned formerly managed object, which is still managed, but detachedJohn remains unmanaged
            assertFalse(entityManager.contains(detachedJohn));
            // managedJohn and john are the same object instance
            assertEquals(john, managedJohn);

            detachedJohn.setFirstName("Mark");

            // Changes on detachedJohn are propagated to john and managedJohn after merge
            assertEquals("Shepard", john.getLastName());
            assertEquals("Shepard", managedJohn.getLastName());

            // First name change not tracked since not merged
            assertNotEquals("Mark", john.getFirstName());

            entityManager.merge(detachedJohn);

            // detachedJohn remains unmanaged, it is the returned value from merge which is merged and obviously equals john instance
            assertFalse(entityManager.contains(detachedJohn));

            // First name change propagated after new merge
            assertEquals("Mark", john.getFirstName());
            assertEquals("Mark", managedJohn.getFirstName());
        }
    }

    @Test
    void givenAManagedEntity_whenWeCallRemoveAndCommit_thenItsDeletedFromDB() {
        try (EntityManager entityManager = JpaUtil.getEntityManager()) {
            entityManager.getTransaction().begin();
            Worker john = new Worker("john.doe6@gmail.com", "John", "Doe");
            entityManager.persist(john);
            entityManager.getTransaction().commit();

            Worker johnFromDb = entityManager.find(Worker.class, john.getId());
            assertNotNull(johnFromDb);

            entityManager.getTransaction().begin();
            entityManager.remove(john);
            assertFalse(entityManager.contains(john));  // already returns false, but the entity is actually present
            // in the persistence context until flush/commit
            //entityManager.getTransaction().commit();
            johnFromDb = entityManager.find(Worker.class, john.getId());
            assertNull(johnFromDb);
            // Will throw OptimisticLockException if commit is made before merge
            assertThrows(IllegalArgumentException.class, () -> entityManager.merge(john));
            entityManager.getTransaction().commit();

//            entityManager.getTransaction().rollback();
//            assertFalse(entityManager.contains(john)); // john is in a detached state after rollback
//            assertTrue(entityManager.contains(entityManager.merge(john)));
        }
    }
}
