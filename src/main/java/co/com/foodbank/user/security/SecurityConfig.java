package co.com.foodbank.user.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.security 5/07/2021
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable().authorizeRequests().antMatchers("/user")
                .permitAll()
                .antMatchers("/user/updateVaultInProvider/**",
                        "/user/updateContribution/**")
                .access("hasIpAddress('127.0.0.1/24')").anyRequest()
                .permitAll();



    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }
}


