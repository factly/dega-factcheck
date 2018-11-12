package com.factly.dega.config;

import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
    import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchPersistentProperty;
    import org.springframework.data.mapping.PersistentEntity;
    import org.springframework.data.mapping.model.Property;
    import org.springframework.data.mapping.model.SimpleTypeHolder;

    public class CustomElasticsearchPersistentProperty extends SimpleElasticsearchPersistentProperty {

        public CustomElasticsearchPersistentProperty(Property property, PersistentEntity owner, SimpleTypeHolder simpleTypeHolder) {
            super(property, owner, simpleTypeHolder);
        }

        @Override
        public boolean isAssociation() {
            return false;
        }
    }
