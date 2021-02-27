package com.safetynet.api;

import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PersonRepository personRepository() {
        return new PersonRepository();
    }

    @Bean
    public FireStationRepository fireStationRepository() {
        return new FireStationRepository();
    }

    @Bean
    public MedicalRecordRepository medicalRecordRepository() {
        return new MedicalRecordRepository();
    }
}
