package com.factly.dega.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String SUPERADMIN = "ROLE_SUPERADMIN";

    public static final String ADMINISTRATOR = "ROLE_ADMINISTRATOR";

    public static final String EDITOR = "ROLE_EDITOR";

    public static final String AUTHOR = "ROLE_AUTHOR";

    public static final String CONTRIBUTOR = "ROLE_CONTRIBUTOR";

    public static final String SUBSCRIBER = "ROLE_SUBSCRIBER";

    private AuthoritiesConstants() {
    }
}
