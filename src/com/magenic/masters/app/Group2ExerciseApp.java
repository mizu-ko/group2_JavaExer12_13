package com.magenic.masters.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Group2ExerciseApp {

	public static void main(String[] args) throws IOException {
		String rawData = Files.readString(Path.of("input/stocks.csv"));
		System.out.println(rawData);
		List<Stock> stockItems = rawData.lines().map(a -> {
			a.split(",");
		});
	}

}
