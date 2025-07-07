package com.plazoleta.foodcourtmicroservice.domain.utils.constants;

public final class DomainMessagesConstants {

    public static final String USER_IS_NOT_OWNER = "The user with id %d is not an OWNER.";
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

    public static final String DISH_NAME_REQUIRED = "Dish name is required.";
    public static final String DISH_NAME_NOT_EMPTY = "Dish name cannot be empty.";
    public static final String DISH_DESCRIPTION_REQUIRED = "Dish description is required.";
    public static final String DISH_PRICE_REQUIRED = "Dish price is required.";
    public static final String DISH_PRICE_GREATER_THAN_ZERO = "Dish price must be greater than 0.";
    public static final String DISH_PRICE_MUST_BE_INTEGER = "Dish price must be an integer.";
    public static final String DISH_CATEGORY_ID_REQUIRED = "Category ID is required.";
    public static final String DISH_RESTAURANT_ID_REQUIRED = "Restaurant ID is required.";

    public static final String DISH_ALREADY_EXISTS = "A dish with the same name '%s' already exists in this restaurant.";
    public static final String DISH_NOT_FOUND_IN_RESTAURANT = "Dish with id %d does not belong to restaurant with id %d or does not exist.";
    public static final String DISH_IMAGE_URL_INVALID = "Dish image URL is invalid.";
    public static final String URL_IMAGE_REGEX = "^(https?://).+\\.(jpg|jpeg|png|gif|bmp)$";

    private DomainMessagesConstants() {
        throw new IllegalStateException("Utility class");
    }
}
