package com.kris.projects.banktransactions.util;

import java.io.File;
import java.nio.file.Paths;

public class BankTransactionUtility {

	public static final String READ = "read";
	public static final String WRITE = "write";
	public static final String JOBREPORT = "reports";
	private static final String PENDING = "\\Pending";
	private static final String PROCESSED = "\\Processed\\";
	private static final String REPORTS = "\\reports\\";
	private static final String INPUTFILEPATTERN = "finance_customer_transactions";
	public static final String CUSTOMER_ACCOUNT = "customerAccount";
	public static final String TRANSACTION_AMOUNT = "transactionamount";
	public static String INPUTFILENAME;
	public static int INVALIDRECORDS;
	public static String date_time;

	public static String getFileName(String action) {
		File directory = null;
		String fileName = null;
		String envPath = System.getenv("TRANSACTION_PROCESSING");
		if (action.equals(READ)) {
			directory = new File(envPath + PENDING);

			File[] files = directory.listFiles((dir, name) -> name.toLowerCase().startsWith(INPUTFILEPATTERN));
			if((files.length==0) || (files.length > 1)) {
				System.out.println("Either zero or Multiple files Present.. Processing only one file... ");
			}
			else {
				if (files[0].getAbsolutePath().contains(".csv")) {
					fileName = files[0].getAbsolutePath();
					INPUTFILENAME = files[0].getName();
							//Paths.get(fileName).getFileName().toString();
				}
			}
			
		}

		if (action.equals(WRITE)) {
			fileName = envPath + PROCESSED +INPUTFILENAME;
		}
		if (action.equals(JOBREPORT)) {
			fileName = envPath + REPORTS + INPUTFILENAME.replace(".csv", ".txt");
		}
		// throw new exception

		return fileName;
	}

}
