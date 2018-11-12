package com.factly.dega.repository.search;

import com.factly.dega.domain.FactCheck;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FactCheck entity.
 */
public interface FactCheckSearchRepository extends ElasticsearchRepository<FactCheck, String> {
}
