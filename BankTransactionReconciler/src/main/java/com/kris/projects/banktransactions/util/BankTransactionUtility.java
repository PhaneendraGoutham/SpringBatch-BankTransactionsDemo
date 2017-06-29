package com.kris.projects.banktransactions.util;

import java.io.File;

public class BankTransactionUtility {

	public static final String READ = "read";
	public static final String WRITE = "write";
	public static final String JOBREPORT = "reports";
	private static final String PENDING = "\\Pending";
	private static final String PROCESSED = "\\Processed";
	private static final String REPORTS = "\\reports\\finance_customer_transactions_report";
	private static final String INPUTFILEPATTERN = "finance_customer_transactions";
	public static final String CUSTOMER_ACCOUNT = "customerAccount";
	public static final String TRANSACTION_AMOUNT = "transactionamount";
	public static String INPUTFILENAME;
	public static int INVALIDRECORDS;

	public static String getFileName(String action) {
		File directory = null;
		String fileName = null;
		String envPath = System.getenv("TRANSACTION_PROCESSING");
		if (action.equals(READ)) {
			directory = new File(envPath + PENDING);

			File[] files = directory.listFiles((dir, name) -> name.toLowerCase().startsWith(INPUTFILEPATTERN));
			if (files.length > 1)
				System.out.println("Multiple files found in the file.. Processing only one file... ");
			// log - multiple files found in input path..
			fileName = files[0].getAbsolutePath().contains(".csv") ? files[0].getAbsolutePath() : null;
			INPUTFILENAME = fileName;
		}

		if (action.equals(WRITE)) {
			fileName = envPath + PROCESSED;
		}
		if (action.equals(JOBREPORT)) {
			String reportFileNameSuffix = INPUTFILENAME
					.substring(INPUTFILENAME.lastIndexOf("-"), INPUTFILENAME.indexOf(".csv")).concat(".txt");
			fileName = envPath + REPORTS + reportFileNameSuffix;
		}
		// throw new exception

		return fileName;
	}

}
