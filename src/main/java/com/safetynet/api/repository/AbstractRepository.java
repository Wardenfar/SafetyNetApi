package com.safetynet.api.repository;

/**
 * Abstract class for repositories
 *
 * @param <E> Entity Type
 */
public abstract class AbstractRepository<E> {

    /**
     * Return the count of entities in the repository
     */
    public abstract int count();

    /**
     * Clear the content of the repository
     */
    public abstract void clear();

    /**
     * Add an entity
     */
    public abstract void add(E entity);

    /**
     * Update an entity
     */
    public abstract void update(E entity);

    /**
     * Remove an entity
     */
    public abstract void remove(E entity);
}
