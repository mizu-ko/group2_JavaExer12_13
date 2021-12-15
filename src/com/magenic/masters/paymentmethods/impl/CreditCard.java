package com.magenic.masters.paymentmethods.impl;

import com.magenic.masters.paymentmethods.PaymentMethod;

public final class CreditCard implements PaymentMethod {
	private String nameOnCard;
	private String ccNumber;
	private String expiryDate;
	private String nickname;

	public CreditCard(String nameOnCard, String ccNumber, String expiryDate) {
		this.nameOnCard = nameOnCard;
		this.ccNumber = ccNumber;
		this.expiryDate = expiryDate;
		this.nickname = "NA";
	}
	public CreditCard(String nameOnCard, String ccNumber, String expiryDate, String nickname) {
		this.nameOnCard = nameOnCard;
		this.ccNumber = ccNumber;
		this.expiryDate = expiryDate;
		this.nickname = nickname;
	}

	@Override
	public String getAccountDetails() {
		// TODO Auto-generated method stub
		String ccDtls = """
				Account nickname :\s%s
				Name on Card:\s%s
				Credit Card number:\s%s
				Account Type: Credit Card
				Expiry Date:\s%s
				""";
		return ccDtls.formatted(this.nickname, this.nameOnCard, this.ccNumber, this.expiryDate);
	}

}
