package com.kris.projects.banktransactions.dto;

import java.math.BigDecimal;

import org.springframework.batch.core.configuration.annotation.StepScope;

/**
 * POJO class for input processing and reporting
 * 
 * @author Krishna Angeras
 *
 */
@StepScope
public class BankTransactionDTO {

	double customerAccount;
	double transactionamount;

	//
	transient BigDecimal debit;
	transient BigDecimal credit;
	transient BigDecimal count;

	public BankTransactionDTO() {
	}

	public BankTransactionDTO(double account, double amount) {
		this.customerAccount = account;
		this.transactionamount = amount;
	}

	public BankTransactionDTO(BigDecimal count, BigDecimal debit, BigDecimal credit) {
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

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

}
