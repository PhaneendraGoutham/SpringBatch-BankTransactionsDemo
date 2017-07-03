package com.kris.projects.banktransactions;

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
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
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

/**
 * SpringBatch Configuration class
 * 
 * Contains @EnableBatchProcessing annotation - to enable Spring Batch
 * Contains @EnableScheduling to enable Spring scheduler Imports BatchScheduler,
 * custom Spring Scheduler to launch the job
 * 
 * @author Krishna Angeras
 *
 */

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

	/**
	 * ItemReader Bean to read the csv file using FlatFileItemReader. Scope is
	 * set to StepScope ie a new instance will be created for each step
	 * 
	 * @return FlatFileItemReader implementation of ItemReader
	 */
	@Bean
	@StepScope
	public FlatFileItemReader<BankTransactionDTO> reader() {
		logger.debug("Inside READER---------<>");
		BankTransactionReader bankTransactionReader = new BankTransactionReader();
		return bankTransactionReader.transactionReader(properties);
	}

	/**
	 * ITEMPROCESSOR IS NOT USED - have kept for demo purpose only..
	 * ItemProcessor Bean Scope is set to StepScope ie a new instance will be
	 * created for each step
	 * 
	 * @return BankTransactionProcessor new instance
	 */
/*	@Bean
	@StepScope
	public BankTransactionProcessor processor() {
		return new BankTransactionProcessor();
	}*/

	/**
	 * ItemWriter bean to write data into db tables using JdbcBatchItemWriter
	 * Scope is set to StepScope ie a new instance will be created for each step
	 * 
	 * @return JdbcBatchItemWriter implementation of ItemWriter
	 */
	@Bean
	@StepScope
	public JdbcBatchItemWriter<BankTransactionDTO> writer() {
		BankTransactionWriter bankTransactionWriter = new BankTransactionWriter();
		return bankTransactionWriter.writeTransactions(dataSource, properties);
	}

	/**
	 * Method to schedule cron job. This method in turn uses the JobLauncher to
	 * launch the job defined in the Listener bean
	 */
	@Scheduled(cron = "${cronvalue}")
	public void perform() {

		logger.info("Job Started at :" + new Date());
		JobParameters param = new JobParametersBuilder().toJobParameters();
		JobExecution execution;
		try {
			execution = jobLauncher.run(listenerJob(listener), param);

			logger.info("Job finished with status :" + execution.getStatus());
			execution.getJobInstance().getId();
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException | RuntimeException e) {
			logger.error("Error in Job -> " + e.toString());

		} finally {
			execution = null;
			param = null;
		}

	}

	/**
	 * Method to build all the steps and execute the job using JobBuilderFactory
	 * Before and After job will be implemented in BankTransactionListener
	 * 
	 * @param listener
	 *            BankTransactionListener object
	 * @return Job
	 */
	public Job listenerJob(BankTransactionListener listener) {
		return jobBuilderFactory.get("listenerJob").incrementer(new RunIdIncrementer()).listener(listener).flow(step1())
				.end().build();
	}

	/**
	 * Method to define skip policy. In this case, the implementation skips few
	 * exceptions like ParseException, IncorrectTokenCountException, etc
	 * 
	 * @return InvalidBankTransactions SkipPolicy
	 */
	@Bean
	public SkipPolicy invalidTransactions() {
		return new InvalidBankTransactions();
	}

	/**
	 * Method to build step1 with reader, processor, writer and skip policy and
	 * return the step using StepBuilderFactory
	 * 
	 * @return Step1
	 */
	@Bean
	public Step step1() {
		Step step1 = stepBuilderFactory.get("step1").allowStartIfComplete(true)
				.transactionManager(new ResourcelessTransactionManager())
				.<BankTransactionDTO, BankTransactionDTO>chunk(1000).reader(reader()).faultTolerant()
				.skipPolicy(invalidTransactions()).writer(writer()).build();

		return step1;
	}

}
