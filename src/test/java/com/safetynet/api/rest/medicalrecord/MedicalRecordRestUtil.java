package com.safetynet.api.rest.medicalrecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.safetynet.api.entity.MedicalRecord;
import com.safetynet.api.model.delete.DeleteMedicalRecordModel;
import com.safetynet.api.model.rest.RestMedicalRecordModel;
import com.safetynet.api.repository.MedicalRecordRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MedicalRecordRestUtil {

    public static void postMedicalRecordSuccess(MockMvc mvc, MedicalRecordRepository medicalRecordRepo, RestMedicalRecordModel model) throws Exception {
        int countBefore = medicalRecordRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                post("/medicalrecord").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.method", is("POST")));

        int countAfter = medicalRecordRepo.count();

        assert countBefore + 1 == countAfter;
        verifyNewMedicalRecord(medicalRecordRepo, model);
    }

    public static void putMedicalRecordSuccess(MockMvc mvc, MedicalRecordRepository medicalRecordRepo, RestMedicalRecordModel model) throws Exception {
        int countBefore = medicalRecordRepo.count();
        MedicalRecord beforeClone = medicalRecordRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName())
                .clone();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                put("/medicalrecord").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.method", is("PUT")));

        int countAfter = medicalRecordRepo.count();

        assert countBefore == countAfter;

        MedicalRecord afterClone = medicalRecordRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName())
                .clone();

        verifyEditedMedicalRecord(beforeClone, afterClone, model);
    }

    public static void deleteMedicalRecordSuccess(MockMvc mvc, MedicalRecordRepository medicalRecordRepo, DeleteMedicalRecordModel model) throws Exception {
        int countBefore = medicalRecordRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                delete("/medicalrecord").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.method", is("DELETE")));

        int countAfter = medicalRecordRepo.count();
        assert countBefore - 1 == countAfter;
    }

    public static void deleteMedicalRecordFail(MockMvc mvc, MedicalRecordRepository medicalRecordRepo, DeleteMedicalRecordModel model) throws Exception {
        int countBefore = medicalRecordRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                delete("/medicalrecord").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.method", is("DELETE")));

        int countAfter = medicalRecordRepo.count();
        assert countBefore == countAfter;
    }


    public static void putMedicalRecordFail(MockMvc mvc, MedicalRecordRepository medicalRecordRepo, RestMedicalRecordModel model) throws Exception {
        int countBefore = medicalRecordRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                put("/medicalrecord").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.method", is("PUT")));

        int countAfter = medicalRecordRepo.count();

        assert countBefore == countAfter;
    }

    private static void verifyEditedMedicalRecord(MedicalRecord before, MedicalRecord after, RestMedicalRecordModel model) {
        // Check address field
        if (model.getBirthdate() == null) {
            assert before.getBirthdate().equals(after.getBirthdate());
        } else {
            assert after.getBirthdate().equals(model.getBirthdate());
        }

        // Check city field
        if (model.getMedications() == null) {
            assert before.getMedications().equals(after.getMedications());
        } else {
            assert after.getMedications().equals(model.getMedications());
        }

        // Check zip field
        if (model.getAllergies() == null) {
            assert before.getAllergies().equals(after.getAllergies());
        } else {
            assert after.getAllergies().equals(model.getAllergies());
        }

        assert before.getPerson().equals(after.getPerson());
        assert after.getPerson().getFirstName().equals(model.getFirstName());
        assert after.getPerson().getLastName().equals(model.getLastName());
    }

    private static void verifyNewMedicalRecord(MedicalRecordRepository medicalRecordRepo, RestMedicalRecordModel model) {
        MedicalRecord medicalRecord = medicalRecordRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName());
        assert medicalRecord.getBirthdate().equals(model.getBirthdate());
        assert medicalRecord.getMedications().equals(model.getMedications());
        assert medicalRecord.getAllergies().equals(model.getAllergies());
        assert medicalRecord.getPerson().getFirstName().equals(model.getFirstName());
        assert medicalRecord.getPerson().getLastName().equals(model.getLastName());
        assert medicalRecord.getPerson().getMedicalRecord().equals(medicalRecord);
    }

    public static void postMedicalRecordFail(MockMvc mvc, MedicalRecordRepository medicalRecordRepo, RestMedicalRecordModel model) throws Exception {
        int countBefore = medicalRecordRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                post("/medicalrecord").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.method", is("POST")));

        int countAfter = medicalRecordRepo.count();

        assert countBefore == countAfter;
    }
}
