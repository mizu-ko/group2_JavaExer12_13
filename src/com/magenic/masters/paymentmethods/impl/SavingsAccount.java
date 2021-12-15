package com.magenic.masters.paymentmethods.impl;

public final class SavingsAccount extends BankAccount {

	public SavingsAccount(String accountName, String accountNumber) {
		super(accountName, accountNumber);
		// TODO Auto-generated constructor stub
	}

	public SavingsAccount(String accountName, String accountNumber, String nickname) {
		super(accountName, accountNumber, nickname);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getAccountDetails() {
		// TODO Auto-generated method stub
		String data = """
				Account nickname :\s%s
				Account Name:\s%s
				Account Number:\s%s
				Account Type: Savings
				Bank name:\sBDO
				""";
		return data.formatted(this.nickname, this.accountName, this.accountNumber);
	}
}
