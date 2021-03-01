package com.safetynet.api;

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
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommunityEmailRouteTests {

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
    public void testCommunityEmail_1() throws Exception {
        List<String> emails = Arrays.asList("test1@gmail.com", "test2@gmail.com", "test3@gmail.com", "mail1@gmail.com");
        makeCommunityEmailRequest("Paris", emails);
    }

    @Test
    public void testCommunityEmail_2() throws Exception {
        List<String> emails = Arrays.asList("mail2@gmail.com", "mail3@gmail.com");
        makeCommunityEmailRequest("Lyon", emails);
    }

    @Test
    public void testCommunityEmail_Empty() throws Exception {
        makeCommunityEmailRequest("Toulouse", Arrays.asList());
        makeCommunityEmailRequest("Brest", Arrays.asList());
    }

    public void makeCommunityEmailRequest(String city, List<String> emails) throws Exception {
        String[] emailsArray = emails.toArray(new String[0]);
        mvc.perform(
                get("/communityEmail?city=" + city).contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.emails", hasSize(emails.size())))
                .andExpect(jsonPath("$.emails", containsInAnyOrder(emailsArray)));
    }
}