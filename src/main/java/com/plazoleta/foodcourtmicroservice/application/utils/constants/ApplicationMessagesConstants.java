package com.plazoleta.foodcourtmicroservice.application.utils.constants;

public final class ApplicationMessagesConstants {

    public static final String RESTAURANT_SAVED_SUCCESSFULLY = "Restaurant saved successfully";
    public static final String DISH_SAVED_SUCCESSFULLY = "Dish saved successfully";
    public static final String DISH_UPDATED_SUCCESSFULLY = "Dish updated successfully";
    public static final String ORDER_ASSIGNED_SUCCESSFULLY = "Order assigned successfully and status changed to IN_PREPARATION";
    public static final String ORDER_MARKED_AS_READY_SUCCESSFULLY = "Order marked as ready successfully and notification sent to customer.";
    public static final String ORDER_DELIVERED_SUCCESSFULLY = "Order delivered successfully.";

    private ApplicationMessagesConstants() {
        throw new IllegalStateException("Utility class");
    }

}
