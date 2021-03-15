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
     * @return
     */
    public abstract boolean add(E entity);

    /**
     * Update an entity
     * @return
     */
    public abstract boolean update(E entity);

    /**
     * Remove an entity
     * @return
     */
    public abstract boolean remove(E entity);
}
