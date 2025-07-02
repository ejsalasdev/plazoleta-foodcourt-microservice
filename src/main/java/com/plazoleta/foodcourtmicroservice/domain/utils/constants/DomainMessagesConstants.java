package com.plazoleta.foodcourtmicroservice.domain.utils.constants;

public final class DomainMessagesConstants {

    public static final String RESTAURANT_NIT_ALREADY_EXISTS = "Restaurant with NIT %s already exists.";
    public static final String REQUIRED_FIELDS = "All required fields must be present.";

    public static final String RESTAURANT_NAME_REQUIRED = "Restaurant name is required.";
    public static final String RESTAURANT_NAME_NUMERIC = "Restaurant name cannot be only numbers.";

    public static final String NAME_ONLY_NUMBERS_REGEX = "\\d+";
    public static final String NIT_NUMERIC_REGEX = "\\d+";
    public static final String PHONE_REGEX = "^\\+?\\d+$";

    public static final String NIT_REQUIRED = "NIT is required.";
    public static final String NIT_NUMERIC = "NIT must be numeric.";

    public static final String PHONE_REQUIRED = "Phone number is required.";
    public static final String PHONE_LENGTH = "Phone number cannot be longer than %d characters.";
    public static final String PHONE_FORMAT = "Phone number must be numeric and may start with '+'.";

    public static final int PHONE_MAX_LENGTH = 13;

    private DomainMessagesConstants() {
        throw new IllegalStateException("Utility class");
    }
}
