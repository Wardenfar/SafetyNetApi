package com.safetynet.api;

import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration
 */
@Configuration
public class AppConfig {

    /**
     * PersonRepository Bean
     */
    @Bean
    public PersonRepository personRepository() {
        return new PersonRepository();
    }

    /**
     * FireStationRepository Bean
     */
    @Bean
    public FireStationRepository fireStationRepository() {
        return new FireStationRepository();
    }

    /**
     * MedicalRecordRepository Bean
     */
    @Bean
    public MedicalRecordRepository medicalRecordRepository() {
        return new MedicalRecordRepository();
    }
}
