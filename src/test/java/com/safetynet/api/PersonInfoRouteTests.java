package com.safetynet.api;

import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.util.ResponseBodyMatchers;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @AfterEach
    public void afterEach() {
        personRepo.clear();
        fireStationRepo.clear();
        medicalRecordRepo.clear();
    }

    @Test
    public void testPersonInfo() throws Exception {
        Map<String, Person> persons = feedDatabase();

        makePersonInfoRequest("Test1", "Example", 1, Arrays.asList(persons.get("Test1_Example")));
        makePersonInfoRequest("Test2", "Example", 1, Arrays.asList(persons.get("Test2_Example")));
        makePersonInfoRequest("Test3", "Example", 1, Arrays.asList(persons.get("Test3_Example")));
        makePersonInfoRequest("Jean1", "Paul", 1, Arrays.asList(persons.get("Jean1_Paul")));
    }

    @Test
    public void testPersonInfo_NotFound() throws Exception {
        Map<String, Person> persons = feedDatabase();

        makePersonInfoRequest("Fake", "Example", 0, Arrays.asList());
        makePersonInfoRequest("Test1", "Fake", 0, Arrays.asList());
    }

    @Test
    public void testPersonInfo_SameName() throws Exception {
        Map<String, Person> persons = feedDatabase();

        makePersonInfoRequest("Double", "Two", 2, Arrays.asList(persons.get("Double_Two"), persons.get("Double_Two_2")));
    }

    @Test
    public void testPersonInfo_ExactlyEqual() throws Exception {
        Map<String, Person> persons = feedDatabase();

        makePersonInfoRequest("Equal", "Exact", 1, Arrays.asList(persons.get("Equal_Exact")));
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

    public Map<String, Person> feedDatabase() {
        FireStation fs1 = createFireStation("1", "506 rue Triangle");
        FireStation fs2 = createFireStation("2", "102 rue Triangle");

        Map<String, Person> persons = new HashMap<>();

        createPerson(persons, fs1, "Test1", "Example", "", "", "", "", "");
        createPerson(persons, fs1, "Test2", "Example", "", "", "", "", "");
        createPerson(persons, fs1, "Test3", "Example", "", "", "", "", "");
        createPerson(persons, fs2, "Jean1", "Paul", "", "", "", "", "");

        createPerson(persons, fs1, "Double", "Two", "", "Paris", "", "", "");
        createPerson(persons, fs1, "Double", "Two", "", "Lyon", "", "", "");

        createPerson(persons, fs1, "Equal", "Exact", "Same", "Same", "Same", "", "");
        createPerson(persons, fs1, "Equal", "Exact", "Same", "Same", "Same", "", "");

        assert personRepo.count() == persons.size() - 1;

        return persons;
    }

    public FireStation createFireStation(String station, String address) {
        FireStation fireStation = new FireStation();
        fireStation.setStation(station);
        fireStation.setAddress(address);
        fireStationRepo.add(fireStation);
        return fireStation;
    }

    public Person createPerson(Map<String, Person> persons, FireStation fireStation, String firstName, String lastName, String address, String city, String zip, String phone, String email) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);
        person.setCity(city);
        person.setZip(zip);
        person.setEmail(email);
        person.setPhone(phone);
        person.setFireStation(fireStation);

        String key = firstName+"_"+lastName;
        if(persons.containsKey(key)) {
            int i = 2;
            while (persons.containsKey(key + "_" + i)) {
                i++;
            }
            persons.put(key + "_" + i, person);
        }else{
            persons.put(key, person);
        }

        personRepo.add(person);
        return person;
    }
}