package com.safetynet.api;

import com.jayway.jsonpath.JsonPath;
import com.safetynet.api.entity.FireStation;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FireAlertRouteTests {

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
    public void testFireAlert_1() throws Exception {
        FireStation fireStation = fireStationRepo.findOneByStation("1");
        List<Person> persons = new ArrayList<>();
        persons.add(personRepo.findOneByFirstNameAndLastName("Test1", "Example"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Test2", "Example"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Test3", "Example"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Pierre", "Paul"));

        makeFireAlertRequestSuccess("506 rue Losange", fireStation, persons);
    }

    @Test
    public void testFireAlert_2() throws Exception {
        FireStation fireStation = fireStationRepo.findOneByStation("2");
        List<Person> persons = new ArrayList<>();
        persons.add(personRepo.findOneByFirstNameAndLastName("Jean", "Paul"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Henri", "Paul"));

        makeFireAlertRequestSuccess("102 rue Triangle", fireStation, persons);
    }

    @Test
    public void testFireAlert_Empty() throws Exception {
        FireStation fireStation = fireStationRepo.findOneByStation("3");
        List<Person> persons = new ArrayList<>();

        makeFireAlertRequestSuccess("51 rue Rectangle", fireStation, persons);
    }

    @Test
    public void testFireAlert_NotFound() throws Exception {
        makeFireAlertRequestFail("506 rue Falsy");
    }

    public void makeFireAlertRequestSuccess(String address, FireStation fireStation, List<Person> persons) throws Exception {
        ResultActions result = mvc.perform(
                get("/fire?address=" + address).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fireStation").exists())
                .andExpect(jsonPath("$.persons", hasSize(persons.size())));

        MvcResult response = result.andReturn();

        result.andExpect(jsonPath("$.fireStation.station", is(fireStation.getStation())));
        result.andExpect(jsonPath("$.fireStation.address", is(fireStation.getAddress())));

        for (int i = 0; i < persons.size(); i++) {

            String firstName = JsonPath.read(response.getResponse().getContentAsString(), "$.persons[" + i + "].firstName");
            String lastName = JsonPath.read(response.getResponse().getContentAsString(), "$.persons[" + i + "].lastName");

            Person person = persons.stream()
                    .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                    .findFirst().orElseThrow();

            result.andExpect(jsonPath("$.persons[" + i + "].firstName", is(person.getFirstName())));
            result.andExpect(jsonPath("$.persons[" + i + "].lastName", is(person.getLastName())));
            result.andExpect(jsonPath("$.persons[" + i + "].age", is(person.ageJson())));
            result.andExpect(jsonPath("$.persons[" + i + "].phone", is(person.getPhone())));

            if (person.getMedicalRecord() != null) {
                String[] allergies = person.getMedicalRecord().getAllergies().toArray(new String[0]);
                String[] medications = person.getMedicalRecord().getMedications().toArray(new String[0]);
                result.andExpect(jsonPath("$.persons[" + i + "].medicalRecord").exists());
                result.andExpect(jsonPath("$.persons[" + i + "].medicalRecord.birthdate", is(person.getMedicalRecord().getBirthdate())));

                result.andExpect(jsonPath("$.persons[" + i + "].medicalRecord.allergies", hasSize(allergies.length)));
                result.andExpect(jsonPath("$.persons[" + i + "].medicalRecord.allergies", containsInAnyOrder(allergies)));

                result.andExpect(jsonPath("$.persons[" + i + "].medicalRecord.medications", hasSize(medications.length)));
                result.andExpect(jsonPath("$.persons[" + i + "].medicalRecord.medications", containsInAnyOrder(medications)));
            }else{
                result.andExpect(jsonPath("$.persons[" + i + "].medicalRecord").doesNotExist());
            }
        }
    }

    public void makeFireAlertRequestFail(String address) throws Exception {
        ResultActions result = mvc.perform(
                get("/fire?address=" + address).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError());
    }
}