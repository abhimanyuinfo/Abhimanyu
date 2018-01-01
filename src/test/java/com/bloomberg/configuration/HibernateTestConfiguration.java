package com.bloomberg.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * This class is same as real HibernateConfiguration class in sources.
 * Only difference is that method dataSource & hibernateProperties 
 * implementations are specific to Hibernate working with MySql database.
 */

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.bloomberg.dao" })
public class HibernateTestConfiguration {

	@Autowired
	private Environment environment;
	
	private final Logger logger = Logger.getLogger(HibernateTestConfiguration.class);


	@Bean(name = "dataSource")
	public DataSource dataSource() {
		logger.info("dataSource Method started..");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/bloomberg_db?rewriteBatchedStatements=true");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		logger.info("dataSource Method ended..");
		return dataSource;
	}

	
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory s) {
		logger.info("transactionManager Method started..");
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		logger.info("transactionManager Method ended..");
		return txManager;
	}
}
