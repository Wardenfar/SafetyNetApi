package com.safetynet.api.util;

import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.MedicalRecord;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;

import java.util.Arrays;
import java.util.List;

public class FeedTestDatabase {

    public static void feedDatabase(PersonRepository personRepo, FireStationRepository fireStationRepo, MedicalRecordRepository medicalRecordRepo) {
        FireStation fs1 = createFireStation(fireStationRepo, "1", "506 rue Losange");
        FireStation fs2 = createFireStation(fireStationRepo, "2", "102 rue Triangle");
        FireStation fs3 = createFireStation(fireStationRepo, "3", "51 rue Rectangle");
        FireStation fs4 = createFireStation(fireStationRepo, "4", "21 rue Droite");

        Person p1 = createPerson(personRepo, fs1, "Test1", "Example", "506 rue Losange", "Paris", "75001", "001-001", "test1@gmail.com");
        MedicalRecord mr1 = createMedicalRecord(medicalRecordRepo, p1, "01/25/1956", Arrays.asList("cepes"), Arrays.asList("paracetamol"));
        Person p2 = createPerson(personRepo, fs1, "Test2", "Example", "506 rue Losange", "Paris", "75001", "001-002", "test2@gmail.com");
        MedicalRecord mr2 = createMedicalRecord(medicalRecordRepo, p2, "01/25/2005", Arrays.asList("oeufs", "graines"), Arrays.asList());
        Person p3 = createPerson(personRepo, fs1, "Test3", "Example", "506 rue Losange", "Paris", "75001", "001-003", "test3@gmail.com");
        //MedicalRecord mr3 = createMedicalRecord(medicalRecordRepo, p3, "01/25/1978", Arrays.asList(), Arrays.asList("truc", "machin"));
        Person p4 = createPerson(personRepo, fs1, "Pierre", "Paul", "506 rue Losange", "Paris", "75001", "001-004", "mail1@gmail.com");
        MedicalRecord mr5 = createMedicalRecord(medicalRecordRepo, p4, "01/25/1996", Arrays.asList("lasagne"), Arrays.asList());

        Person p5 = createPerson(personRepo, fs2, "Jean", "Paul", "102 rue Triangle", "Lyon", "99999", "002-001", "mail2@gmail.com");
        MedicalRecord mr4 = createMedicalRecord(medicalRecordRepo, p5, "01/25/2009", Arrays.asList(), Arrays.asList());
        Person p6 = createPerson(personRepo, fs2, "Henri", "Paul", "102 rue Triangle", "Lyon", "99999", "002-002", "mail3@gmail.com");
        MedicalRecord mr6 = createMedicalRecord(medicalRecordRepo, p6, "01/25/1951", Arrays.asList("pizza"), Arrays.asList("doliprane"));

        assert fireStationRepo.count() == 4;
        assert personRepo.count() == 6;
        assert medicalRecordRepo.count() == 5;
    }

    public static FireStation createFireStation(FireStationRepository fireStationRepo, String station, String address) {
        FireStation fireStation = new FireStation();
        fireStation.setStation(station);
        fireStation.setAddress(address);
        fireStationRepo.add(fireStation);
        return fireStation;
    }

    public static MedicalRecord createMedicalRecord(MedicalRecordRepository medicalRecordRepo, Person person, String birthdate, List<String> allergies, List<String> medications) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPerson(person);
        medicalRecord.setBirthdate(birthdate);
        medicalRecord.setAllergies(allergies);
        medicalRecord.setMedications(medications);
        medicalRecordRepo.add(medicalRecord);
        return medicalRecord;
    }

    public static Person createPerson(PersonRepository personRepo, FireStation fireStation, String firstName, String lastName, String address, String city, String zip, String phone, String email) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);
        person.setCity(city);
        person.setZip(zip);
        person.setEmail(email);
        person.setPhone(phone);
        person.setFireStation(fireStation);

        personRepo.add(person);
        return person;
    }
}
