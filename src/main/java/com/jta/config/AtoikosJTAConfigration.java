package com.jta.config;

import java.util.Properties;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.atomikos.datasource.pool.ConnectionFactory;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.icatch.jta.hibernate3.TransactionManagerLookup;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@Configuration
public class AtoikosJTAConfigration {

	@Inject
	private Environment environment;
	
	public void tailorProperties(Properties properties) {
		properties.setProperty("hibernate.transaction.manager_lookup_class", 
				TransactionManagerLookup.class.getName());		
	}
	
	@Bean
	public UserTransaction userTransaction() throws Throwable{
		UserTransactionImp userTransactionImp = new UserTransactionImp();
		userTransactionImp.setTransactionTimeout(1000);
		return userTransactionImp;
	}
	
	@Bean(initMethod="init", destroyMethod="close")
	public TransactionManager transactionManager() throws Throwable{
		UserTransactionManager userTransactionManager = new UserTransactionManager();
		userTransactionManager.setForceShutdown(false);
		return userTransactionManager;
	}
	
	@Bean(initMethod="init", destroyMethod="close")
	public DataSource dataSource() {
		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		xaDataSource.setUniqueResourceName("xads");
		return xaDataSource;
	}
	
	/*@Bean(initMethod="init",destroyMethod="close")
	public ConnectionFactory connectionFactory() {
		
	}*/
}
