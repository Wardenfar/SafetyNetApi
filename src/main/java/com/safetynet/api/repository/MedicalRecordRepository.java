package com.safetynet.api.repository;

import com.safetynet.api.entity.MedicalRecord;
import com.safetynet.api.entity.Person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MedicalRecordRepository {

    private Set<MedicalRecord> medicalRecords = new HashSet<>();

    public int count() {
        return medicalRecords.size();
    }

    public void add(MedicalRecord medicalRecord) {
        assert medicalRecord.getPerson() != null;
        medicalRecords.add(medicalRecord);
        Person person = medicalRecord.getPerson();
        person.setMedicalRecord(medicalRecord);
    }

    public Set<MedicalRecord> findAll() {
        return Collections.unmodifiableSet(medicalRecords);
    }
}
