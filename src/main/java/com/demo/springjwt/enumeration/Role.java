package com.demo.springjwt.enumeration;

public enum Role {

    ROLE_USER( "user:read"),
    ROLE_HR("user:read", "user:update"),
    ROLE_MANAGER("user:read", "user:update"),
    ROLE_ADMIN("user:read", "user:create", "user:update"),
    ROLE_SUPER_ADMIN("user:read", "user:create", "user:update", "user:delete");

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }

}
