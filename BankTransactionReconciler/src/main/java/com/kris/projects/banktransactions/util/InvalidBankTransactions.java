package com.kris.projects.banktransactions.util;

import java.io.FileNotFoundException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.validation.BindException;

public class InvalidBankTransactions implements SkipPolicy {

	private static final Logger logger = LoggerFactory.getLogger(InvalidBankTransactions.class);

	@Override
	public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
		if (exception instanceof FileNotFoundException) {
			return false;
		} else if ((exception instanceof FlatFileParseException) || (exception instanceof IncorrectTokenCountException)
				|| (exception instanceof BindException) || (exception instanceof ParseException)) {
			BankTransactionUtility.INVALIDRECORDS++;
			return true;
		} else {
			return false;
		}
	}

}