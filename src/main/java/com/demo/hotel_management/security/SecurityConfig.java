package com.demo.hotel_management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${spring.security.mail}")
    private String mail;
    @Value("${spring.security.password}")
    private String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .rememberMe()
                .tokenValiditySeconds(24 * 60 * 60)
                .key("uniqueAndSecret")
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(mail).password("{noop}" + password).roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers( "/ajax/**",
                        "/bootstrap/**",
                        "/bootstrap-select/**",
                        "/data-tables/**",
                        "/date-picker/**",
                        "/fontawesome/**",
                        "/js/**",
                        "/footer.css",
                        "/jqeury.mask.js",
                        "/moment.js");
    }
}