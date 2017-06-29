package com.kris.projects.banktransactions.batch;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import com.kris.projects.banktransactions.dto.BankTransactionDTO;

public class BankTransactionWriter {

	public JdbcBatchItemWriter<BankTransactionDTO> writeTransactions(DataSource dataSource) {
		JdbcBatchItemWriter<BankTransactionDTO> writer = new JdbcBatchItemWriter<BankTransactionDTO>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<BankTransactionDTO>());
		writer.setSql(
				"INSERT INTO customertransactions (account, amount) VALUES (:customerAccount, :transactionamount)");
		writer.setDataSource(dataSource);
		return writer;
	}

}
