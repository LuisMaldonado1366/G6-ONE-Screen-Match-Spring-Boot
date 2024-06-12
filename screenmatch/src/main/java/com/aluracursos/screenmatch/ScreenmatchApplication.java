package com.aluracursos.screenmatch;

import com.aluracursos.screenmatch.models.SeriesData;
import com.aluracursos.screenmatch.service.APIConsume;
import com.aluracursos.screenmatch.service.ParseData;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	Dotenv dotenv = Dotenv.load();
	String token = dotenv.get("OMDB_TOKEN");

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		APIConsume apiConsume = new APIConsume();
		String url = "http://www.omdbapi.com/?apikey=" + token + "&t=" +
				URLEncoder.encode("White collar", StandardCharsets.UTF_8);
		String json = apiConsume.getData(url);

		System.out.println(json);
		ParseData parseData = new ParseData();

		var data = parseData.parseData(json, SeriesData.class);
		System.out.println(data);
	}
}
