package com.safetynet.api.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static List<String> readStringList(JsonNode jsonNode) {
        List<String> values = new ArrayList<>();
        for (JsonNode node : jsonNode) {
            values.add(node.asText());
        }
        return values;
    }
}
