package com.magenic.masters.object;

public class CartItem {
	private Stock stockItem;
	private Double quantity; // if kg, 1. if others, use user input.
	private Double inputQuantity; // input by user.
	private Double totalPrice;

	public Double getInputQuantity() {
		return inputQuantity;
	}

	public void setInputQuantity(Double inputQuantity) {
		this.inputQuantity = inputQuantity;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Stock getStockItem() {
		return stockItem;
	}

	public void setStockItem(Stock stockItem) {
		this.stockItem = stockItem;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

}
