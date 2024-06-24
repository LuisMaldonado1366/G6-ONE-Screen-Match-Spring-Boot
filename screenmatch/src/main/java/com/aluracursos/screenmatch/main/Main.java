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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
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

//        System.out.println("Top 5 Episodes:");
//        dataEpisodes.stream()
//                .filter(episode -> !episode.rating().equalsIgnoreCase("N/A"))
//                .peek(episode -> System.out.println("First: Filter 'N/A' ratings" + episode))
//                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
//                .peek(episode -> System.out.println("Second: Sorting ratings." + episode))
//                .map(episode -> episode.title().toUpperCase())
//                .peek(episode -> System.out.println("Third: Title to uppercase" + episode))
//                .limit(5)
//                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(season -> season.episodes().stream()
                        .map(episode -> new Episode(episode.episodeNumber(), episode)))
                .collect(Collectors.toList());

//        episodes.forEach(System.out::println);

//        System.out.println("Please, enter the year thereupon to look for episodes:");
//        int inputYear = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate localDate = LocalDate.of(inputYear, 1,1);

//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodes.stream()
//                .filter(episode -> episode.getReleaseDate() != null &&
//                        episode.getReleaseDate().isAfter(localDate))
//                .forEach(episode -> System.out.println(
//                        "Season: " + episode.getSeason() +
//                                "\tEpisode: " + episode.getEpisodeNumber() +
//                                "\tTitle: " + episode.getTitle() +
//                                "\tRelease date: " + episode.getReleaseDate().format(dateTimeFormatter)
//                ));

//        System.out.println("Enter a keyword or the complete title to look for: ");
//        String partialTitle = scanner.nextLine();
//
//        Optional<Episode> lookupEpisode = episodes.stream()
//                .filter(episode -> episode.getTitle().toUpperCase().contains(partialTitle.toUpperCase()))
//                .findFirst();
//
//        if (lookupEpisode.isPresent()) {
//            System.out.println("Found match: ");
//            System.out.println("\t The data is: " + lookupEpisode.get());
//        } else {
//            System.out.println("Episode not found!");
//        }

        Map<Integer, Double> evaluationsPerSeason = episodes.stream()
                .filter(episode -> episode.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason, Collectors.averagingDouble(Episode::getRating)));

        System.out.println(evaluationsPerSeason);

        DoubleSummaryStatistics statistics = episodes.stream()
                .filter(episode -> episode.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        System.out.println("Mean rating: " + statistics.getAverage());
        System.out.println("Highest rating: " + statistics.getMax());
        System.out.println("Lowest rating: " + statistics.getMin());

    }
}
