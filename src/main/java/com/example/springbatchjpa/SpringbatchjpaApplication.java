package com.example.springbatchjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

@SpringBootApplication/*(exclude={BatchAutoConfiguration.class})*/ //dont this. else batch config wont start
// Disable some Spring Boot auto config
        (exclude ={DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class })

@EnableTransactionManagement
public class SpringbatchjpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbatchjpaApplication.class, args);
    }


}


//Spring comes with lot of default behaviour,some controller by annotation, and some without them too.
//clarity needed on default behaviours, and ways to disable them, to write custom methods to populate API beans
