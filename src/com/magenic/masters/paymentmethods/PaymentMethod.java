package com.magenic.masters.paymentmethods;

import com.magenic.masters.paymentmethods.impl.BankAccount;
import com.magenic.masters.paymentmethods.impl.CreditCard;
import com.magenic.masters.paymentmethods.impl.Gcash;

public sealed interface PaymentMethod permits BankAccount, CreditCard, Gcash {

	public String getAccountDetails();
}
