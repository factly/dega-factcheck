package com.factly.dega.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ClaimantSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ClaimantSearchRepositoryMockConfiguration {

    @MockBean
    private ClaimantSearchRepository mockClaimantSearchRepository;

}
