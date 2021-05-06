package com.safetynet.api.repository;

import com.safetynet.api.entity.MedicalRecord;
import com.safetynet.api.entity.Person;
import org.tinylog.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The repository implementation for MedicalRecord Entity
 */
public class MedicalRecordRepository extends AbstractRepository<MedicalRecord> {

    // All entities in this repository
    private Set<MedicalRecord> medicalRecords = new HashSet<>();

    @Override
    public int count() {
        return medicalRecords.size();
    }

    @Override
    public void clear() {
        medicalRecords.clear();
    }

    @Override
    public boolean update(MedicalRecord entity) {
        Logger.debug("Updating MedicalRecord : " + entity.toString());

        if(entity.getPerson() == null){
            Logger.error("Person is null");
            return false;
        }

        MedicalRecord prev = findOneByPerson(entity.getPerson());

        // Prev does not exists
        if(prev == null){
            Logger.error("Previous does not exist");
            return false;
        }

        // The person has been updated
        if(!entity.getPerson().equals(prev.getPerson())){
            Logger.error("Could not update the Person");
            return false;
        }

        medicalRecords.remove(prev);
        entity.getPerson().setMedicalRecord(entity);
        medicalRecords.add(entity);

        return true;
    }

    @Override
    public boolean remove(MedicalRecord entity) {
        Logger.debug("Removing MedicalRecord : " + entity.toString());

        medicalRecords.remove(entity);
        if(entity.getPerson() != null){
            entity.setPerson(null);
        }
        return true;
    }

    @Override
    public boolean add(MedicalRecord entity) {
        Logger.debug("Adding MedicalRecord : " + entity.toString());

        // The person property is required
        if (entity.getPerson() == null) {
            Logger.error("The person is required");
            return false;
        }
        // The person has already a Medical Record
        if (entity.getPerson().getMedicalRecord() != null && entity.getPerson().getMedicalRecord() != entity) {
            Logger.error("The person has already a Medical Record");
            return false;
        }

        // Set back-reference
        entity.getPerson().setMedicalRecord(entity);

        medicalRecords.add(entity);

        // Set back reference
        Person person = entity.getPerson();
        person.setMedicalRecord(entity);
        return true;
    }

    public MedicalRecord findOneByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream().filter(m ->
                m.getPerson().getFirstName().equals(firstName)
                        && m.getPerson().getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    public MedicalRecord findOneByPerson(Person person) {
        return medicalRecords.stream().filter(m ->
                m.getPerson().equals(person))
                .findFirst()
                .orElse(null);
    }

    /**
     * return all MedicalRecord in this repository
     */
    public Set<MedicalRecord> findAll() {
        return Collections.unmodifiableSet(medicalRecords);
    }

}
