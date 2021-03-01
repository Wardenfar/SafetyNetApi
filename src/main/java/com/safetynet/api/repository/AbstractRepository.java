package com.safetynet.api.repository;

import com.safetynet.api.entity.Person;

public abstract class AbstractRepository<E> {

    public abstract int count();

    public abstract void clear();

    public abstract void update(E entity);
}
