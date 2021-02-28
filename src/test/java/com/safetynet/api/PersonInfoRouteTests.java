package com.safetynet.api;

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
import org.springframework.test.web.servlet.ResultActions;
import static org.hamcrest.Matchers.*;
import java.util.Arrays;

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
        makePersonInfoRequestSuccess(personRepo.findAllByFirstNameAndLastName("Test1", "Example"));
        makePersonInfoRequestSuccess(personRepo.findAllByFirstNameAndLastName("Test2", "Example"));
        makePersonInfoRequestSuccess(personRepo.findAllByFirstNameAndLastName("Test3", "Example"));
        makePersonInfoRequestSuccess(personRepo.findAllByFirstNameAndLastName("Pierre", "Paul"));
        makePersonInfoRequestSuccess(personRepo.findAllByFirstNameAndLastName("Jean", "Paul"));
        makePersonInfoRequestSuccess(personRepo.findAllByFirstNameAndLastName("Henri", "Paul"));
    }

    @Test
    public void testPersonInfo_NotFound() throws Exception {
        makePersonInfoRequestFail("Fake", "Example");
        makePersonInfoRequestFail("Test1", "Fake");
    }

    public void makePersonInfoRequestSuccess(Person person) throws Exception {

        String[] allergies = person.getMedicalRecord().getAllergies().toArray(new String[0]);
        String[] medications = person.getMedicalRecord().getMedications().toArray(new String[0]);

        ResultActions result = mvc.perform(
                get("/personInfo?firstName=" + person.getFirstName() + "&lastName=" + person.getLastName()).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.age", is(person.ageJson())))
                .andExpect(jsonPath("$.email", is(person.getEmail())))
                .andExpect(jsonPath("$.address", is(person.getAddress())))
                .andExpect(jsonPath("$.zip", is(person.getZip())))
                .andExpect(jsonPath("$.city", is(person.getCity())))

                .andExpect(jsonPath("$.medicalRecord").exists())
                .andExpect(jsonPath("$.medicalRecord.birthdate", is(person.getMedicalRecord().getBirthdate())))

                .andExpect(jsonPath("$.medicalRecord.allergies", hasSize(allergies.length)))
                .andExpect(jsonPath("$.medicalRecord.allergies", containsInAnyOrder(allergies)))

                .andExpect(jsonPath("$.medicalRecord.medications", hasSize(medications.length)))
                .andExpect(jsonPath("$.medicalRecord.medications", containsInAnyOrder(medications)))

                .andExpect(jsonPath("$.medicalRecord.person").doesNotExist())
                .andExpect(jsonPath("$.fireStation").doesNotExist());
    }

    public void makePersonInfoRequestFail(String firstName, String lastName) throws Exception {
        ResultActions result = mvc.perform(
                get("/personInfo?firstName=" + firstName + "&lastName=" + lastName).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError());
    }
}