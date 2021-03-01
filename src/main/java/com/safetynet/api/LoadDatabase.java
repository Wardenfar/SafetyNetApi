package com.safetynet.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.MedicalRecord;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    /**
     * Load database from "data.json" at startup
     */
    @Bean
    @Profile("!test")
    CommandLineRunner load(FireStationRepository fireStationRepo, PersonRepository personRepo, MedicalRecordRepository medicalRecordRepo) {
        return args -> {
            log.info("Loading 'data.json' ...");

            // read the resource file "data.json"
            InputStream fileInput = readJsonFile("data.json");
            // parse it as JSON
            JsonNode json = loadJsonData(fileInput);

            // Process FireStations
            processFireStations(json.get("firestations"), fireStationRepo);
            // Process Persons
            processPersons(json.get("persons"), personRepo, fireStationRepo);
            // Process MedicalRecords
            processMedicalRecords(json.get("medicalrecords"), medicalRecordRepo, personRepo);

            // Print some stats
            log.info("Loaded !");
            log.info("FireStations    : " + fireStationRepo.count());
            log.info("Persons         : " + personRepo.count());
            log.info("Medical Records : " + medicalRecordRepo.count());
        };
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
                log.error("No fire station found for address : " + person.getAddress());
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
                log.error("No person found for name : " + personFirstName + " " + personLastName);
                continue;
            }
            medicalRecord.setPerson(person);

            medicalRecordRepo.add(medicalRecord);
        }
    }

    /**
     * Return the InputStream form the resource File
     */
    public InputStream readJsonFile(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }

    /**
     * Load the json Data from an InputStream
     */
    public JsonNode loadJsonData(InputStream fileInput) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(fileInput, JsonNode.class);
    }
}
