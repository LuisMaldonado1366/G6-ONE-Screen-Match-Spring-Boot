package com.aluracursos.screenmatch.main;

import java.util.Arrays;
import java.util.List;

public class StreamsExample {
    public void showExmaple() {
        List<String> names = Arrays.asList("Luis", "Ney", "Joss", "Ale", "Roger");

        names.stream()
                .sorted()
                .limit(4)
                .filter(userName -> userName.startsWith("N"))
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
}
