package com.safetynet.api.rest.firestation;


import com.safetynet.api.ApiApplication;
import com.safetynet.api.model.delete.DeleteFireStationModel;
import com.safetynet.api.model.rest.RestFireStationModel;
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

import static com.safetynet.api.rest.firestation.FireStationRestUtil.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FireStationRestTests {

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
    public void postFireStation() throws Exception {
        RestFireStationModel model1 = new RestFireStationModel("1", "50 rue la Paix");
        RestFireStationModel model2 = new RestFireStationModel("2", "21 avenue du Boulevard");

        postFireStationSuccess(mvc, fireStationRepo, model1);
        postFireStationSuccess(mvc, fireStationRepo, model2);
    }

    @Test
    public void postFireStationFail_exists() throws Exception {
        RestFireStationModel model1 = new RestFireStationModel("1", "506 rue Losange");
        RestFireStationModel model2 = new RestFireStationModel("2", "102 rue Triangle");

        postFireStationFail(mvc, fireStationRepo, model1);
        postFireStationFail(mvc, fireStationRepo, model2);
    }

    @Test
    public void postFireStationFail_exists_2() throws Exception {
        RestFireStationModel model = new RestFireStationModel("15", "502 rue Point");

        postFireStationSuccess(mvc, fireStationRepo, model);
        postFireStationFail(mvc, fireStationRepo, model);
    }

    @Test
    public void postFireStationFail_station_null() throws Exception {
        RestFireStationModel model = new RestFireStationModel("105", null);

        postFireStationFail(mvc, fireStationRepo, model);
    }

    @Test
    public void putFireStation() throws Exception {
        RestFireStationModel model1 = new RestFireStationModel("101", "506 rue Losange");
        RestFireStationModel model2 = new RestFireStationModel("102", "102 rue Triangle");

        putFireStationSuccess(mvc, fireStationRepo, model1);
        putFireStationSuccess(mvc, fireStationRepo, model2);
    }

    @Test
    public void putFireStationFail_notExist() throws Exception {
        RestFireStationModel model1 = new RestFireStationModel("10", "not exists");
        RestFireStationModel model2 = new RestFireStationModel("12", "same");
        RestFireStationModel model3 = new RestFireStationModel("15", "same 2");

        putFireStationFail(mvc, fireStationRepo, model1);
        putFireStationFail(mvc, fireStationRepo, model2);
        putFireStationFail(mvc, fireStationRepo, model3);
    }

    @Test
    public void deleteFireStation() throws Exception {
        DeleteFireStationModel model1 = new DeleteFireStationModel("51 rue Rectangle");
        DeleteFireStationModel model2 = new DeleteFireStationModel("21 rue Droite");

        deleteFireStationSuccess(mvc, personRepo, fireStationRepo, model1);
        deleteFireStationSuccess(mvc, personRepo, fireStationRepo, model2);
    }

    @Test
    public void deleteFireStation_withPersons() throws Exception {
        DeleteFireStationModel model1 = new DeleteFireStationModel("506 rue Losange");
        DeleteFireStationModel model2 = new DeleteFireStationModel("102 rue Triangle");

        deleteFireStationFail(mvc, fireStationRepo, model1);
        deleteFireStationFail(mvc, fireStationRepo, model2);
    }

    @Test
    public void deleteFireStationFail_notExist() throws Exception {
        DeleteFireStationModel model1 = new DeleteFireStationModel("not exists");
        DeleteFireStationModel model2 = new DeleteFireStationModel("same");

        deleteFireStationFail(mvc, fireStationRepo, model1);
        deleteFireStationFail(mvc, fireStationRepo, model2);
    }

    @Test
    public void deleteFireStationFail_station_null() throws Exception {
        DeleteFireStationModel model = new DeleteFireStationModel(null);

        deleteFireStationFail(mvc, fireStationRepo, model);
    }
}
