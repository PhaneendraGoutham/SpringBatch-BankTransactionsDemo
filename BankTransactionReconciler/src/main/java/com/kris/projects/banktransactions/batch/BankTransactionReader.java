package com.kris.projects.banktransactions.batch;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.PathResource;

import com.kris.projects.banktransactions.dto.BankTransactionDTO;
import com.kris.projects.banktransactions.util.BankTransactionUtility;

public class BankTransactionReader {
	
	public FlatFileItemReader<BankTransactionDTO> transactionReader() {
		FlatFileItemReader<BankTransactionDTO> reader = new FlatFileItemReader<BankTransactionDTO>();
		reader.setResource(new PathResource(BankTransactionUtility.getFileName(BankTransactionUtility.READ)));
		reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<BankTransactionDTO>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] {BankTransactionUtility.CUSTOMER_ACCOUNT,
								BankTransactionUtility.TRANSACTION_AMOUNT });
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