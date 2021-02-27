package com.safetynet.api;

import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.util.FeedTestDatabase;
import com.safetynet.api.util.ResponseBodyMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonInfoRouteTests {

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
    public void testPersonInfo() throws Exception {
        Person personTest1 = personRepo.findAllByFirstNameAndLastName("Test1", "Example").iterator().next();
        Person personTest2 = personRepo.findAllByFirstNameAndLastName("Test2", "Example").iterator().next();
        Person personTest3 = personRepo.findAllByFirstNameAndLastName("Test3", "Example").iterator().next();
        Person personJean1 = personRepo.findAllByFirstNameAndLastName("Jean1", "Paul").iterator().next();

        makePersonInfoRequest(personTest1.getFirstName(), personTest1.getLastName(), 1, Arrays.asList(personTest1));
        makePersonInfoRequest(personTest2.getFirstName(), personTest2.getLastName(), 1, Arrays.asList(personTest2));
        makePersonInfoRequest(personTest3.getFirstName(), personTest3.getLastName(), 1, Arrays.asList(personTest3));
        makePersonInfoRequest(personJean1.getFirstName(), personJean1.getLastName(), 1, Arrays.asList(personJean1));
    }

    @Test
    public void testPersonInfo_NotFound() throws Exception {
        makePersonInfoRequest("Fake", "Example", 0, Arrays.asList());
        makePersonInfoRequest("Test1", "Fake", 0, Arrays.asList());
    }

    @Test
    public void testPersonInfo_SameName() throws Exception {
        Iterator<Person> iterator = personRepo.findAllByFirstNameAndLastName("Double", "Two").iterator();
        Person personDouble1 = iterator.next();
        Person personDouble2 = iterator.next();

        makePersonInfoRequest(personDouble1.getFirstName(), personDouble1.getLastName(), 2, Arrays.asList(personDouble1, personDouble2));
    }

    @Test
    public void testPersonInfo_ExactlyEqual() throws Exception {
        Person personExact = personRepo.findAllByFirstNameAndLastName("Equal", "Exact").iterator().next();

        makePersonInfoRequest("Equal", "Exact", 1, Arrays.asList(personExact));
    }

    public void makePersonInfoRequest(String firstName, String lastName, int count, List<Person> persons) throws Exception {
        mvc.perform(
                get("/personInfo?firstName=" + firstName + "&lastName=" + lastName).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(count)))
                .andExpect(ResponseBodyMatchers.responseBody().isListEqualInJson(persons, Person.class));
    }
}