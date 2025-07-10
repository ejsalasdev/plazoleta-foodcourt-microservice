package com.plazoleta.foodcourtmicroservice.infrastructure.utils.constants;

public final class SpecificationConstants {

    // Entity field names
    public static final String RESTAURANT_FIELD = "restaurant";
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String OWNER_ID_FIELD = "ownerId";
    public static final String CATEGORY_ID_FIELD = "categoryId";
    public static final String CATEGORY_FIELD = "category";
    public static final String CUSTOMER_ID_FIELD = "customerId";
    public static final String STATUS_FIELD = "status";

    private SpecificationConstants() {
        throw new IllegalStateException("Utility class");
    }
}
