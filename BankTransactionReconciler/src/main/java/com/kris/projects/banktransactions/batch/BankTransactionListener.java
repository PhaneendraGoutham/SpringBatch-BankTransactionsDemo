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
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.kris.projects.banktransactions.dto.BankTransactionDTO;
import com.kris.projects.banktransactions.util.BankTransactionUtility;

@Component
public class BankTransactionListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(BankTransactionListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public BankTransactionListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");
			BankTransactionDTO result = null;

			List<BankTransactionDTO> results = jdbcTemplate.query(
					"select count(distinct account), sum(case when amount <0 then amount else 0 end) debit, sum(case when amount >0 then amount else 0 end) credit from customertransactions;",
					new RowMapper<BankTransactionDTO>() {
						@Override
						public BankTransactionDTO mapRow(ResultSet rs, int row) throws SQLException {
							return new BankTransactionDTO(rs.getBigDecimal(1), rs.getBigDecimal(2),
									rs.getBigDecimal(3));
						}
					});

			result = results.get(0);

			if (result == null) {
				log.error("Program Error: Unable to retrieve the execution details");
			} else {
				String newline = "\n";
				StringBuffer sbf = new StringBuffer("File Processed: ")
						.append(BankTransactionUtility.getFileName("read")).append(newline).append("Total Accounts: ")
						.append(result.getCount()).append(newline).append("Total Credits : ").append(result.getCredit())
						.append(newline).append("Total Debits  : ").append(result.getDebit()).append(newline)
						.append("Skipped Transactions : ").append(BankTransactionUtility.INVALIDRECORDS)
						.append(newline);

				try {
					Files.write(Paths.get(BankTransactionUtility.getFileName(BankTransactionUtility.JOBREPORT)),
							sbf.toString().getBytes());

				} catch (IOException e) {
					e.printStackTrace();
					log.error("Program Error: Unable to retrieve the execution details");
				}
			}
		}
	}

}
