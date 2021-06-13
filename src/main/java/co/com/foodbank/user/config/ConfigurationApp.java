package co.com.foodbank.user.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import co.com.foodbank.vault.sdk.config.EnableVaultSDK;

@Configuration
@EnableVaultSDK
@ComponentScan(basePackages = "co.com.foodbank.user")
public class ConfigurationApp {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }
}
