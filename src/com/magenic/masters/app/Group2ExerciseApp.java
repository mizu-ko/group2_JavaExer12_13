package com.magenic.masters.app;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.magenic.masters.object.Stock;

public class Group2ExerciseApp {
	private static List<Stock> cart = new ArrayList<>();
	private static List<Stock> stock = new ArrayList<>();

	public static void main(String[] args) throws IOException {

		Scanner scanner = new Scanner(System.in);
		String rawData = Files.readString(Path.of("input/stocks.csv"));
		stock = rawData.lines().filter(Predicate.not(String::isBlank)).map(a -> mapToObject(a))
				.collect(Collectors.toList());

//
//		Integer categoryInt = scanner.nextInt();
		categoryMenu(scanner);
	}

	private static void categoryMenu(Scanner scanner) {
		printCategory();
		String category = waitForCategoryInput(scanner);
		System.out.println(category);
		if (category.equals("EXIT")) {
			return;
		} else if (category.equals("CHECKOUT")) {
			checkoutMenu();
		} else {
			categorizedItemsMenu(category, scanner);
		}
	}

	private static String waitForCategoryInput(Scanner scanner) {
		Integer categInt = scanner.nextInt();
		System.out.println("Selected is " + categInt);
		if (categInt.equals(-1)) {
			// go to checkout
			return "CHECKOUT";
		} else if (categInt.equals(-2)) {
			return "EXIT";
		} else {
			String category = getCategory(categInt);
			if (category.equals("N/A")) {
				System.out.print("Please input valid category:");
				return waitForCategoryInput(scanner);
			} else {
				return getCategory(categInt);
			}
		}
	}

	private static void waitForStockInput(List<Stock> categorizedItems, Scanner scanner) {
		Integer n = 0;
		List<String> stockDisplays = new ArrayList<>();
		for (Stock item : categorizedItems) {
			item.setIndex(n);
			n++;

			String stockDisplay = """
					[%d] %s        price: %.2f / %s""";
			String formatted = stockDisplay.formatted(item.getIndex(), item.getProductName(), item.getPrice(),
					item.getQuantityType());
			stockDisplays.add(formatted);
		}
		stockDisplays.forEach(System.out::println);
		System.out.print("Choose item (-1 to go back to Categories):");
		Integer stock = scanner.nextInt();
		if (stock.equals(-1)) {
			System.out.print("Navigating to categories..");
			categoryMenu(scanner);

		} else if (stock > categorizedItems.size() - 1) {
			System.out.println("==========\nPlease enter valid item number\n\n");
			waitForStockInput(categorizedItems, scanner);
		} else {
			Stock selected = categorizedItems.get(stock);
			System.out.print("Please enter quantity:");
			Double quantity = scanner.nextDouble();
			BigDecimal totalPrice = BigDecimal.valueOf(quantity).multiply(selected.getPrice());
			String itemAddedDisplay = """
					Item Added : %s | %.2f / %s x %.1f | %.2f
					""";

			System.out.print(itemAddedDisplay.formatted(selected.getProductName(), selected.getPrice(),
					selected.getQuantityType(), quantity, totalPrice));
			cart.add(selected);
			printCurrentItems();
			waitForStockInput(categorizedItems, scanner);
		}
	}

	private static void printCurrentItems() {
		// TODO: dummy print, need teeing collector here..
		String currentItems = """
				Current cart contents:
				Total amount : %.2f
				Total amount compact : %s
				Number of items : %s
				""";
		System.out.println(currentItems.formatted(11000.0, "11.000 K", "12"));
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

	private static void categorizedItemsMenu(String category, Scanner scanner) {
		Predicate<Stock> filterByCategory = (s) -> s.getCategory().equals(category);
		List<Stock> filtered = stock.stream().filter(filterByCategory).collect(Collectors.toList());
		waitForStockInput(filtered, scanner);

	}

	private static void printCategory() {
		String categories = """
				Category:
				  1 - Pantry
				  2 - Meat
				  3 - Snacks

				Choose Category(-1 to checkout, -2 to Exit):""";
		System.out.print(categories);
	}

	private static String getCategory(Integer input) {
		var category = switch (input) {
		case 1 -> "Pantry";
		case 2 -> "Meat/Poultry/Seafood";
		case 3 -> "Snacks";
		default -> "N/A";
		};
		return category;
	}
	
	private static void checkoutMenu() {
		System.out.println("Payment Method.");
		// TODO : payment method, details, write to file.

	}

}
