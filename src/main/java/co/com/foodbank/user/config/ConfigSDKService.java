package co.com.foodbank.user.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import co.com.foodbank.vault.sdk.service.SDKVaultService;

@Configuration
@Qualifier("sdkService")
public class ConfigSDKService {
    @Bean
    public SDKVaultService sdkService() {
        return new SDKVaultService();
    }


}
