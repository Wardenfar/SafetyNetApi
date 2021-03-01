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
    public void update(MedicalRecord entity) {

    }

    @Override
    public void remove(MedicalRecord entity) {

    }

    @Override
    public void add(MedicalRecord medicalRecord) {
        // The person property is required
        assert medicalRecord.getPerson() != null;
        medicalRecords.add(medicalRecord);

        // Set back reference
        Person person = medicalRecord.getPerson();
        person.setMedicalRecord(medicalRecord);
    }

    /**
     * return all MedicalRecord in this repository
     */
    public Set<MedicalRecord> findAll() {
        return Collections.unmodifiableSet(medicalRecords);
    }
}
