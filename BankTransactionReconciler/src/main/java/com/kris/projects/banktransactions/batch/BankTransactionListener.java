package com.kris.projects.banktransactions.batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.kris.projects.banktransactions.dto.BankTransactionDTO;
import com.kris.projects.banktransactions.util.BankTransactionProperties;
import com.kris.projects.banktransactions.util.BankTransactionUtility;

/**
 * A custom listener class that extends JobExecutionListenerSupport. Overrides
 * beforeJob and afterJob to perform pre validation and post tasks (like file
 * move and reporting)
 * 
 * @author Krishna Angeras
 *
 */
@Component

public class BankTransactionListener extends JobExecutionListenerSupport {

	private static final Logger logger = LoggerFactory.getLogger(BankTransactionListener.class);

	private final JdbcTemplate jdbcTemplate;
	private final BankTransactionProperties properties;

	/**
	 * Autowiring JdbcTemplate, Properties to the Constructor
	 * 
	 * @param jdbcTemplate
	 *            Autowire Spring JdbcTemplate class
	 * @param properties
	 *            Autowire BankTransactionProperties
	 */
	@Autowired

	public BankTransactionListener(BankTransactionProperties properties, JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.properties = properties;
	}

	/**
	 * Overridden method - Validating if the input Path exists or not and
	 * terminate the job execution if the file does not exist
	 * 
	 * @see org.springframework.batch.core.listener.JobExecutionListenerSupport#beforeJob(org.springframework.batch.core.JobExecution)
	 * 
	 */
	@Override
	public void beforeJob(JobExecution jobExecution) {
		BankTransactionUtility bankTransactionUtility = new BankTransactionUtility(properties);
		if (bankTransactionUtility.getFileName(BankTransactionUtility.READ).equals("")) {
			logger.error("Input File Not Found... -> " + jobExecution.getStatus());
			jobExecution.setStatus(BatchStatus.FAILED);
			jobExecution.stop();
		}
	}

	/**
	 * Overridden method - Once ItemReader and ItemWriter are executed, Perform
	 * file copy and reporting
	 * 
	 * @see org.springframework.batch.core.listener.JobExecutionListenerSupport#afterJob(org.springframework.batch.core.JobExecution)
	 * 
	 */
	@Override
	@StepScope
	public void afterJob(JobExecution jobExecution) {
		BankTransactionUtility bankTransactionUtility = new BankTransactionUtility(properties);
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("Read and Write Job Completed --> " + jobExecution.getStatus());
			BankTransactionDTO result = null;
			try {
				List<BankTransactionDTO> results = jdbcTemplate.query(properties.getSelectqry(),
						new RowMapper<BankTransactionDTO>() {
							@Override
							public BankTransactionDTO mapRow(ResultSet rs, int row) throws SQLException {
								return new BankTransactionDTO(rs.getBigDecimal(1), rs.getBigDecimal(2),
										rs.getBigDecimal(3));
							}
						});
				result = results.get(0);
			} catch (Exception e) {
				logger.error("Error Encountered while generating report -> " + e.toString());
			}
			if (result == null) {
				logger.error("Error Encountered: Unable to retrieve the execution details");
			} else {
				String newline = "\n";
				StringBuffer sbf = new StringBuffer("File Processed: ")
						.append(bankTransactionUtility.getFileName("read")).append(newline).append("Total Accounts: ")
						.append(result.getCount()).append(newline).append("Total Credits : ").append(result.getCredit())
						.append(newline).append("Total Debits  : ").append(result.getDebit()).append(newline)
						.append("Skipped Transactions : ").append(BankTransactionUtility.INVALIDRECORDS)
						.append(newline);

				try {
					Files.write(Paths.get(bankTransactionUtility.getFileName(BankTransactionUtility.JOBREPORT)),
							sbf.toString().getBytes());
					Files.move(Paths.get(bankTransactionUtility.getFileName(BankTransactionUtility.READ)),
							Paths.get(bankTransactionUtility.getFileName(BankTransactionUtility.WRITE)));
					logger.info("Completed all the tasks... ");
				} catch (IOException e) {
					logger.error("Error while creating/ moving the output files -> " + e.toString());
				}
			}
		}
		cleanup();
	}

	/**
	 * Method to clean up the table and reset the invalid records count - to
	 * prepare for the next batch
	 */
	private void cleanup() {
		BankTransactionUtility.INVALIDRECORDS = 0;
		try {
			jdbcTemplate.execute(properties.getDeleteqry());
			logger.debug("CLEARED THE TABLES");
		} catch (Exception e) {
			logger.error("Error after Job execution.. during Clean up -> " + e.toString());
		}
	}
}
