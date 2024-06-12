package com.aluracursos.screenmatch;

import com.aluracursos.screenmatch.main.Main;
import com.aluracursos.screenmatch.main.StreamsExample;
import com.aluracursos.screenmatch.models.EpisodeData;
import com.aluracursos.screenmatch.models.SeasonData;
import com.aluracursos.screenmatch.models.SeriesData;
import com.aluracursos.screenmatch.service.APIConsume;
import com.aluracursos.screenmatch.service.ParseData;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	Dotenv dotenv = Dotenv.load();
	String token = dotenv.get("OMDB_TOKEN");

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		Main main = new Main();
//		main.displayMenu();

		StreamsExample streamsExample = new StreamsExample();

		streamsExample.showExmaple();
	}
}
