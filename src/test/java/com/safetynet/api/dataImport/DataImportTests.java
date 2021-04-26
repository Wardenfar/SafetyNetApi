package com.safetynet.api.dataImport;

import com.safetynet.api.ApiApplication;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.service.DataImportService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DataImportTests {

    @Autowired
    private DataImportService dataImportService;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private FireStationRepository fireStationRepo;

    @Autowired
    private MedicalRecordRepository medicalRecordRepo;

    @After
    public void afterEach() {
        personRepo.clear();
        fireStationRepo.clear();
        medicalRecordRepo.clear();
    }

    @Test
    public void test_data_import() throws IOException {
        InputStream in = readJsonFile("json/success_1.json");
        dataImportService.importJson(in);
        assert fireStationRepo.count() == 11;
        assert personRepo.count() == 23;
        assert medicalRecordRepo.count() == 23;
    }

    @Test(expected = NullPointerException.class)
    public void test_data_import_fireStation_missing_station() throws IOException {
        InputStream in = readJsonFile("json/fail_1.json");
        dataImportService.importJson(in);
    }

    @Test(expected = NullPointerException.class)
    public void test_data_import_fireStation_missing_address() throws IOException {
        InputStream in = readJsonFile("json/fail_2.json");
        dataImportService.importJson(in);
    }

    @Test(expected = NullPointerException.class)
    public void test_data_import_fireStation_missing_all() throws IOException {
        InputStream in = readJsonFile("json/fail_3.json");
        dataImportService.importJson(in);
    }

    @Test()
    public void test_data_import_person_missing_address() throws IOException {
        InputStream in = readJsonFile("json/success_2.json");
        dataImportService.importJson(in);
        assert fireStationRepo.count() == 3;
        assert personRepo.count() == 2;
        assert medicalRecordRepo.count() == 0;
    }

    @Test()
    public void test_data_import_fireStation_same_address() throws IOException {
        InputStream in = readJsonFile("json/success_3.json");
        dataImportService.importJson(in);
        assert fireStationRepo.count() == 3;
        assert personRepo.count() == 0;
        assert medicalRecordRepo.count() == 0;
    }

    /**
     * Return the InputStream form the resource File
     */
    public InputStream readJsonFile(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }
}
