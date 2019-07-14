package com.factly.dega.config.dbmigrations;

import com.factly.dega.domain.Authority;
import com.factly.dega.domain.User;
import com.factly.dega.security.AuthoritiesConstants;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;

/**
 * Creates the initial database setup
 */
@ChangeLog(order = "001")
public class InitialSetupMigration {

    @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
    public void addAuthorities(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        Authority superAdminAuthority = new Authority();
        superAdminAuthority.setName(AuthoritiesConstants.SUPERADMIN);
        Authority administratorAuthority = new Authority();
        administratorAuthority.setName(AuthoritiesConstants.ADMINISTRATOR);
        Authority editorAuthority = new Authority();
        editorAuthority.setName(AuthoritiesConstants.EDITOR);
        Authority authorAuthority = new Authority();
        authorAuthority.setName(AuthoritiesConstants.AUTHOR);
        Authority contributorAuthority = new Authority();
        contributorAuthority.setName(AuthoritiesConstants.CONTRIBUTOR);
        Authority subscriberAuthority = new Authority();
        subscriberAuthority.setName(AuthoritiesConstants.SUBSCRIBER);
        mongoTemplate.save(adminAuthority);
        mongoTemplate.save(userAuthority);
        mongoTemplate.save(superAdminAuthority);
        mongoTemplate.save(administratorAuthority);
        mongoTemplate.save(editorAuthority);
        mongoTemplate.save(authorAuthority);
        mongoTemplate.save(contributorAuthority);
        mongoTemplate.save(subscriberAuthority);
    }
}
