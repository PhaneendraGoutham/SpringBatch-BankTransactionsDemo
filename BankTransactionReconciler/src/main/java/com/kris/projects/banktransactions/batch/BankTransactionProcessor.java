package com.kris.projects.banktransactions.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.kris.projects.banktransactions.dto.BankTransactionDTO;

/**
 * THIS CLASS IS NOT USED !! Have kept this for demo purpose
 * Custom Processor class to Pass on the BankTransactionDTO from ItemReader to
 * ItemWriter
 * 
 * @author Krishna Angeras
 *
 */
public class BankTransactionProcessor implements ItemProcessor<BankTransactionDTO, BankTransactionDTO> {

	private static final Logger logger = LoggerFactory.getLogger(BankTransactionProcessor.class);

	/**
	 * Overridden Method - to return the DTO
	 * 
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 *      Pass on the BankTransactionDTO from ItemReader to ItemWriter
	 */
	@Override
	public BankTransactionDTO process(final BankTransactionDTO transactions) {
		return transactions;
	}

}
