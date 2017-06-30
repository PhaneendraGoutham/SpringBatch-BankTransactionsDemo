package com.kris.projects.banktransactions.batch;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;

import com.kris.projects.banktransactions.dto.BankTransactionDTO;
import com.kris.projects.banktransactions.util.BankTransactionProperties;
import com.kris.projects.banktransactions.util.BankTransactionUtility;

@Component
public class BankTransactionReader {

	private static final Logger logger = LoggerFactory.getLogger(BankTransactionReader.class);

	public FlatFileItemReader<BankTransactionDTO> transactionReader(BankTransactionProperties properties) {
		BankTransactionUtility bankTransactionUtility = new BankTransactionUtility(properties);
		FlatFileItemReader<BankTransactionDTO> reader = new FlatFileItemReader<BankTransactionDTO>();
		String filePath = bankTransactionUtility.getFileName(BankTransactionUtility.READ);
		Path path = Paths.get(filePath);
		reader.setResource(new PathResource(path));
		reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<BankTransactionDTO>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { properties.getCustomeraccount(), properties.getTransactionamount() });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<BankTransactionDTO>() {
					{
						setTargetType(BankTransactionDTO.class);
					}
				});
			}
		});
		return reader;
	}
}