package com.demo.hotel_management;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.Formatter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

import static org.apache.http.protocol.HTTP.USER_AGENT;


/*
Created postgresql-tapered-66973 as DATABASE_URL
Creating app... done, ⬢ quiet-springs-81500
https://quiet-springs-81500.herokuapp.com/ | https://git.heroku.com/quiet-springs-81500.git

DATABASE_URL: postgres://fhcorgnllxvhkc:576936a50df4df8e74335d16c99045df829bfb75a2888933ed3d0807b429dc95@ec2-54-243-147-162.compute-1.amazonaws.com:5432/d4kpiadsgu8ckn
*/
@Slf4j
@Configuration
@EnableScheduling
@RestController
@SpringBootApplication
public class HotelManagementApplication {

    @Value("${app.domain}")
    private String domain;
    @Value("${datasource.driver}")
    private String datasourceDriver;
    @Value("${datasource.url}")
    private String datasourceUrl;
    @Value("${datasource.username}")
    private String datasourceUsername;
    @Value("${datasource.password}")
    private String datasourcePassword;

    public HotelManagementApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(HotelManagementApplication.class, args);
        log.debug("application started");
    }


    @Bean
    @Primary
    public DataSource postgresDataSource() {

        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(datasourceDriver);

        //todo

//        dataSource.setUrl("jdbc:postgresql://ec2-54-243-147-162.compute-1.amazonaws.com:5432/d4kpiadsgu8ckn");
//        dataSource.setUsername("fhcorgnllxvhkc");
//        dataSource.setPassword("576936a50df4df8e74335d16c99045df829bfb75a2888933ed3d0807b429dc95");

        dataSource.setUrl(datasourceUrl);
        dataSource.setUsername(datasourceUsername);
        dataSource.setPassword(datasourcePassword);


        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnReturn(true);
        dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setValidationMessageSource(messageSource());
        return factoryBean;
    }


    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(String text, Locale locale) {
                return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(object);
            }
        };
    }


    @Scheduled(fixedDelay = 600000)
    public void scheduleFixedDelayTask() {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(domain);

        request.addHeader("User-Agent", USER_AGENT);

        try {
            client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
