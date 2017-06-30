package com.kris.projects.banktransactions.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.kris.projects.banktransactions.dto.BankTransactionDTO;

public class BankTransactionProcessor implements ItemProcessor<BankTransactionDTO, BankTransactionDTO> {

	private static final Logger logger = LoggerFactory.getLogger(BankTransactionProcessor.class);

	@Override
	public BankTransactionDTO process(final BankTransactionDTO transactions) {
		return transactions;
	}

}
