package com.aluracursos.screenmatch.main;

import com.aluracursos.screenmatch.models.Episode;
import com.aluracursos.screenmatch.models.EpisodeData;
import com.aluracursos.screenmatch.models.SeasonData;
import com.aluracursos.screenmatch.models.SeriesData;
import com.aluracursos.screenmatch.service.APIConsume;
import com.aluracursos.screenmatch.service.ParseData;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private  final APIConsume apiConsume = new APIConsume();
    private final Dotenv dotenv = Dotenv.load();
    private final String URL_BASE = "http://www.omdbapi.com/?apikey=" + dotenv.get("OMDB_TOKEN");
    private final ParseData parseData = new ParseData();


    public void displayMenu() throws IOException, InterruptedException {
        System.out.println("Enter the series name you want to search: ");
        String seriesName = scanner.nextLine();

        String urlSeries = URL_BASE + "&t=" +
                URLEncoder.encode(seriesName, StandardCharsets.UTF_8);
        String jsonSeries = apiConsume.getData(urlSeries);

        var seriesData = parseData.parseData(jsonSeries, SeriesData.class);
        System.out.println(seriesData);

        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i <= seriesData.totalSeasons(); i++) {
            String urlSeason = urlSeries + "&Season=" + i;
            String jsonSeason = apiConsume.getData(urlSeason);
            var seasonsData = parseData.parseData(jsonSeason, SeasonData.class);
            seasons.add(seasonsData);
        }

//        seasons.forEach(season -> season.episodes().forEach(episode -> System.out.println(episode.title())));

        List<EpisodeData> dataEpisodes = seasons.stream()
                .flatMap(season -> season.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("Episodes Top 5:");
        dataEpisodes.stream()
                .filter(episode -> !episode.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(season -> season.episodes().stream()
                        .map(episode -> new Episode(episode.episodeNumber(), episode)))
                .collect(Collectors.toList());

        episodes.forEach(System.out::println);
    }
}
