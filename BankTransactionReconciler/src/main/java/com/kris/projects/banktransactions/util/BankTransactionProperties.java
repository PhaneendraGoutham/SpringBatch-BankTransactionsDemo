package com.kris.projects.banktransactions.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This Class will wire the properties using ConfigurationProperties
 * 
 * @author Krishna Angeras
 *
 */
@Component
@ConfigurationProperties
public class BankTransactionProperties {

	private String envpath;
	private String insertqry;
	private String deleteqry;
	private String selectqry;
	private String inputfileprefix;
	private String customeraccount;
	private String transactionamount;

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
