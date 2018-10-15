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

import javax.sql.DataSource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
@SpringBootApplication
public class HotelManagementApplication {


//    private String ipAddress = InetAddress.getLocalHost().getHostAddress();
//    @Value("${local.server.port}")
//    private static int port;

    public HotelManagementApplication() throws UnknownHostException {
    }

    public static void main(String[] args) {
        SpringApplication.run(HotelManagementApplication.class, args);
        log.debug("application started");
    }

    @Bean
    @Primary
    public DataSource postgresDataSource() {
//        String databaseUrl = System.getenv("DATABASE_URL");
////        log.info("Initializing PostgreSQL database: {}", databaseUrl);
////
////        URI dbUri;
////        try {
////            dbUri = new URI(databaseUrl);
////        }
////        catch (URISyntaxException e) {
////            log.error(String.format("Invalid DATABASE_URL: %s", databaseUrl), e);
////            return null;
////        }
////
////        String username = dbUri.getUserInfo().split(":")[1];
////        String password = dbUri.getUserInfo().split(":")[2];
////        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
////                + dbUri.getPort() + dbUri.getPath();
//
//        String dbUrl = databaseUrl.
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");


        dataSource.setUrl("jdbc:postgresql://ec2-54-243-147-162.compute-1.amazonaws.com:5432/d4kpiadsgu8ckn");
        dataSource.setUsername("fhcorgnllxvhkc");
        dataSource.setPassword("576936a50df4df8e74335d16c99045df829bfb75a2888933ed3d0807b429dc95");

//        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("");


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
    public void scheduleFixedDelayTask() throws IOException {

        String url = "https://quiet-springs-81500.herokuapp.com/clients";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        request.addHeader("User-Agent", USER_AGENT);
        client.execute(request);
    }

}
