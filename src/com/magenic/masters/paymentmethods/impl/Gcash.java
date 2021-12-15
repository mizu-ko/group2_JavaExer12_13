package com.magenic.masters.paymentmethods.impl;

import com.magenic.masters.paymentmethods.PaymentMethod;

public record Gcash (String subscriberName, String mobileNumber, String nickname) implements PaymentMethod {

	public Gcash(String subscriberName, String mobileNumber) {
		this(subscriberName, mobileNumber, "NA");
		// TODO Auto-generated constructor stub
	};
	@Override
	public String getAccountDetails() {
		String gcashDtls = """
				Account nickname: %s
				Subscriber name:\s%s
				Mobile Number:\s%s
				Account Type: Gcash
				""";
		// TODO Auto-generated method stub
		return gcashDtls.formatted(nickname, subscriberName, mobileNumber);
	}

	
}
