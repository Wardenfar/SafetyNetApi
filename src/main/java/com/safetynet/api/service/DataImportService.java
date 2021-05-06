package com.safetynet.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.MedicalRecord;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.io.IOException;
import java.io.InputStream;

@Service
public class DataImportService {

    FireStationRepository fireStationRepo;
    MedicalRecordRepository medicalRecordRepo;
    PersonRepository personRepo;

    public DataImportService(FireStationRepository fireStationRepo, MedicalRecordRepository medicalRecordRepo, PersonRepository personRepo) {
        this.fireStationRepo = fireStationRepo;
        this.medicalRecordRepo = medicalRecordRepo;
        this.personRepo = personRepo;
    }

    public void importJson(InputStream fileInput) throws IOException {
        // parse it as JSON
        JsonNode json = loadJsonData(fileInput);

        // Process FireStations
        processFireStations(json.get("firestations"), fireStationRepo);
        // Process Persons
        processPersons(json.get("persons"), personRepo, fireStationRepo);
        // Process MedicalRecords
        processMedicalRecords(json.get("medicalrecords"), medicalRecordRepo, personRepo);

        // Print some stats
        Logger.info("Loaded !");
        Logger.info("FireStations    : " + fireStationRepo.count());
        Logger.info("Persons         : " + personRepo.count());
        Logger.info("Medical Records : " + medicalRecordRepo.count());
    }

    /**
     * Add FireStations to the repository from the Json Array
     */
    private void processFireStations(JsonNode fireStationsJson, FireStationRepository fireStationRepo) {
        for (JsonNode json : fireStationsJson) {
            FireStation fireStation = FireStation.fromJson(json);
            fireStationRepo.add(fireStation);
        }
    }

    /**
     * Add Persons to the repository from the Json Array
     * and link with the FireStations
     */
    private void processPersons(JsonNode personsJson, PersonRepository personRepo, FireStationRepository fireStationRepo) {
        for (JsonNode json : personsJson) {
            Person person = Person.fromJson(json);

            FireStation fireStation = fireStationRepo.findOneByAddress(person.getAddress());
            if (fireStation == null) {
                Logger.error("No fire station found for address : " + person.getAddress());
                continue;
            }
            person.setFireStation(fireStation);

            personRepo.add(person);
        }
    }

    /**
     * Add MedicalRecord to the repository from the Json Array
     * and link with the Person
     */
    private void processMedicalRecords(JsonNode medicalRecords, MedicalRecordRepository medicalRecordRepo, PersonRepository personRepo) {
        for (JsonNode json : medicalRecords) {
            MedicalRecord medicalRecord = MedicalRecord.fromJson(json);

            String personFirstName = json.get("firstName").asText();
            String personLastName = json.get("lastName").asText();

            Person person = personRepo.findOneByFirstNameAndLastName(personFirstName, personLastName);
            if (person == null) {
                Logger.error("No person found for name : " + personFirstName + " " + personLastName);
                continue;
            }
            medicalRecord.setPerson(person);

            medicalRecordRepo.add(medicalRecord);
        }
    }

    /**
     * Load the json Data from an InputStream
     */
    public JsonNode loadJsonData(InputStream fileInput) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(fileInput, JsonNode.class);
    }
}
