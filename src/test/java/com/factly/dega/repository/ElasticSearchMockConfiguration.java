package com.factly.dega.repository;

import io.searchbox.client.JestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchMockConfiguration {

    @MockBean
    private JestClient testClient;

}
