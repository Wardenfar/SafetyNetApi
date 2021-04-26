package com.safetynet.api;

import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.service.DataImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.InputStream;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    /**
     * Load database from "data.json" at startup
     */
    @Bean
    @Profile("!test")
    CommandLineRunner load(DataImportService importService, FireStationRepository fireStationRepo, PersonRepository personRepo, MedicalRecordRepository medicalRecordRepo) {
        return args -> {
            log.info("Loading 'data.json' ...");

            // read the resource file "data.json"
            InputStream fileInput = readJsonFile("data.json");

            // Import using the service
            importService.importJson(fileInput);
        };
    }

    /**
     * Return the InputStream form the resource File
     */
    public InputStream readJsonFile(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }
}
