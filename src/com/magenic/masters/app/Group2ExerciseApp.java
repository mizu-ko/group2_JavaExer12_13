package com.magenic.masters.app;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.magenic.masters.object.Stock;

public class Group2ExerciseApp {

	public static void main(String[] args) throws IOException {
		Integer categoryInt = 1;
		String rawData = Files.readString(Path.of("input/stocks.csv"));
		List<Stock> stockItems = rawData.lines().filter(Predicate.not(String::isBlank)).map(a -> mapToObject(a))
				.collect(Collectors.toList());

		printCategory();
		printItemsInCategory(stockItems, getCategory(categoryInt));
	}

	private static Stock mapToObject(String str) {
		String[] split = str.split(",");
		Stock stock = new Stock();
		stock.setProductName(split[0]);
		stock.setPrice(BigDecimal.valueOf(Double.valueOf(split[1])));
		stock.setQuantityType(split[2]);
		stock.setCategory(split[3]);
		return stock;
	}

	private static void printItemsInCategory(List<Stock> stockItems, String category) {
		Predicate<Stock> filterByCategory = (s) -> s.getCategory().equals(category);
		Integer n = 0;
		stockItems.stream()
		.filter(filterByCategory)
		.collect(Collectors.toList());

	}

	private static void printCategory() {
		String categories = """
				Category:
				  1 - Pantry
				  2 - Meat
				  3 - Snacks
				""";
		System.out.println(categories);
	}

	private static String getCategory(Integer input) {
		var category = switch (input) {
		case 0 -> "Pantry";
		case 1 -> "Meat/Poultry/Seafood";
		case 2 -> "Snacks";
		default -> "N/A";
		};
		return category;
	}

}
