package com.kris.projects.banktransactions.dto;

public class BankTransactionDTO {

	double customerAccount;
	double transactionamount;

	transient double debit;
	transient double credit;
	transient double count;

	public BankTransactionDTO() {

	}

	public BankTransactionDTO(double account, double amount) {
		this.customerAccount = account;
		this.transactionamount = amount;
	}

	public BankTransactionDTO(double count, double debit, double credit) {
		this.debit = debit;
		this.credit = credit;
		this.count = count;
	}

	public double getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(double customerAccount) {
		this.customerAccount = customerAccount;
	}

	public double getTransactionamount() {
		return transactionamount;
	}

	public void setTransactionamount(double transactionamount) {
		this.transactionamount = transactionamount;
	}

	public double getDebit() {
		return debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

}
