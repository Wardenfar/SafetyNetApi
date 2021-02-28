package com.safetynet.api;

import com.jayway.jsonpath.JsonPath;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.util.FeedTestDatabase;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ChildAlertRouteTests {

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
    public void testChildAlert() throws Exception {
        Map<Person, Integer> children = new HashMap<>();
        children.put(personRepo.findOneByFirstNameAndLastName("Test1", "Example"), 2);
        children.put(personRepo.findOneByFirstNameAndLastName("Test2", "Example"), 2);
        children.put(personRepo.findOneByFirstNameAndLastName("Test3", "Example"), 2);
        children.put(personRepo.findOneByFirstNameAndLastName("Pierre", "Paul"), 2);
        makeChildAlertRequestSuccess("506 rue Losange", children);
    }

    @Test
    public void testChildAlert_Empty() throws Exception {
        Map<Person, Integer> children = new HashMap<>();
        makeChildAlertRequestSuccess("506 rue Falsy", children);
    }

    public void makeChildAlertRequestSuccess(String address, Map<Person, Integer> children) throws Exception {
        ResultActions result = mvc.perform(
                get("/childAlert?address=" + address).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.children", hasSize(children.size())));

        MvcResult response = result.andReturn();

        for (int i = 0; i < children.size(); i++) {

            result.andExpect(jsonPath("$.children[" + i + "].child").exists());
            result.andExpect(jsonPath("$.children[" + i + "].family").exists());

            result.andExpect(jsonPath("$.children[" + i + "].child.firstName").exists());
            result.andExpect(jsonPath("$.children[" + i + "].child.lastName").exists());
            result.andExpect(jsonPath("$.children[" + i + "].child.age").exists());

            String firstName = JsonPath.read(response.getResponse().getContentAsString(), "$.children[" + i + "].child.firstName");
            String lastName = JsonPath.read(response.getResponse().getContentAsString(), "$.children[" + i + "].child.lastName");

            Person child = children.keySet()
                    .stream()
                    .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                    .findFirst().orElseThrow();
            int familySize = children.get(child);

            result.andExpect(jsonPath("$.children[" + i + "].child.age", is(child.ageJson())));
            result.andExpect(jsonPath("$.children[" + i + "].family", hasSize(familySize)));

            for (int j = 0; j < familySize; j++) {
                result.andExpect(jsonPath("$.children[" + i + "].family[" + j + "].lastName", is(child.getLastName())));
                result.andExpect(jsonPath("$.children[" + i + "].family[" + j + "].firstName", not(child.getFirstName())));
            }
        }
    }

    public void makePhoneAlertRequestFail(String stationNumber) throws Exception {
        mvc.perform(
                get("/phoneAlert?stationNumber=" + stationNumber).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError());
    }
}