package com.kris.projects.banktransactions.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kris.projects.banktransactions.batch.BankTransactionListener;
import com.kris.projects.banktransactions.batch.BankTransactionProcessor;
import com.kris.projects.banktransactions.batch.BankTransactionReader;
import com.kris.projects.banktransactions.batch.BankTransactionWriter;
import com.kris.projects.banktransactions.dto.BankTransactionDTO;

@Configuration
@EnableBatchProcessing
public class BankTransactionConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	@Bean
	public FlatFileItemReader<BankTransactionDTO> reader() {
		BankTransactionReader bankTransactionReader = new BankTransactionReader();
		return bankTransactionReader.transactionReader();
	}

	@Bean
	public BankTransactionProcessor processor() {
		return new BankTransactionProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<BankTransactionDTO> writer() {
		BankTransactionWriter bankTransactionWriter = new BankTransactionWriter();
		return bankTransactionWriter.writeTransactions(dataSource);
	}

	@Bean
	public Job listenerJob(BankTransactionListener listener) {
		return jobBuilderFactory.get("listenerJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(step1()).end().build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<BankTransactionDTO, BankTransactionDTO>chunk(10).reader(reader())
				.processor(processor()).writer(writer()).build();
	}

}
