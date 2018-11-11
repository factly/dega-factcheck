package com.factly.dega.repository.search;

import com.factly.dega.domain.Claim;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Claim entity.
 */
public interface ClaimSearchRepository extends ElasticsearchRepository<Claim, String> {
}
