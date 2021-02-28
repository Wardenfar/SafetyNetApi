package com.safetynet.api.util;

import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;

public class FeedTestDatabase {

    public static void feedDatabase(PersonRepository personRepo, FireStationRepository fireStationRepo, MedicalRecordRepository medicalRecordRepo) {
        FireStation fs1 = createFireStation(fireStationRepo, "1", "506 rue Losange");
        FireStation fs2 = createFireStation(fireStationRepo, "2", "102 rue Triangle");
        FireStation fs3 = createFireStation(fireStationRepo, "3", "51 rue Rectangle");

        createPerson(personRepo, fs1, "Test1", "Example", "", "", "", "001-001", "");
        createPerson(personRepo, fs1, "Test2", "Example", "", "", "", "001-002", "");
        createPerson(personRepo, fs1, "Test3", "Example", "", "", "", "001-003", "");
        createPerson(personRepo, fs2, "Jean1", "Paul", "", "", "", "001-004", "");

        createPerson(personRepo, fs1, "Double", "Two", "", "Paris", "", "002-001", "");
        createPerson(personRepo, fs2, "Double", "Two", "", "Lyon", "", "002-002", "");

        createPerson(personRepo, fs3, "Equal", "Exact", "Same", "Same", "Same", "003-001", "");
        createPerson(personRepo, fs3, "Equal", "Exact", "Same", "Same", "Same", "003-001", "");

        assert fireStationRepo.count() == 3; // The two last are equals
        assert personRepo.count() == 8 - 1; // The two last are equals
    }

    public static FireStation createFireStation(FireStationRepository fireStationRepo, String station, String address) {
        FireStation fireStation = new FireStation();
        fireStation.setStation(station);
        fireStation.setAddress(address);
        fireStationRepo.add(fireStation);
        return fireStation;
    }

    public static void createPerson(PersonRepository personRepo, FireStation fireStation, String firstName, String lastName, String address, String city, String zip, String phone, String email) {
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
    }
}
