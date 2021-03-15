package com.safetynet.api.repository;

import com.safetynet.api.entity.MedicalRecord;
import com.safetynet.api.entity.Person;

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
        if(entity.getPerson() == null){
            return false;
        }

        MedicalRecord prev = findOneByPerson(entity.getPerson());

        // Prev does not exists
        if(prev == null){
            return false;
        }

        // The person has been updated
        if(!entity.getPerson().equals(prev.getPerson())){
            return false;
        }

        medicalRecords.remove(prev);
        entity.getPerson().setMedicalRecord(entity);
        medicalRecords.add(entity);

        return true;
    }

    @Override
    public boolean remove(MedicalRecord entity) {
        medicalRecords.remove(entity);
        if(entity.getPerson() != null){
            entity.setPerson(null);
        }
        return true;
    }

    @Override
    public boolean add(MedicalRecord medicalRecord) {
        // The person property is required
        if (medicalRecord.getPerson() == null) {
            return false;
        }
        // The person has already a Medical Record
        if (medicalRecord.getPerson().getMedicalRecord() != null && medicalRecord.getPerson().getMedicalRecord() != medicalRecord) {
            return false;
        }

        // Set back-reference
        medicalRecord.getPerson().setMedicalRecord(medicalRecord);

        medicalRecords.add(medicalRecord);

        // Set back reference
        Person person = medicalRecord.getPerson();
        person.setMedicalRecord(medicalRecord);
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
