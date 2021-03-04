package com.safetynet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.safetynet.api.model.rest.RestPersonModel;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonRestTests {

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
    public void postPerson() throws Exception {
        RestPersonModel model1 = new RestPersonModel("1", "Add1", "Last", "address", "city", "00112", "0112457889", "test@gmail.com");
        RestPersonModel model2 = new RestPersonModel("2", "Add2", "Last", "address", "city", "00112", "0112457889", "test@gmail.com");
        RestPersonModel model3 = new RestPersonModel("3", "Add3", "Last", "address", "city", "00112", "0112457889", "test@gmail.com");

        RestPersonModel fail1 = new RestPersonModel("2", "Add1", "Last", "address", "city", "00112", "0112457889", "test@gmail.com");
        RestPersonModel fail2 = new RestPersonModel("2", null, "Last", "address", "city", "00112", "0112457889", "test@gmail.com");
        RestPersonModel fail3 = new RestPersonModel("2", "AddFail", null, "address", "city", "00112", "0112457889", "test@gmail.com");
        RestPersonModel fail4 = new RestPersonModel(null, "AddFail", "Last", "address", "city", "00112", "0112457889", "test@gmail.com");

        postPersonSuccess(model1);
        postPersonSuccess(model2);
        postPersonSuccess(model3);

        postPersonFail(fail1);
        postPersonFail(fail2);
        postPersonFail(fail3);
        postPersonFail(fail4);
    }

    public void postPersonSuccess(RestPersonModel model) throws Exception {
        int countBefore = personRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)));

        int countAfter = personRepo.count();

        assert countBefore + 1 == countAfter;
    }

    public void postPersonFail(RestPersonModel model) throws Exception {
        int countBefore = personRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)));

        int countAfter = personRepo.count();

        assert countBefore == countAfter;
    }
}
