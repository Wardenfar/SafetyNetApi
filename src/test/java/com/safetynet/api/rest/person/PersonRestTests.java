package com.safetynet.api.rest.person;

import com.safetynet.api.ApiApplication;
import com.safetynet.api.model.delete.DeletePersonModel;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.safetynet.api.rest.person.PersonRestUtil.*;

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

        postPersonSuccess(mvc, personRepo, model1);
        postPersonSuccess(mvc, personRepo, model2);
        postPersonSuccess(mvc, personRepo, model3);
    }

    @Test
    public void postPersonFail_sameName() throws Exception {
        RestPersonModel fail = new RestPersonModel("2", "Test1", "Example", "address", "city", "00112", "0112457889", "test@gmail.com");
        postPersonFail(mvc, personRepo, fail);
    }

    @Test
    public void postPersonFail_firstName_null() throws Exception {
        RestPersonModel fail = new RestPersonModel("2", null, "Last", "address", "city", "00112", "0112457889", "test@gmail.com");
        postPersonFail(mvc, personRepo, fail);
    }

    @Test
    public void postPersonFail_lastName_null() throws Exception {
        RestPersonModel fail = new RestPersonModel("2", "AddFail", null, "address", "city", "00112", "0112457889", "test@gmail.com");
        postPersonFail(mvc, personRepo, fail);
    }

    @Test
    public void postPersonFail_fireStation_null() throws Exception {
        RestPersonModel fail = new RestPersonModel(null, "AddFail", "Last", "address", "city", "00112", "0112457889", "test@gmail.com");
        postPersonFail(mvc, personRepo, fail);
    }

    @Test
    public void postPersonFail_fireStation_doesNotExists() throws Exception {
        RestPersonModel fail = new RestPersonModel("100", "Add5", "Last", "address", "city", "00112", "0112457889", "test@gmail.com");
        postPersonFail(mvc, personRepo, fail);
    }

    @Test
    public void putPersonSuccess_change_oneField() throws Exception {
        RestPersonModel model1 = new RestPersonModel(null, "Test1", "Example", "new address", null, null, null, null);
        RestPersonModel model2 = new RestPersonModel(null, "Test1", "Example", null, "new city", null, null, null);
        RestPersonModel model3 = new RestPersonModel(null, "Test1", "Example", null, null, "new zip", null, null);
        RestPersonModel model4 = new RestPersonModel(null, "Test1", "Example", null, null, null, "new phone", null);
        RestPersonModel model5 = new RestPersonModel(null, "Test1", "Example", null, null, null, null, "new email");

        putPersonSuccess(mvc, personRepo, model1);
        putPersonSuccess(mvc, personRepo, model2);
        putPersonSuccess(mvc, personRepo, model3);
        putPersonSuccess(mvc, personRepo, model4);
        putPersonSuccess(mvc, personRepo, model5);
    }

    @Test
    public void putPersonSuccess_change_multipleField() throws Exception {
        RestPersonModel model1 = new RestPersonModel(null, "Test2", "Example", "new address", null, "new zip", null, null);
        RestPersonModel model2 = new RestPersonModel(null, "Test2", "Example", null, "new city", null, "new phone", null);
        RestPersonModel model3 = new RestPersonModel(null, "Test2", "Example", "new address", null, "new zip", null, "new email");

        putPersonSuccess(mvc, personRepo, model1);
        putPersonSuccess(mvc, personRepo, model2);
        putPersonSuccess(mvc, personRepo, model3);
    }

    @Test
    public void putPersonSuccess_change_allFields() throws Exception {
        RestPersonModel model = new RestPersonModel(null, "Test2", "Example", "new address", "new city", "new zip", "new phone", "new email");
        putPersonSuccess(mvc, personRepo, model);
    }

    @Test
    public void putPersonFail_noChange() throws Exception {
        RestPersonModel model1 = new RestPersonModel(null, "Test1", "Example", null, null, null, null, null);
        putPersonFail(mvc, personRepo, model1);
    }

    @Test
    public void putPersonFail_firstName_null() throws Exception {
        RestPersonModel model1 = new RestPersonModel(null, null, "Example", "new address", null, null, null, null);
        putPersonFail(mvc, personRepo, model1);
    }

    @Test
    public void putPersonFail_lastName_null() throws Exception {
        RestPersonModel model1 = new RestPersonModel(null, "Test1", null, "new address", null, null, null, null);
        putPersonFail(mvc, personRepo, model1);
    }

    @Test
    public void deletePerson() throws Exception {
        DeletePersonModel model1 = new DeletePersonModel("Test3", "Example");
        deletePersonSuccess(mvc, personRepo, model1);
    }

    @Test
    public void deletePersonFail_MedicalRecord_notNull() throws Exception {
        DeletePersonModel model1 = new DeletePersonModel("Test1", "Example");
        deletePersonFail(mvc, personRepo, model1);
    }

    @Test
    public void deletePersonFail_notExist1() throws Exception {
        DeletePersonModel model = new DeletePersonModel("Test3", "Example");
        deletePersonSuccess(mvc, personRepo, model);
        deletePersonFail(mvc, personRepo, model);
    }

    @Test
    public void deletePersonFail_notExist2() throws Exception {
        DeletePersonModel model = new DeletePersonModel("Not", "Exists");
        deletePersonFail(mvc, personRepo, model);
    }

}
