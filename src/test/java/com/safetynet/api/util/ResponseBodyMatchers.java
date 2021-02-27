package com.safetynet.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.safetynet.api.entity.Person;
import junit.framework.Assert;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Collection;
import java.util.List;

public class ResponseBodyMatchers {
    private ObjectMapper objectMapper = new ObjectMapper();

    public <T> ResultMatcher isListEqualInJson(
            List<Person> expectedObject, Class type) {

        ObjectMapper mapper = new ObjectMapper();
        TypeBase expectedType = mapper.getTypeFactory().constructCollectionType(List.class, type);

        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            T actualObject = objectMapper.readValue(json, expectedType);
            Collection first = expectedObject;
            Collection second = (Collection) actualObject;
            Assert.assertTrue(first.size() == second.size() && first.containsAll(second) && second.containsAll(first));
        };
    }

    static ResponseBodyMatchers responseBody() {
        return new ResponseBodyMatchers();
    }
}