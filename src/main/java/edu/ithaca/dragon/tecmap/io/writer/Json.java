package edu.ithaca.dragon.tecmap.io.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Json {

    public static String toJsonString(Object objectToSerialize) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString( objectToSerialize);
    }
}
