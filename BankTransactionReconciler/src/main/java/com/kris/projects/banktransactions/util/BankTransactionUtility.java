package com.kris.projects.banktransactions.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

/**
 * Utility class that contain file handling and other utilities
 * 
 * @author Krishna Angeras
 *
 */
@Component
@JobScope
public class BankTransactionUtility {

	private BankTransactionProperties properties;

	public static final String READ = "read";
	public static final String WRITE = "write";
	public static final String JOBREPORT = "reports";
	private static final String PENDING = "\\Pending";
	private static final String PROCESSED = "\\Processed\\";
	private static final String REPORTS = "\\reports\\";
	public static int INVALIDRECORDS = 0;
	private static String INPUTFILENAME;
	private static final Logger logger = LoggerFactory.getLogger(BankTransactionUtility.class);

	public BankTransactionUtility() {
	}

	public BankTransactionUtility(BankTransactionProperties properties) {
		this.properties = properties;
	}

	/**
	 * Method to get the fileName based on action - read, report and write
	 * 
	 * @param action
	 *            read, write, report actions
	 * @return Filename based on action parameter
	 */

	public String getFileName(String action) {
		File directory = null;
		String fileName = "";
		String envPath = System.getenv(properties.getEnvpath());

		if (envPath == null) {
			logger.debug("Directory created -> " + directory);
		}
		else {
		if (action.equals(READ)) {
			directory = new File(envPath + PENDING);
			if (!directory.exists()) {
				directory.mkdir();
				logger.debug("Directory created -> " + directory);
			}

			File[] files = directory
					.listFiles((dir, name) -> name.toLowerCase().startsWith(properties.getInputfileprefix()));

			for (File file : files) {
				if (file.getAbsolutePath().contains(".csv")) {
					fileName = files[0].getAbsolutePath();
					INPUTFILENAME = files[0].getName();
					break;
				}
			}
			logger.debug("Input csv Filename -> " + fileName);
		}

		if (action.equals(WRITE)) {
			directory = new File(envPath + PROCESSED);
			if (!directory.exists()) {
				directory.mkdir();
				logger.info("Directory created -> " + directory);
			}

			fileName = envPath + PROCESSED + INPUTFILENAME;
			if (new File(fileName).exists()) {
				String backup = fileName + "_backup";
				new File(fileName).renameTo(new File(backup));
				logger.info("File already exists in Processed Directory. Renamed existing file to " + backup);
			}
		}
		if (action.equals(JOBREPORT)) {
			directory = new File(envPath + REPORTS);
			if (!directory.exists()) {
				directory.mkdir();
				logger.info("Directory created -> " + directory);
			}
			fileName = envPath + REPORTS + INPUTFILENAME.replace(".csv", ".txt");
		}
		}
		return fileName;
	}

}
