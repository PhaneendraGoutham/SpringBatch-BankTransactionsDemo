package com.kris.projects.banktransactions.util;

import java.sql.BatchUpdateException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

/**
 * This Class implements SkipPolicy - skips the lines that contains certain
 * exceptions and count the skipped Lines
 * 
 * @author Krishna Angeras
 *
 */
@Component
public class InvalidBankTransactions implements SkipPolicy {

	private static final Logger logger = LoggerFactory.getLogger(InvalidBankTransactions.class);

	/**
	 * Overridden method - to skip certain exceptions and count the skipped
	 * lines
	 * 
	 * @see org.springframework.batch.core.step.skip.SkipPolicy#shouldSkip(java.lang.Throwable,
	 *      int)
	 */
	@Override
	public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
		boolean returnValue = false;
		if ((exception instanceof FlatFileParseException) || (exception instanceof IncorrectTokenCountException)
				|| (exception instanceof BindException) || (exception instanceof ParseException)
				|| (exception instanceof BatchUpdateException)
				|| (exception instanceof DataIntegrityViolationException)) {
			BankTransactionUtility.INVALIDRECORDS++;
			returnValue = true;
		} else {
			logger.error("Error in Processing the file --> " + exception.toString());
			returnValue = false;
		}

		return returnValue;
	}

}