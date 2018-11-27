package com.factly.dega.repository.search;

import com.factly.dega.domain.Factcheck;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Factcheck entity.
 */
public interface FactcheckSearchRepository extends ElasticsearchRepository<Factcheck, String> {
}
