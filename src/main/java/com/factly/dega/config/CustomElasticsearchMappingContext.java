package com.factly.dega.config;

import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
    import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
    import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchPersistentEntity;
    import org.springframework.data.mapping.model.Property;
    import org.springframework.data.mapping.model.SimpleTypeHolder;

    public class CustomElasticsearchMappingContext extends SimpleElasticsearchMappingContext {
        @Override
        protected ElasticsearchPersistentProperty createPersistentProperty(Property property, SimpleElasticsearchPersistentEntity owner, SimpleTypeHolder simpleTypeHolder) {
            return new CustomElasticsearchPersistentProperty(property, owner, simpleTypeHolder);
        }
    }
