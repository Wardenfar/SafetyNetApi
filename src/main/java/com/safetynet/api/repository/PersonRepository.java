package com.safetynet.api.repository;

import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonRepository extends AbstractRepository<Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonRepository.class);

    private Set<Person> persons = new HashSet<>();

    @Override
    public boolean add(Person person) {
        log.debug("Adding Person : " + person.toString());

        // the fireStation property is Required
        if (person.getFireStation() == null) {
            return false;
        }

        persons.add(person);

        // Set back reference
        FireStation fireStation = person.getFireStation();
        fireStation.addPerson(person);

        return true;
    }

    @Override
    public int count() {
        return persons.size();
    }

    @Override
    public void clear() {
        persons.clear();
    }

    @Override
    public boolean update(Person entity) {
        log.debug("Updating Person : " + entity.toString());

        // Checks not null
        if (entity.getFireStation() == null) {
            log.error("FireStation is null");
            return false;
        }

        // Remove the old
        Person prev = findOneByFirstNameAndLastName(entity.getFirstName(), entity.getLastName());
        prev.getFireStation().removePerson(prev);
        persons.remove(prev);

        // Add the new entity
        persons.add(entity);
        FireStation fireStation = entity.getFireStation();
        fireStation.addPerson(entity);
        return true;
    }

    @Override
    public boolean remove(Person entity) {
        log.debug("Removing Person : " + entity.toString());

        if (entity.getMedicalRecord() != null) {
            log.error("MedicalRecord is not null");
            return false;
        }
        entity.getFireStation().removePerson(entity);
        persons.remove(entity);
        return true;
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
