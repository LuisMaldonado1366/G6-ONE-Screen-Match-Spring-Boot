package com.aluracursos.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeriesData(
    @JsonAlias("Title") String title,
    Integer totalSeasons,
    @JsonAlias("imdbRating") String rating) {

}
