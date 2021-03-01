package com.safetynet.api.repository;

import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonRepository extends AbstractRepository<Person> {

    private Set<Person> persons = new HashSet<>();

    public void add(Person person) {
        // the fireStation property is Required
        assert person.getFireStation() != null;
        persons.add(person);

        // Set back reference
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

    @Override
    public void update(Person entity) {
        // Remove previous Person
        Person prev = findOneByFirstNameAndLastName(entity.getFirstName(), entity.getLastName());
        prev.getFireStation().remove(prev);
        persons.remove(prev);

        // Add the new entity
        persons.add(entity);
        entity.getFireStation().add(entity);
    }

    @Override
    public void remove(Person entity) {

    }

    public Set<Person> findAll() {
        return Collections.unmodifiableSet(persons);
    }

    /**
     * return one Person by FirstName and LastName
     */
    public Person findOneByFirstNameAndLastName(String firstName, String lastName) {
        return persons.stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .findFirst().orElse(null);
    }

    /**
     * return all Persons by Address
     */
    public Set<Person> findAllByAddress(String address) {
        return persons.stream()
                .filter(p -> p.getAddress().equals(address))
                .collect(Collectors.toSet());
    }

    /**
     * return all Persons by LastName
     */
    public Set<Person> findAllByLastName(String lastName) {
        return persons.stream()
                .filter(p -> p.getLastName().equals(lastName))
                .collect(Collectors.toSet());
    }

    /**
     * return all Persons by City
     */
    public Set<Person> findAllByCity(String city) {
        return persons.stream()
                .filter(p -> p.getCity().equals(city))
                .collect(Collectors.toSet());
    }
}
