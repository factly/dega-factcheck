package com.factly.dega.repository.search;

import com.factly.dega.domain.Claimant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Claimant entity.
 */
public interface ClaimantSearchRepository extends ElasticsearchRepository<Claimant, String> {
}
