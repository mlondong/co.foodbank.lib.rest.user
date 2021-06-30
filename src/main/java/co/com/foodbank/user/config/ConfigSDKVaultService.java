package co.com.foodbank.user.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import co.com.foodbank.vault.sdk.service.SDKVaultService;

/**
 * @author mauricio.londono@gmail.com co.com.foodbank.user.config 29/06/2021
 */
@Configuration
@Qualifier("sdkVaultService")
public class ConfigSDKVaultService {
    @Bean
    public SDKVaultService sdkVaultService() {
        return new SDKVaultService();
    }

}
