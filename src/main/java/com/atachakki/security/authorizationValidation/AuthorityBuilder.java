package com.atachakki.security.authorizationValidation;

public final class AuthorityBuilder {

    private AuthorityBuilder() {}

    public static String shopAuthority(Long shopId, Object module, Object level) {
        return "SHOP_" + shopId + "_" + module + "_" + level;
    }

    public static String shopFull(Long shopId, Object module) {
        return "SHOP_" + shopId + "_" + module + "_FULL";
    }

    public static String owner(Long shopId) {
        return "SHOP_" + shopId + "_OWNER";
    }

    public static String shopkeeper(Long shopId) {
        return "SHOP_" + shopId + "_SHOPKEEPER";
    }
}
