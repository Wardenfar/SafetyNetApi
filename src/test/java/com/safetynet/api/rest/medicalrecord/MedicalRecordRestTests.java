package com.safetynet.api.rest.medicalrecord;

import com.safetynet.api.ApiApplication;
import com.safetynet.api.model.delete.DeleteMedicalRecordModel;
import com.safetynet.api.model.rest.RestMedicalRecordModel;
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
import java.util.Arrays;

import static com.safetynet.api.rest.medicalrecord.MedicalRecordRestUtil.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MedicalRecordRestTests {

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
    public void postMedicalRecord() throws Exception {
        RestMedicalRecordModel model1 = new RestMedicalRecordModel(
                "Test3", "Example",
                "01/05/2020", Arrays.asList("doliprane"), Arrays.asList("agoraphobie"));

        postMedicalRecordSuccess(mvc, medicalRecordRepo, model1);
    }

    @Test
    public void postMedicalRecordFail_alreadyHave_aRecord() throws Exception {
        RestMedicalRecordModel model1 = new RestMedicalRecordModel(
                "Test1", "Example",
                "01/05/2020", Arrays.asList("doliprane"), Arrays.asList("agoraphobie"));

        postMedicalRecordFail(mvc, medicalRecordRepo, model1);
    }

    @Test
    public void postMedicalRecordFail_person_notExists() throws Exception {
        RestMedicalRecordModel model1 = new RestMedicalRecordModel(
                "Not", "Exists",
                "01/05/2020", Arrays.asList("doliprane"), Arrays.asList("agoraphobie"));

        postMedicalRecordFail(mvc, medicalRecordRepo, model1);
    }

    @Test
    public void postMedicalRecordFail_firstName_null() throws Exception {
        RestMedicalRecordModel model = new RestMedicalRecordModel(
                null, "Exists",
                "01/05/2020", Arrays.asList("doliprane"), Arrays.asList("agoraphobie"));

        postMedicalRecordFail(mvc, medicalRecordRepo, model);
    }

    @Test
    public void postMedicalRecordFail_lastName_null() throws Exception {
        RestMedicalRecordModel model = new RestMedicalRecordModel(
                "Not", null,
                "01/05/2020", Arrays.asList("doliprane"), Arrays.asList("agoraphobie"));

        postMedicalRecordFail(mvc, medicalRecordRepo, model);
    }

    @Test
    public void putMedicalRecord() throws Exception {
        RestMedicalRecordModel model1 = new RestMedicalRecordModel(
                "Test1", "Example",
                "01/05/2020", null, Arrays.asList("agoraphobie", "new allergies"));
        RestMedicalRecordModel model2 = new RestMedicalRecordModel(
                "Test1", "Example",
                "01/05/2020", Arrays.asList("doliprane", "new medicaments"), null);

        putMedicalRecordSuccess(mvc, medicalRecordRepo, model1);
        putMedicalRecordSuccess(mvc, medicalRecordRepo, model2);
    }

    @Test
    public void putMedicalRecordFail_notExist() throws Exception {
        RestMedicalRecordModel model1 = new RestMedicalRecordModel(
                "Not", "Exist",
                "01/05/2020", null, Arrays.asList("agoraphobie", "new allergies"));
        RestMedicalRecordModel model2 = new RestMedicalRecordModel(
                "Same", "Same",
                "01/05/2020", Arrays.asList("doliprane", "new medicaments"), null);
        RestMedicalRecordModel model3 = new RestMedicalRecordModel(
                null, "Same",
                "01/05/2020", Arrays.asList("doliprane", "new medicaments"), null);
        RestMedicalRecordModel model4 = new RestMedicalRecordModel(
                "Same", null,
                "01/05/2020", Arrays.asList("doliprane", "new medicaments"), null);

        putMedicalRecordFail(mvc, medicalRecordRepo, model1);
        putMedicalRecordFail(mvc, medicalRecordRepo, model2);
        putMedicalRecordFail(mvc, medicalRecordRepo, model3);
        putMedicalRecordFail(mvc, medicalRecordRepo, model4);
    }

    @Test
    public void deleteMedicalRecord() throws Exception {
        DeleteMedicalRecordModel model1 = new DeleteMedicalRecordModel("Test1", "Example");
        DeleteMedicalRecordModel model2 = new DeleteMedicalRecordModel("Test2", "Example");
        DeleteMedicalRecordModel model3 = new DeleteMedicalRecordModel("Pierre", "Paul");

        deleteMedicalRecordSuccess(mvc, medicalRecordRepo, model1);
        deleteMedicalRecordSuccess(mvc, medicalRecordRepo, model2);
        deleteMedicalRecordSuccess(mvc, medicalRecordRepo, model3);
    }

    @Test
    public void deleteMedicalRecordFail_record_notExists() throws Exception {
        DeleteMedicalRecordModel model = new DeleteMedicalRecordModel("Test3", "Example");

        deleteMedicalRecordFail(mvc, medicalRecordRepo, model);
    }

    @Test
    public void deleteMedicalRecordFail_record_notValid() throws Exception {
        DeleteMedicalRecordModel model1 = new DeleteMedicalRecordModel(null, "Example");
        DeleteMedicalRecordModel model2 = new DeleteMedicalRecordModel("Test", null);

        deleteMedicalRecordFail(mvc, medicalRecordRepo, model1);
        deleteMedicalRecordFail(mvc, medicalRecordRepo, model2);
    }
}
