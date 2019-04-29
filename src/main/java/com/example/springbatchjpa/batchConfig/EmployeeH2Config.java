package com.example.springbatchjpa.batchConfig;

import com.example.springbatchjpa.utility.PropertyFileProcessor;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

//class level mapping of persistence units to JPA repositories classes
@Configuration
@EnableTransactionManagement

//@PropertySource({"classpath:${ext.properties.dir:classpath:}/application-config.properties"}). This can be used instead of manually reading resource, and populating environment
/*@EnableJpaRepositories(      //manages lifecycle of persistence context units for JPA repositores
        basePackages = "com.example.springbatchjpa.JPARepository",
        entityManagerFactoryRef= "employeeEntityManager",
        transactionManagerRef= "employeeTransactionManager"
)*/
public class EmployeeH2Config {

    private PropertyFileProcessor propertyFileProcessor = new PropertyFileProcessor();


    @Bean(name = "userEntityManager")
    @DependsOn({"userDataSource"})
    public LocalContainerEntityManagerFactoryBean userEntityManager() {    //load entity manager and map it to entity bean
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(userDataSource());
        // em.setPackagesToScan(
        //  new String[]{"com.example.springbatchjpa.Entity"}); //entity manager manages lifecycle of entities
        em.setPersistenceUnitName("userEntityManager");
        em.setPackagesToScan("com.example.springbatchjpa.Entity");

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("spring.jpa.hibernate.ddl-auto",
                propertyFileProcessor.getPropertyValue("spring.jpa.hibernate.ddl-auto"));
        properties.put("spring.jpa.database-platform",
                propertyFileProcessor.getPropertyValue("spring.jpa.database-platform"));

        properties.put("spring.jpa.show-sql",
                propertyFileProcessor.getPropertyValue("spring.jpa.show-sql"));
        properties.put("spring.jpa.generate-ddl",
                propertyFileProcessor.getPropertyValue("spring.jpa.generate-ddl"));
        properties.put("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults",
                propertyFileProcessor.getPropertyValue("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults"));
        properties.put("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation",
                propertyFileProcessor.getPropertyValue("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation"));

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public DataSource userDataSource() {

        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        dataSource.setDriverClassName(propertyFileProcessor.getPropertyValue("spring.datasource.driverClassName"));
        dataSource.setUrl(propertyFileProcessor.getPropertyValue("spring.datasource.url"));
        dataSource.setUsername(propertyFileProcessor.getPropertyValue("spring.datasource.username"));
        dataSource.setPassword(propertyFileProcessor.getPropertyValue("spring.datasource.password"));
        return dataSource;
    }


    //below can be used if application.properties within aplication.
   /* @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
*/
    @Bean(name="transactionManager")
    public PlatformTransactionManager employeeTransactionManager(@Qualifier("userDataSource") DataSource dataSource, @Qualifier("userEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
//loading transaction with em, ds
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
      //  transactionManager.setPersistenceUnitName("employeeTransactionManager");
        transactionManager.setDataSource(dataSource);
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

  /*  @Bean
    @Override
    public PlatformTransactionManager getTransactionManager() {
        DataSource dataSource=userDataSource();
        return employeeTransactionManager(dataSource);
    }*/

   /* @Bean
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(userDataSource());
        factory.setTransactionManager(getTransactionManager());
        factory.afterPropertiesSet();
        return (JobRepository) factory.getObject();
    }

    @Bean
    public JobLauncher getJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    @Override
    public JobExplorer getJobExplorer() throws Exception {
        JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
        factoryBean.setDataSource(userDataSource());
      //  factoryBean.setTablePrefix("SYSTEM.");
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }*/
}

//insert dependenies into method, instead of calling method again and again