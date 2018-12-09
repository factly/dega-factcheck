package com.factly.dega.config;

import com.factly.dega.aop.clientdetails.ClientDetailsAspect;
import io.github.jhipster.config.JHipsterConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAspectJAutoProxy
public class ClientDetailsAspectConfiguration {

    @Bean
    @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
    public ClientDetailsAspect clientDetailsAspect(Environment env, RestTemplate restTemplate, @Value("${dega.core.url}") String coreServiceUrl) {
        return new ClientDetailsAspect(env, restTemplate, coreServiceUrl);
    }
}
