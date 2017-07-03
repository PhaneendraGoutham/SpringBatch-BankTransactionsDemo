package com.kris.projects.banktransactions.scheduler;

import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * This Class contains beans required for simple job launch
 * 
 * @author Krishna Angeras
 *
 */
@EnableScheduling
public class BatchScheduler {

	@Bean
	public MapJobRepositoryFactoryBean mapJobRepositoryFactory() throws Exception {
		MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
		return factory;
	}

	@Bean
	public JobRepository jobRepository(MapJobRepositoryFactoryBean factory) throws Exception {
		return factory.getObject();
	}

	/**
	 * Method to configure and return a SimpleJobLauncher object
	 * 
	 * @param jobRepository
	 *            a SimpleJobRepository object
	 * @return launcher instance of SimpleJobLauncher
	 */
	@Bean
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(jobRepository);
		return launcher;
	}

}
