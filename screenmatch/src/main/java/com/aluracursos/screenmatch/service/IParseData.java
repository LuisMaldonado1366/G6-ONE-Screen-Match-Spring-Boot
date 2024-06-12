package com.aluracursos.screenmatch.service;

public interface IParseData {

    <T> T parseData(String json, Class <T> dataClass);
}
