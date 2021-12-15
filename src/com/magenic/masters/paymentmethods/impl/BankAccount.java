package com.magenic.masters.paymentmethods.impl;

import com.magenic.masters.paymentmethods.PaymentMethod;

public sealed class BankAccount implements PaymentMethod permits CheckingAccount,SavingsAccount {

	protected String accountName;
	protected String nickname;
	protected String accountNumber;

	public BankAccount(String accountName, String accountNumber) {
		this.accountName = accountName;
		this.accountNumber = accountNumber;
		this.nickname = "NA";
	}

	public BankAccount(String accountName, String accountNumber, String nickname) {
		this.accountName = accountName;
		this.accountNumber = accountNumber;
		this.nickname = nickname;
	}

	@Override
	public String getAccountDetails() {
		// TODO Auto-generated method stub
		String data = """
				Account nickname :\s%s
				Account Name:\s%s
				Account Number:\s%s
				Bank name:\sBDO
				""";
		return data.formatted(this.nickname, this.accountName, this.accountNumber);
	}

}
