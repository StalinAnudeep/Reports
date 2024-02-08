package com.spdcl.config;

import java.beans.PropertyVetoException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.spdcl"})
@PropertySource("classpath:application.properties")
@EnableScheduling
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
    private Environment env;

	@Bean
	public InternalResourceViewResolver resolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/").setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)).resourceChain(false);
        registry.addResourceHandler("/demo/**").addResourceLocations("classpath:/static/demo/").setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)).resourceChain(false);
    }
	
	/*
	 * @Bean DriverManagerDataSource getDataSource() { DriverManagerDataSource ds =
	 * new DriverManagerDataSource();
	 * ds.setDriverClassName(env.getRequiredProperty("jdbc.driverClassName"));
	 * ds.setUrl(env.getRequiredProperty("jdbc.url"));
	 * ds.setUsername(env.getRequiredProperty("jdbc.username"));
	 * ds.setPassword(env.getRequiredProperty("jdbc.password")); return ds; }
	 */
	
	@Bean(name = "db1")
	ComboPooledDataSource  getDataSource() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(env.getRequiredProperty("jdbc.driverClassName"));
			cpds.setJdbcUrl( env.getRequiredProperty("jdbc.url"));
			cpds.setUser(env.getRequiredProperty("jdbc.username"));                                  
			cpds.setPassword(env.getRequiredProperty("jdbc.password"));
			cpds.setMinPoolSize(10);                                     
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(50);
			cpds.setMaxConnectionAge(900);
			cpds.setMaxIdleTime(600);
			cpds.setMaxIdleTimeExcessConnections(300);
			cpds.setTestConnectionOnCheckout(true);
		} catch (IllegalStateException | PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
		return cpds;
	}
	
	
	
	@Bean(name = "db2")
	ComboPooledDataSource  getDataSource1() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(env.getRequiredProperty("spdcl.jdbc.driverClassName"));
			cpds.setJdbcUrl( env.getRequiredProperty("spdcl.jdbc.url"));
			cpds.setUser(env.getRequiredProperty("spdcl.jdbc.username"));                                  
			cpds.setPassword(env.getRequiredProperty("spdcl.jdbc.password"));
			cpds.setMinPoolSize(10);                                     
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(50);
			cpds.setMaxConnectionAge(900);
			cpds.setMaxIdleTime(600);
			cpds.setMaxIdleTimeExcessConnections(300);
			cpds.setTestConnectionOnCheckout(true);
		} catch (IllegalStateException | PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
		return cpds;
	}
	
	
	@Bean(name = "db3")
	ComboPooledDataSource  getDataSource2() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(env.getRequiredProperty("cpdcl.jdbc.driverClassName"));
			cpds.setJdbcUrl( env.getRequiredProperty("cpdcl.jdbc.url"));
			cpds.setUser(env.getRequiredProperty("cpdcl.jdbc.username"));                                  
			cpds.setPassword(env.getRequiredProperty("cpdcl.jdbc.password"));
			cpds.setMinPoolSize(10);                                     
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(50);
			cpds.setMaxConnectionAge(900);
			cpds.setMaxIdleTime(600);
			cpds.setMaxIdleTimeExcessConnections(300);
			cpds.setTestConnectionOnCheckout(true);
		} catch (IllegalStateException | PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
		return cpds;
	}
	
	@Bean(name = "db4")
	ComboPooledDataSource  getDevDataSource() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(env.getRequiredProperty("dev.jdbc.driverClassName"));
			cpds.setJdbcUrl( env.getRequiredProperty("dev.jdbc.url"));
			cpds.setUser(env.getRequiredProperty("dev.jdbc.username"));                                  
			cpds.setPassword(env.getRequiredProperty("dev.jdbc.password"));
			cpds.setMinPoolSize(10);                                     
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(50);
			cpds.setMaxConnectionAge(900);
			cpds.setMaxIdleTime(600);
			cpds.setMaxIdleTimeExcessConnections(300);
			cpds.setTestConnectionOnCheckout(true);
		} catch (IllegalStateException | PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
		return cpds;
	}
	
	@Bean(name = "db5")
	ComboPooledDataSource  getLTDevDataSource() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(env.getRequiredProperty("ltdev.jdbc.driverClassName"));
			cpds.setJdbcUrl( env.getRequiredProperty("ltdev.jdbc.url"));
			cpds.setUser(env.getRequiredProperty("ltdev.jdbc.username"));                                  
			cpds.setPassword(env.getRequiredProperty("ltdev.jdbc.password"));
			cpds.setMinPoolSize(10);                                     
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(50);
			cpds.setMaxConnectionAge(900);
			cpds.setMaxIdleTime(600);
			cpds.setMaxIdleTimeExcessConnections(300);
			cpds.setTestConnectionOnCheckout(true);
		} catch (IllegalStateException | PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
		return cpds;
	}
	
	
	
	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("db1") ComboPooledDataSource ds) {
	    return new JdbcTemplate(ds);
	}
	
	@Bean(name = "jdbcTemplate1")
	public JdbcTemplate jdbcTemplate1(@Qualifier("db2") ComboPooledDataSource ds) {
	    return new JdbcTemplate(ds);
	}
	
	@Bean(name = "jdbcTemplate2")
	public JdbcTemplate jdbcTemplate2(@Qualifier("db3") ComboPooledDataSource ds) {
	    return new JdbcTemplate(ds);
	}
	
	@Bean(name = "jdbcTemplateDev")
	public JdbcTemplate jdbcTemplateDev(@Qualifier("db4") ComboPooledDataSource ds) {
	    return new JdbcTemplate(ds);
	}
	
	@Bean(name = "jdbcTemplateLTDev")
	public JdbcTemplate jdbcTemplateLTDev(@Qualifier("db5") ComboPooledDataSource ds) {
	    return new JdbcTemplate(ds);
	}
	
	@Bean(name="transactionManager")
	public PlatformTransactionManager txManager(){
	    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(getDataSource());
	    return transactionManager;
	}	
	
	
}
