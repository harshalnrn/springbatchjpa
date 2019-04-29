package com.example.springbatchjpa.batchConfig;

import com.example.springbatchjpa.Entity.Employee;
import org.hibernate.service.spi.InjectService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

@Configuration
@EnableBatchProcessing


@Transactional(propagation = Propagation.REQUIRED)
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory; // to build the job (job launcher, job repository behind the scenes)

    @Autowired
    private StepBuilderFactory stepBuilderFactory; //to build step

   @PersistenceUnit(name = "userEntityManager")
    EntityManagerFactory entityManagerFactory;

    @Autowired
    @Qualifier("transactionManager")
    PlatformTransactionManager transactionManager;

    /*@Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    Employee1Repository employee1Repository;*/

//try replacing resource configuration with @Value , instead of mnaually populating


    @Bean
    public Job exportCsvToH2(@Qualifier("step1") Step step1) {
        System.out.println("inside job method");
        return jobBuilderFactory.get("exportCsvToH2").incrementer(new RunIdIncrementer()).start(step1).build();   //flow() vs start()
    }

    @Bean
    public Step step1() throws FileNotFoundException,Exception {
        System.out.println("inside step method");

            return stepBuilderFactory.get("step1").transactionManager(transactionManager).<Employee, Employee>chunk(2).reader(reader1()).writer(writer1()).reader(reader2()).writer(writer2()).build();

    }

  /*  @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2").<Employee, Employee>chunk(1).reader(reader2()).writer(writer2()).build();
    }*/


    @Bean
    public FlatFileItemReader<Employee> reader1() throws FileNotFoundException {

        System.out.println("inside reader method");
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream("employee.txt"));  //shall look for file in root location: springbatchjpa
        FlatFileItemReader<Employee> flatFileItemReader = new FlatFileItemReader<Employee>();
        flatFileItemReader.setResource(inputStreamResource);
        flatFileItemReader.setLinesToSkip(1);
        //configure how each line shall be parsed and mapped to fields of entity bean
        flatFileItemReader
                .setLineMapper(new DefaultLineMapper() {
                    {
                        //set of columnnames in each row of csv
                        setLineTokenizer(new DelimitedLineTokenizer() {
                            {
                                setNames(new String[]{"id","password", "email", "sex", "age", "title", "salary"});
                            }
                        });
                        //Set values to fields of employee class. How does it do. will it look for fields with same name?
                        System.out.println("before mapping csv data to entity bean");
                        setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
                            {
                                setTargetType(Employee.class);
                            }
                        });
                    }
                });

        return flatFileItemReader;
    }


    //For chunk-based step reader and writer are mandatory.
    //If you don't want a writer use a No-operation ItemWriter that does nothing.


    @Bean
    public JpaItemWriter<Employee> writer1() {
        JpaItemWriter<Employee> writer = new JpaItemWriter();
            writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public JpaPagingItemReader<Employee> reader2() throws  Exception {
        JpaPagingItemReader<Employee> reader=new JpaPagingItemReader();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select e from Employee e");
        reader.setPageSize(5);
        reader.afterPropertiesSet();

        return reader;
    }
   /* @Bean
    public RepositoryItemWriter<Employee> writer1() {
        System.out.println("inside writer method");
        RepositoryItemWriter<Employee> repositoryItemWriter = new RepositoryItemWriter<Employee>();
        repositoryItemWriter.setRepository(employeeRepository);
        System.out.println("before persisting entity data");
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;
    }

    @Bean
    public RepositoryItemReader<Employee> reader2() {
        RepositoryItemReader<Employee> repositoryItemReader = new RepositoryItemReader<Employee>();
        repositoryItemReader.setRepository(employee1Repository);
        System.out.println("before reading database data");
        repositoryItemReader.setMethodName("findAll");
        repositoryItemReader.setPageSize(10);
        final HashMap<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);  //sort by id in asc order
        repositoryItemReader.setSort(sorts);
        return repositoryItemReader;*/

//note sorting manadatory for repositoryItemReader
        // }

    @Bean
    public FlatFileItemWriter<Employee> writer2() {

        FlatFileItemWriter<Employee> writer = new FlatFileItemWriter<Employee>();
        FileSystemResource fileSystemResource=new FileSystemResource("D:\\persons.txt");
        writer.setResource(fileSystemResource); //note any i-o class from core can be used //note this is only for classpath resourse, where path inside class path

        DelimitedLineAggregator<Employee> lineAggregator = new DelimitedLineAggregator<Employee>();
        lineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<Employee> fieldExtractor = new BeanWrapperFieldExtractor<Employee>();
        fieldExtractor.setNames(new String[]{"id","password", "email", "sex", "age", "title", "salary"});
        lineAggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(lineAggregator);
        return writer;
    }
    }
