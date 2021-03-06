package com.safetynet.api;

import com.jayway.jsonpath.JsonPath;
import com.safetynet.api.entity.FireStation;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FloodStationsRouteTests {

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
    public void testFloodStation_OneEntry() throws Exception {
        FireStation fireStation = fireStationRepo.findOneByAddress("506 rue Losange");

        List<Person> persons = new ArrayList<>();
        persons.add(personRepo.findOneByFirstNameAndLastName("Test1", "Example"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Test2", "Example"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Test3", "Example"));
        persons.add(personRepo.findOneByFirstNameAndLastName("Pierre", "Paul"));

        Map<FireStation, Map<String, List<Person>>> fireStations = new HashMap<>();
        fireStations.put(fireStation, buildPersonByAddress(persons));

        makeFloodStationsRequestSuccess(Arrays.asList("1"), fireStations);
    }

    @Test
    public void testFloodStation_TwoEntries() throws Exception {
        FireStation fs1 = fireStationRepo.findOneByAddress("506 rue Losange");
        FireStation fs2 = fireStationRepo.findOneByAddress("102 rue Triangle");

        List<Person> persons1 = new ArrayList<>();
        persons1.add(personRepo.findOneByFirstNameAndLastName("Test1", "Example"));
        persons1.add(personRepo.findOneByFirstNameAndLastName("Test2", "Example"));
        persons1.add(personRepo.findOneByFirstNameAndLastName("Test3", "Example"));
        persons1.add(personRepo.findOneByFirstNameAndLastName("Pierre", "Paul"));

        List<Person> persons2 = new ArrayList<>();
        persons2.add(personRepo.findOneByFirstNameAndLastName("Henri", "Paul"));
        persons2.add(personRepo.findOneByFirstNameAndLastName("Jean", "Paul"));

        Map<FireStation, Map<String, List<Person>>> fireStations = new HashMap<>();
        fireStations.put(fs1, buildPersonByAddress(persons1));
        fireStations.put(fs2, buildPersonByAddress(persons2));

        makeFloodStationsRequestSuccess(Arrays.asList("1", "2"), fireStations);
    }

    @Test
    public void testFireAlert_Empty() throws Exception {
        Map<FireStation, Map<String, List<Person>>> fireStations = new HashMap<>();

        makeFloodStationsRequestSuccess(Arrays.asList(), fireStations);
    }

    @Test
    public void testFloodStations_NotFound_1() throws Exception {
        makeFloodStationsRequestFail(Arrays.asList("1000"));
    }

    @Test
    public void testFloodStations_NotFound_2() throws Exception {
        makeFloodStationsRequestFail(Arrays.asList("1", "2", "3", "-1"));
    }

    public void makeFloodStationsRequestSuccess(List<String> stations, Map<FireStation, Map<String, List<Person>>> fireStations) throws Exception {
        String stationsParam = String.join(",", stations);

        ResultActions result = mvc.perform(
                get("/flood/stations?stations=" + stationsParam).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        MvcResult response = result.andReturn();

        result.andExpect(jsonPath("$.fireStations", hasSize(fireStations.size())));

        verifyFireStations(fireStations, result, response);
    }

    private void verifyFireStations(Map<FireStation, Map<String, List<Person>>> fireStations, ResultActions result, MvcResult response) throws Exception {
        for (int i = 0; i < fireStations.size(); i++) {
            String station = JsonPath.read(response.getResponse().getContentAsString(), "$.fireStations[" + i + "].fireStation.station");
            FireStation fireStation = fireStations.keySet().stream().filter(fs -> fs.getStation().equals(station)).findFirst().orElseThrow();
            Map<String, List<Person>> personsByAddress = fireStations.get(fireStation);

            result.andExpect(jsonPath("$.fireStations[" + i + "].fireStation.address", is(fireStation.getAddress())));
            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress.*", hasSize(personsByAddress.size())));

            verifyPersonsByAddress(result, response, i, personsByAddress);
        }
    }

    private void verifyPersonsByAddress(ResultActions result, MvcResult response, int i, Map<String, List<Person>> personsByAddress) throws Exception {
        for (Map.Entry<String, List<Person>> entry : personsByAddress.entrySet()) {
            String address = entry.getKey();
            List<Person> persons = entry.getValue();

            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "']", hasSize(persons.size())));

            verifyPersons(result, response, i, address, persons);
        }
    }

    private void verifyPersons(ResultActions result, MvcResult response, int i, String address, List<Person> persons) throws Exception {
        for (int k = 0; k < persons.size(); k++) {

            String firstName = JsonPath.read(response.getResponse().getContentAsString(), "$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].firstName");
            String lastName = JsonPath.read(response.getResponse().getContentAsString(), "$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].lastName");

            Person person = persons.stream()
                    .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                    .findFirst().orElseThrow();

            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].firstName", is(person.getFirstName())));
            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].lastName", is(person.getLastName())));
            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].age", is(person.ageJson())));
            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].phone", is(person.getPhone())));

            verifyMedicalRecord(result, i, address, k, person);
        }
    }

    private void verifyMedicalRecord(ResultActions result, int i, String address, int k, Person person) throws Exception {
        if (person.getMedicalRecord() != null) {
            String[] allergies = person.getMedicalRecord().getAllergies().toArray(new String[0]);
            String[] medications = person.getMedicalRecord().getMedications().toArray(new String[0]);

            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].medicalRecord").exists());
            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].medicalRecord.birthdate", is(person.getMedicalRecord().getBirthdate())));

            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].medicalRecord.allergies", hasSize(allergies.length)));
            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].medicalRecord.allergies", containsInAnyOrder(allergies)));

            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].medicalRecord.medications", hasSize(medications.length)));
            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].medicalRecord.medications", containsInAnyOrder(medications)));
        } else {
            result.andExpect(jsonPath("$.fireStations[" + i + "].personsByAddress['" + address + "'][" + k + "].medicalRecord").doesNotExist());
        }
    }

    public void makeFloodStationsRequestFail(List<String> stations) throws Exception {
        String stationsParam = String.join(",", stations);

        ResultActions result = mvc.perform(
                get("/flood/stations?stations=" + stationsParam).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError());
    }

    public Map<String, List<Person>> buildPersonByAddress(List<Person> persons) {
        return persons.stream().collect(Collectors.groupingBy(Person::getAddress));
    }
}