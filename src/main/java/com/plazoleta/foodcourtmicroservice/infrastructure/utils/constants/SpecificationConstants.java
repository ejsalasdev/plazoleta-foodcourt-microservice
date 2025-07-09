package com.plazoleta.foodcourtmicroservice.infrastructure.utils.constants;

public final class SpecificationConstants {

    // Entity field names
    public static final String RESTAURANT_FIELD = "restaurant";
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String OWNER_ID_FIELD = "ownerId";
    public static final String CATEGORY_ID_FIELD = "categoryId";

    private SpecificationConstants() {
        throw new IllegalStateException("Utility class");
    }
}
