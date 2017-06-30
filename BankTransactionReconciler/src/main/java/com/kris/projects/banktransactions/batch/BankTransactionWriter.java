package com.kris.projects.banktransactions.batch;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.stereotype.Component;

import com.kris.projects.banktransactions.dto.BankTransactionDTO;
import com.kris.projects.banktransactions.util.BankTransactionProperties;

@Component
public class BankTransactionWriter {
	private static final Logger logger = LoggerFactory.getLogger(BankTransactionReader.class);

	public JdbcBatchItemWriter<BankTransactionDTO> writeTransactions(DataSource dataSource,
			BankTransactionProperties properties) {
		JdbcBatchItemWriter<BankTransactionDTO> writer = new JdbcBatchItemWriter<BankTransactionDTO>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<BankTransactionDTO>());
		writer.setSql(properties.getInsertqry());
		writer.setDataSource(dataSource);
		return writer;
	}
}
