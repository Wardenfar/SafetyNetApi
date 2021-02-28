package com.safetynet.api;

import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.util.FeedTestDatabase;
import com.safetynet.api.util.ResponseBodyMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PhoneAlertRouteTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private FireStationRepository fireStationRepo;

    @Autowired
    private MedicalRecordRepository medicalRecordRepo;

    @Before
    public void beforeEach() {
        FeedTestDatabase.feedDatabase(personRepo, fireStationRepo, medicalRecordRepo);
    }

    @After
    public void afterEach() {
        personRepo.clear();
        fireStationRepo.clear();
        medicalRecordRepo.clear();
    }

    @Test
    public void testPhoneAlert() throws Exception {
        List<String> phones = Arrays.asList("001-001", "001-002", "001-003", "002-001");
        makePhoneAlertRequest("1", phones.size(), phones);

        phones = Arrays.asList("001-004", "002-002");
        makePhoneAlertRequest("2", phones.size(), phones);
    }

    @Test
    public void testPersonInfo_FireStationNotFound() throws Exception {
        mvc.perform(
                get("/phoneAlert?stationNumber=1000").contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testPersonInfo_ExactlyEqual() throws Exception {
        List<String> phones = Arrays.asList("003-001");
        makePhoneAlertRequest("3", phones.size(), phones);
    }


    public void makePhoneAlertRequest(String stationNumber, int count, List<String> phones) throws Exception {
        String[] phonesArray = phones.toArray(new String[0]);
        mvc.perform(
                get("/phoneAlert?stationNumber=" + stationNumber).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.phones", hasSize(count)))
                .andExpect(jsonPath("$.phones", containsInAnyOrder(phonesArray)));
    }
}