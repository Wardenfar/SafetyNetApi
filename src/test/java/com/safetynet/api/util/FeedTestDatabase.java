package com.safetynet.api.util;

import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;

public class FeedTestDatabase {

    public static void feedDatabase(PersonRepository personRepo, FireStationRepository fireStationRepo, MedicalRecordRepository medicalRecordRepo) {
        FireStation fs1 = createFireStation(fireStationRepo, "1", "506 rue Triangle");
        FireStation fs2 = createFireStation(fireStationRepo, "2", "102 rue Triangle");

        createPerson(personRepo, fs1, "Test1", "Example", "", "", "", "", "");
        createPerson(personRepo, fs1, "Test2", "Example", "", "", "", "", "");
        createPerson(personRepo, fs1, "Test3", "Example", "", "", "", "", "");
        createPerson(personRepo, fs2, "Jean1", "Paul", "", "", "", "", "");

        createPerson(personRepo, fs1, "Double", "Two", "", "Paris", "", "", "");
        createPerson(personRepo, fs1, "Double", "Two", "", "Lyon", "", "", "");

        createPerson(personRepo, fs1, "Equal", "Exact", "Same", "Same", "Same", "", "");
        createPerson(personRepo, fs1, "Equal", "Exact", "Same", "Same", "Same", "", "");

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
