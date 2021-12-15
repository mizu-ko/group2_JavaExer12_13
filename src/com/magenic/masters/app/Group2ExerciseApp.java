package com.magenic.masters.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.magenic.masters.object.CartItem;
import com.magenic.masters.object.Stock;
import com.magenic.masters.paymentmethods.PaymentMethod;
import com.magenic.masters.paymentmethods.impl.CheckingAccount;
import com.magenic.masters.paymentmethods.impl.CreditCard;
import com.magenic.masters.paymentmethods.impl.Gcash;
import com.magenic.masters.paymentmethods.impl.SavingsAccount;

public class Group2ExerciseApp {
	private static List<Stock> stock = new ArrayList<>();
	private static List<CartItem> cartItems = new ArrayList<>();

	private static List<PaymentMethod> paymentMethods = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		loadPaymentMethods();
		printPaymentMethod();

		Scanner scanner = new Scanner(System.in);
		waitForPaymentMethodInput(scanner);

//		String rawData = Files.readString(Path.of("input/stocks.csv"));
//		stock = rawData.lines().filter(Predicate.not(String::isBlank)).map(a -> mapToObject(a))
//				.collect(Collectors.toList());
//
//		categoryMenu(scanner);
//		scanner.close();
	}

	private void paymentMethodMenu(Scanner scanner) {

	}

	private static void loadPaymentMethods() {
		PaymentMethod savings = new SavingsAccount("Mau Tuazon", "005412345678", "mau savings");
		PaymentMethod checking = new CheckingAccount("Mau Tuazon", "005412345678", "mau checking");
		PaymentMethod cc = new CreditCard("Mau Tuazon", "4344548954", "02/2023", "mau credit card");
		PaymentMethod gcash = new Gcash("John Doe", "09127233232", "mau gcash");
		paymentMethods.addAll(List.of(savings, checking, cc, gcash));

	}

	private static void printPaymentMethods() {
		for (PaymentMethod method : paymentMethods) {
			System.out.print("[" + paymentMethods.indexOf(method) + "]");
			System.out.println(method.getAccountDetails());
		}
	}

	private static void categoryMenu(Scanner scanner) {
		printCategory();
		String category = waitForCategoryInput(scanner);
		if (category.equals("EXIT")) {
			System.out.println("Thank you.");
			return;// to stop recursion
		} else if (category.equals("CHECKOUT")) {
			if (cartItems.isEmpty()) {
				System.out.println("Cart is empty, nothing to checkout.\n");
				categoryMenu(scanner);
			} else {
				checkoutMenu(scanner);
			}
		} else {
			System.out.println(category + " Items");
			categorizedItemsMenu(category, scanner);
		}
	}

	private static String waitForCategoryInput(Scanner scanner) {
		Integer categInt = scanner.nextInt();
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

	private static void waitForStockInput(List<Stock> categorizedItems, Scanner scanner, boolean firstTraversal) {
		Integer n = 0;
		List<String> stockDisplays = new ArrayList<>();
		if (firstTraversal) {
			for (Stock item : categorizedItems) {
				item.setIndex(n);
				n++;

				String stockDisplay = """
						[%d] %-40s price: %.2f / %s\n""";
				String formatted = stockDisplay.formatted(item.getIndex(), item.getProductName(), item.getPrice(),
						item.getQuantityType());
				stockDisplays.add(formatted);
			}
		}
		stockDisplays.forEach(System.out::printf);
		System.out.print("\nChoose item (-1 to go back to Categories):");
		Integer stock = scanner.nextInt();
		if (stock.equals(-1)) {
			System.out.println("Navigating to categories..");
			categoryMenu(scanner);

		} else if (stock > categorizedItems.size() - 1) {
			System.out.println("==========\nPlease enter valid item number\n\n");
			waitForStockInput(categorizedItems, scanner, false);
		} else {
			Stock selected = categorizedItems.get(stock);
			switch (selected.getQuantityType()) {
			case "kg" -> System.out.print("Enter how much (in kg):");
			default -> System.out.print("Enter how many:");
			}
			Double quantity = 0.0;// scanner.nextDouble();
			try {
				quantity = scanner.nextDouble();
			} catch (Exception e) {
				System.out.println("Please input valid number");
				quantity = scanner.nextDouble();
				waitForPaymentMethodInput(scanner);
			}
			Double totalPrice = selected.getPrice() * quantity;
			String itemAddedDisplay = """
					Item Added : %s | %.2f / %s x %.2f | %.2f
					""";

			System.out.print("\n" + itemAddedDisplay.formatted(selected.getProductName(), selected.getPrice(),
					selected.getQuantityType(), quantity, totalPrice));
			adduToCartu(selected, quantity);// add items to cart here..
			printCurrentItems(false);
			waitForStockInput(categorizedItems, scanner, false);
		}
	}

	private static void adduToCartu(Stock item, Double quantity) {
		var itemQty = switch (item.getQuantityType()) {
		case "kg" -> 1;
		default -> quantity;
		};
		CartItem cartItem = new CartItem();
		var totalPrice = quantity * item.getPrice();
		cartItem.setStockItem(item);
		cartItem.setQuantity(itemQty);
		cartItem.setInputQuantity(quantity);
		cartItem.setTotalPrice(totalPrice);
		cartItems.add(cartItem);
	}

	private static void printCurrentItems(boolean isCheckout) {
		if (!isCheckout) {
			System.out.println(parseTotalCartItems());
		}
		for (CartItem cartItem : cartItems) {
			Stock selected = cartItem.getStockItem();

			String itemAddedDisplay = """
					%s | %.2f / %s x %.1f | %.2f
					""";

			System.out.print(itemAddedDisplay.formatted(selected.getProductName(), selected.getPrice(),
					selected.getQuantityType(), cartItem.getInputQuantity(), cartItem.getTotalPrice()));
		}
		System.out.println();

	}

	private static String parseTotalCartItems() {
		BiFunction<Double, Double, String> formatToString = (a, b) -> {
			String message;
			message = """
					Total amount: %.2f
					Total amount compact: %s
					Number of Items: %.0f
					""";
			NumberFormat fmt = NumberFormat.getCompactNumberInstance();
			fmt.setMinimumFractionDigits(3);
			return message.formatted(a, fmt.format(a), b);
		};
		String totalCartItems = cartItems.stream()
				.collect(Collectors.teeing(Collectors.summingDouble(CartItem::getTotalPrice),
						Collectors.summingDouble(CartItem::getQuantity), (a, b) -> formatToString.apply(a, b)));

		return totalCartItems;
	}

	private static Stock mapToObject(String str) {
		String[] split = str.split(",");
		Stock stock = new Stock();
		stock.setProductName(split[0]);
		stock.setPrice(Double.valueOf(split[1]));
		stock.setQuantityType(split[2]);
		stock.setCategory(split[3]);
		return stock;
	}

	private static void categorizedItemsMenu(String category, Scanner scanner) {
		Predicate<Stock> filterByCategory = (s) -> s.getCategory().equals(category);
		List<Stock> filtered = stock.stream().filter(filterByCategory).collect(Collectors.toList());
		waitForStockInput(filtered, scanner, true);

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

	private static void checkoutMenu(Scanner scanner) {
		System.out.println("Current cart contents: ");
		printCurrentItems(true);
		printPaymentMethod();
		waitForPaymentMethodInput(scanner);

		// TODO : payment method, details, write to file.
	}

	private static void printPaymentMethod() {
		System.out.println("Payment Method:");
		for (PaymentMethod method : paymentMethods) {
			System.out.print("[" + paymentMethods.indexOf(method) + "]");
			System.out.println(method.getAccountDetails() + "\n");
		}
		System.out.println("[4] COD\n");
		System.out.println("[5] Pay with other account");
		System.out.print("\nPlease choose payment method:");
	}

	private static void printCod() {
		
	}
	private static void waitForPaymentMethodInput(Scanner scanner) {
		Integer paymentInt = -1;
		try {
			 paymentInt = scanner.nextInt();
		} catch (Exception e) {
			System.out.println("Please input valid number");
			waitForPaymentMethodInput(scanner);
		}
		PaymentMethod method = null;
		if(paymentInt < paymentMethods.size() && paymentInt >= 0) {
			method = paymentMethods.get(paymentInt);
		} else if (paymentInt == 4) {
			// TODO: custom and cod
		} else {
			
			System.out.println("Please input valid number");
			waitForPaymentMethodInput(scanner);
		}
		String header = switch(method) {
			case SavingsAccount sa -> sa.getAccountDetails();
			case CheckingAccount ca -> ca.getAccountDetails();
			case CreditCard cc -> cc.getAccountDetails();
			case Gcash gc -> gc.getAccountDetails();
			case null -> "N/A";
			default -> "N/A";
		};
		
//		String paymentType = switch (paymentInt) {
//		case 0 -> ""
//		case 3 -> "cc";
//		case 4 -> "gcash";
//		default -> "N/A";
//		};
		if (header.equals("N/A")) {
			waitForPaymentMethodInput(scanner);
		} else {
			String receiptHeader = header;
			String append = parseTotalCartItems();
			receiptHeader += "\n"+append;
			try {
				// TODO : refactor to different type of receipt.?
				Files.writeString(Path.of("output/receipt.txt"), receiptHeader, StandardOpenOption.CREATE);
				System.out.println("Thank you for your payment.");
				System.out.println(receiptHeader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
