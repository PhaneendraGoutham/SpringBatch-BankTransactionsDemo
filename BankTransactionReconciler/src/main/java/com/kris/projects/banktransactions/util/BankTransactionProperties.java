package com.kris.projects.banktransactions.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class BankTransactionProperties {

	String envpath;
	String insertqry;
	String deleteqry;
	String selectqry;
	String inputfileprefix;
	String customeraccount;
	String transactionamount;

	public String getEnvpath() {
		return envpath;
	}

	public void setEnvpath(String envpath) {
		this.envpath = envpath;
	}

	public String getInsertqry() {
		return insertqry;
	}

	public void setInsertqry(String insertqry) {
		this.insertqry = insertqry;
	}

	public String getDeleteqry() {
		return deleteqry;
	}

	public void setDeleteqry(String deleteqry) {
		this.deleteqry = deleteqry;
	}

	public String getSelectqry() {
		return selectqry;
	}

	public void setSelectqry(String selectqry) {
		this.selectqry = selectqry;
	}

	public String getInputfileprefix() {
		return inputfileprefix;
	}

	public void setInputfileprefix(String inputfileprefix) {
		this.inputfileprefix = inputfileprefix;
	}

	public String getCustomeraccount() {
		return customeraccount;
	}

	public void setCustomeraccount(String customeraccount) {
		this.customeraccount = customeraccount;
	}

	public String getTransactionamount() {
		return transactionamount;
	}

	public void setTransactionamount(String transactionamount) {
		this.transactionamount = transactionamount;
	}
}
