package org.thshsh.spring;

import java.util.HashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

public abstract class DataSourceConfiguration {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);

    protected DatasourceProperties props;
    protected EnableJpaRepositories jpaAnnotation;
	
	public DataSourceConfiguration(DatasourceProperties props) {
		this.props = props;
		jpaAnnotation = this.getClass().getAnnotation(EnableJpaRepositories.class);
	}
	
	

    public DataSource dataSource() {
		DataSource ds = DataSourceBuilder.create().build();
		return ds;
    }
	

	public LocalContainerEntityManagerFactoryBean entityManager() {

	    LocalContainerEntityManagerFactoryBean em  = new LocalContainerEntityManagerFactoryBean();
	    em.setDataSource(dataSource());    
	    //em.setPackagesToScan( props.getPackages());
	    em.setPackagesToScan(jpaAnnotation.basePackages());
	    em.setPersistenceUnitName(props.getName());
	    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    em.setJpaVendorAdapter(vendorAdapter);
	    HashMap<String, Object> properties = new HashMap<>();
	    properties.putAll(props.getProps());
	    

	    if(LOGGER.isDebugEnabled()) {
		    LOGGER.debug("Properties:");
		    properties.forEach((k,v) -> {
		    	LOGGER.debug("{} = {}",k,v);
		    });
	    }
	    
	    em.setJpaPropertyMap(properties);
	    
	    return em;
	}
	



	public PlatformTransactionManager transactionManager() {
		LOGGER.info("create appTransactionManager");
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(entityManager().getObject());
	    return transactionManager;
	}
	
	
}