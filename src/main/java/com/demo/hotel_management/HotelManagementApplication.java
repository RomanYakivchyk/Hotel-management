package com.demo.hotel_management;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.sql.DataSource;
import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
@Configuration
public class HotelManagementApplication {
    private static Logger logger = LoggerFactory.getLogger(HotelManagementApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(HotelManagementApplication.class, args);
        logger.debug("application started");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
