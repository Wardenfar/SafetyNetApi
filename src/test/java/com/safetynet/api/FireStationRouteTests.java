package com.safetynet.api;

import com.jayway.jsonpath.JsonPath;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.util.DateUtils;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FireStationRouteTests {

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
        DateUtils.setFakeCurrentDate(LocalDate.of(2021, 1, 1));
        FeedTestDatabase.feedDatabase(personRepo, fireStationRepo, medicalRecordRepo);
    }

    @After
    public void afterEach() {
        personRepo.clear();
        fireStationRepo.clear();
        medicalRecordRepo.clear();
    }

    @Test
    public void testFireStation_1() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(personRepo.findOneByFirstNameAndLastName("Test1", "Example"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Test2", "Example"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Test3", "Example"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Pierre", "Paul"));

        makeFireStationRequestSuccess("1", persons, 2, 1, 1);
    }

    @Test
    public void testFireStation_2() throws Exception {
        List<Person> persons = new ArrayList<>();
        persons.add(personRepo.findOneByFirstNameAndLastName("Jean", "Paul"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Henri", "Paul"));

        makeFireStationRequestSuccess("2", persons, 1, 1, 0);
    }

    @Test
    public void testFireStation_NotFound() throws Exception {
        makeFireStationRequestFail("1000");
    }

    public void makeFireStationRequestSuccess(String stationNumber, List<Person> persons, int adultCount, int childCount, int unknownCount) throws Exception {
        ResultActions result = mvc.perform(
                get("/firestation?stationNumber=" + stationNumber).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.adultCount", is(adultCount)))
                .andExpect(jsonPath("$.childCount", is(childCount)))
                .andExpect(jsonPath("$.unknownCount", is(unknownCount)))
                .andExpect(jsonPath("$.persons", hasSize(persons.size())));

        MvcResult response = result.andReturn();

        verifyPersons(persons, result, response);
    }

    private void verifyPersons(List<Person> persons, ResultActions result, MvcResult response) throws Exception {
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
            result.andExpect(jsonPath("$.persons[" + i + "].address", is(person.getAddress())));
            result.andExpect(jsonPath("$.persons[" + i + "].zip", is(person.getZip())));
            result.andExpect(jsonPath("$.persons[" + i + "].city", is(person.getCity())));
        }
    }

    public void makeFireStationRequestFail(String stationNumber) throws Exception {
        ResultActions result = mvc.perform(
                get("/firestation?stationNumber=" + stationNumber).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError());
    }
}