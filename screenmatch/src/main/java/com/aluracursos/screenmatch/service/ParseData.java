package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.models.SeriesData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseData implements IParseData{
    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public <T> T parseData(String json, Class<T> dataClass) {
        try {
            return objectMapper.readValue(json, dataClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
