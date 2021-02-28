package com.safetynet.api.repository;

import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonRepository extends AbstractRepository {

    private Set<Person> persons = new HashSet<>();

    public void add(Person person) {
        assert person.getFireStation() != null;
        persons.add(person);
        FireStation fireStation = person.getFireStation();
        fireStation.add(person);
    }

    public int count() {
        return persons.size();
    }

    @Override
    public void clear() {
        persons.clear();
    }

    public Set<Person> findAll() {
        return Collections.unmodifiableSet(persons);
    }

    public Person findAllByFirstNameAndLastName(String firstName, String lastName) {
        return persons.stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .findFirst().orElse(null);
    }

    public Set<Person> findAllByAddress(String address) {
        return persons.stream()
                .filter(p -> p.getAddress().equals(address))
                .collect(Collectors.toSet());
    }

    public Set<Person> findAllByLastName(String lastName) {
        return persons.stream()
                .filter(p -> p.getLastName().equals(lastName))
                .collect(Collectors.toSet());
    }
}
