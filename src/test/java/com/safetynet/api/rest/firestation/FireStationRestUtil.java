package com.safetynet.api.rest.firestation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.model.delete.DeleteFireStationModel;
import com.safetynet.api.model.rest.RestFireStationModel;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.PersonRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FireStationRestUtil {

    public static void postFireStationSuccess(MockMvc mvc, FireStationRepository fireStationRepo, RestFireStationModel model) throws Exception {
        int countBefore = fireStationRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                post("/firestation").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.method", is("POST")));

        int countAfter = fireStationRepo.count();

        assert countBefore + 1 == countAfter;
        verifyNewFireStation(fireStationRepo, model);
    }

    public static void putFireStationSuccess(MockMvc mvc, FireStationRepository fireStationRepo, RestFireStationModel model) throws Exception {
        int countBefore = fireStationRepo.count();
        FireStation beforeClone = fireStationRepo.findOneByStation(model.getStation())
                .clone();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                put("/firestation").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.method", is("PUT")));

        int countAfter = fireStationRepo.count();

        assert countBefore == countAfter;

        FireStation afterClone = fireStationRepo.findOneByStation(model.getStation())
                .clone();

        verifyEditedFireStation(beforeClone, afterClone, model);
    }

    public static void deleteFireStationSuccess(MockMvc mvc, PersonRepository personRepo, FireStationRepository fireStationRepo, DeleteFireStationModel model) throws Exception {
        int countBefore = fireStationRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                delete("/firestation").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.method", is("DELETE")));

        int countAfter = fireStationRepo.count();
        assert countBefore - 1 == countAfter;

        checkAfterDelete(personRepo, model);

    }

    public static void deleteFireStationFail(MockMvc mvc, FireStationRepository fireStationRepo, DeleteFireStationModel model) throws Exception {
        int countBefore = fireStationRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                delete("/firestation").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.method", is("DELETE")));

        int countAfter = fireStationRepo.count();
        assert countBefore == countAfter;
    }

    private static void checkAfterDelete(PersonRepository personRepo, DeleteFireStationModel model) {
        // For each persons (checks)
        for (Person person : personRepo.findAll()) {
            if(person.getFireStation() == null){
                // The field fireStation is required in Person
                assert false;
            } else if(person.getFireStation().getStation().equals(model.getStation())){
                // The fireStation has not been deleted
                assert false;
            }
        }
    }


    public static void putFireStationFail(MockMvc mvc, FireStationRepository fireStationRepo, RestFireStationModel model) throws Exception {
        int countBefore = fireStationRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                put("/firestation").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.method", is("PUT")));

        int countAfter = fireStationRepo.count();

        assert countBefore == countAfter;
    }

    private static void verifyEditedFireStation(FireStation before, FireStation after, RestFireStationModel model) {
        // Check address field
        if (model.getStation() == null) {
            assert before.getStation().equals(after.getStation());
        } else {
            assert after.getStation().equals(model.getStation());
        }

        // Check address field
        if (model.getAddress() == null) {
            assert before.getAddress().equals(after.getAddress());
        } else {
            assert after.getAddress().equals(model.getAddress());
        }

        // check persons set
        assert before.getPersons().equals(after.getPersons());
    }

    private static void verifyNewFireStation(FireStationRepository fireStationRepo, RestFireStationModel model) {
        FireStation fireStation = fireStationRepo.findOneByStation(model.getStation());
        assert fireStation.getAddress().equals(model.getAddress());
        assert fireStation.getStation().equals(model.getStation());
        assert fireStation.getPersons().isEmpty();
    }

    public static void postFireStationFail(MockMvc mvc, FireStationRepository fireStationRepo, RestFireStationModel model) throws Exception {
        int countBefore = fireStationRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                post("/firestation").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.method", is("POST")));

        int countAfter = fireStationRepo.count();

        assert countBefore == countAfter;
    }
}
