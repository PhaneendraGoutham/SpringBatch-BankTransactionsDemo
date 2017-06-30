package com.kris.projects.banktransactions.config;

import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.kris.projects.banktransactions.batch.BankTransactionListener;
import com.kris.projects.banktransactions.batch.BankTransactionProcessor;
import com.kris.projects.banktransactions.batch.BankTransactionReader;
import com.kris.projects.banktransactions.batch.BankTransactionWriter;
import com.kris.projects.banktransactions.dto.BankTransactionDTO;
import com.kris.projects.banktransactions.scheduler.BatchScheduler;
import com.kris.projects.banktransactions.util.BankTransactionProperties;
import com.kris.projects.banktransactions.util.InvalidBankTransactions;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
@EnableScheduling
public class BankTransactionConfig {

	private static final Logger logger = LoggerFactory.getLogger(BankTransactionConfig.class);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	@Autowired
	private SimpleJobLauncher jobLauncher;

	@Autowired
	BankTransactionListener listener;

	@Autowired
	BankTransactionProperties properties;

	@Bean
	@StepScope
	public FlatFileItemReader<BankTransactionDTO> reader() {
		BankTransactionReader bankTransactionReader = new BankTransactionReader();
		return bankTransactionReader.transactionReader(properties);
	}

	@Bean
	@StepScope
	public BankTransactionProcessor processor() {
		return new BankTransactionProcessor();
	}

	@Bean
	@StepScope
	public JdbcBatchItemWriter<BankTransactionDTO> writer() {
		BankTransactionWriter bankTransactionWriter = new BankTransactionWriter();
		return bankTransactionWriter.writeTransactions(dataSource, properties);
	}

	@Scheduled(cron = "00 51 02 * * *")
	public void perform() {

		logger.info("Job Started at :" + new Date());
		JobParameters param = new JobParametersBuilder().toJobParameters();

		JobExecution execution;
		try {
			execution = jobLauncher.run(listenerJob(listener), param);
			logger.info("Job finished with status :" + execution.getStatus());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException | RuntimeException e) {
			logger.error("Error in Job -> " + e.toString());
		}

	}

	public Job listenerJob(BankTransactionListener listener) {
		return jobBuilderFactory.get("listenerJob").incrementer(new RunIdIncrementer()).listener(listener).flow(step1())
				.end().build();
	}

	@Bean
	public SkipPolicy invalidTransactions() {
		return new InvalidBankTransactions();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<BankTransactionDTO, BankTransactionDTO>chunk(1000).reader(reader())
				.faultTolerant().skipPolicy(invalidTransactions()).processor(processor()).writer(writer()).build();
	}

}
