# Learn Hibernate and JPA - Core Hibernate and JPA Concepts

This is the codebase for Module "Core Hibernate and JPA Concepts" of Learn Hibernate and JPA.

## JPA Concepts

### Persistence context
The persistence context is the part of JPA that keeps track of all the entities currently managed by the EntityManager.
When an entity is in the managed state it is stored in this context. Any changes we make to that entity are automatically tracked,
and JPA will update the database when the transaction is committed (or the context is flushed)

### Entity states
JPA defines 4 of them:
- **Transient (new)**: the entity instance has just been instantiated using the new operator. Not yet persited et not tracked by persistence context
- **Managed (persistent)**: entity currently tracked by the persistence context. Changes to its state automatically detected and synchronized with the database, usually when the transaction commits
- **Detached**: the entity was previously managed but is no longer associated with an active persistence context, for example, after a transaction ends.
- **Removed**: the entity is marked for deletion in the persistence context. The actual deletion happens when the context is flushed or the transaction is committed

**Detaching an entity can be done explicitly by calling entityManager.detach(entity)**. Alternatively,
all entities can be detached at once using entityManager.clear(), or indirectly by closin the EntityManager

*entityManager.contains(entity)* only checks if an entity is managed by the persistence context, not if it is present in database. A detached entity may be present in the DB, but *contains()* will return false.

merging entities is the opposite of detaching them. So, **if we want to make persistent changes to a detached entity, we must reattach it to a new persistence context, typically using merge()**
CAUTION : it is crucial to differentiate java objects from entities. As seen in EntityLifecycleUnitTest in entity-lifecycle-start,
merge(entity) returns an managedEntity instance which is distinct from input entity object
