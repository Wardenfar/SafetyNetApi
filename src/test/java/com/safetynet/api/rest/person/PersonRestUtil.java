package com.safetynet.api.rest.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.safetynet.api.entity.Person;
import com.safetynet.api.model.delete.DeletePersonModel;
import com.safetynet.api.model.rest.RestPersonModel;
import com.safetynet.api.repository.PersonRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PersonRestUtil {

    public static void postPersonSuccess(MockMvc mvc, PersonRepository personRepo, RestPersonModel model) throws Exception {
        int countBefore = personRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.method", is("POST")));

        int countAfter = personRepo.count();

        assert countBefore + 1 == countAfter;
        verifyNewPerson(personRepo, model);
    }

    public static void putPersonSuccess(MockMvc mvc, PersonRepository personRepo, RestPersonModel model) throws Exception {
        int countBefore = personRepo.count();
        Person beforeClone = personRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName())
                .clone();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                put("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.method", is("PUT")));

        int countAfter = personRepo.count();

        assert countBefore == countAfter;

        Person afterClone = personRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName())
                .clone();

        verifyEditedPerson(beforeClone, afterClone, model);
    }

    public static void deletePersonSuccess(MockMvc mvc, PersonRepository personRepo, DeletePersonModel model) throws Exception {
        int countBefore = personRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                delete("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.method", is("DELETE")));

        int countAfter = personRepo.count();
        assert countBefore - 1 == countAfter;
    }

    public static void deletePersonFail(MockMvc mvc, PersonRepository personRepo, DeletePersonModel model) throws Exception {
        int countBefore = personRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                delete("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.method", is("DELETE")));

        int countAfter = personRepo.count();
        assert countBefore == countAfter;
    }


    public static void putPersonFail(MockMvc mvc, PersonRepository personRepo, RestPersonModel model) throws Exception {
        int countBefore = personRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                put("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.method", is("PUT")));

        int countAfter = personRepo.count();

        assert countBefore == countAfter;
    }

    private static void verifyEditedPerson(Person before, Person after, RestPersonModel model) {
        assert before.getFirstName().equals(after.getFirstName());
        assert before.getLastName().equals(after.getLastName());

        // Check address field
        if (model.getAddress() == null) {
            assert before.getAddress().equals(after.getAddress());
        } else {
            assert after.getAddress().equals(model.getAddress());
        }

        // Check city field
        if (model.getCity() == null) {
            assert before.getCity().equals(after.getCity());
        } else {
            assert after.getCity().equals(model.getCity());
        }

        // Check zip field
        if (model.getZip() == null) {
            assert before.getZip().equals(after.getZip());
        } else {
            assert after.getZip().equals(model.getZip());
        }

        // Check phone field
        if (model.getPhone() == null) {
            assert before.getPhone().equals(after.getPhone());
        } else {
            assert after.getPhone().equals(model.getPhone());
        }

        // Check email field
        if (model.getEmail() == null) {
            assert before.getEmail().equals(after.getEmail());
        } else {
            assert after.getEmail().equals(model.getEmail());
        }

        // Check fireStation field
        if (model.getFireStation() == null) {
            assert before.getFireStation().getStation().equals(after.getFireStation().getStation());
        } else {
            assert after.getFireStation().getStation().equals(model.getFireStation());
        }

        assert before.getMedicalRecord().equals(after.getMedicalRecord());
    }

    private static void verifyNewPerson(PersonRepository personRepo, RestPersonModel model) {
        Person person = personRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName());
        assert person.getAddress().equals(model.getAddress());
        assert person.getCity().equals(model.getCity());
        assert person.getZip().equals(model.getZip());
        assert person.getPhone().equals(model.getPhone());
        assert person.getEmail().equals(model.getEmail());
        assert person.getFireStation().getAddress().equals(model.getFireStation());
        assert person.getMedicalRecord() == null;
    }

    public static void postPersonFail(MockMvc mvc, PersonRepository personRepo, RestPersonModel model) throws Exception {
        int countBefore = personRepo.count();

        ObjectWriter writer = new ObjectMapper().writer();
        String contentJson = writer.writeValueAsString(model);

        mvc.perform(
                post("/person").contentType(MediaType.APPLICATION_JSON)
                        .content(contentJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.method", is("POST")));

        int countAfter = personRepo.count();

        assert countBefore == countAfter;
    }
}
