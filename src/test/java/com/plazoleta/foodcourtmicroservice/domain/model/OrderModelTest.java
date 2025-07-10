package com.plazoleta.foodcourtmicroservice.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;

class OrderModelTest {

    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurant Test", "123456789", "Test Address", 
                "3001234567", "https://logo.test.com", 1L);
    }

    private CategoryModel buildCategory(Long id) {
        return new CategoryModel(id, "Test Category", "Test Description");
    }

    private DishModel buildDish(Long id, RestaurantModel restaurant, CategoryModel category) {
        return new DishModel(id, "Test Dish", BigDecimal.valueOf(15000), "Test Description", 
                "https://image.test.com", category, restaurant, true);
    }

    private OrderDishModel buildOrderDish(Long id, DishModel dish, Integer quantity) {
        OrderDishModel orderDish = new OrderDishModel();
        orderDish.setId(id);
        orderDish.setDish(dish);
        orderDish.setQuantity(quantity);
        return orderDish;
    }

    @Test
    void when_createOrderModelWithAllFields_then_fieldsAreSetCorrectly() {
        // Arrange
        Long id = 1L;
        Long customerId = 123L;
        LocalDateTime date = LocalDateTime.now();
        OrderStatusEnum status = OrderStatusEnum.PENDING;
        Long employeeId = 456L;
        RestaurantModel restaurant = buildRestaurant(1L);
        CategoryModel category = buildCategory(1L);
        DishModel dish = buildDish(1L, restaurant, category);
        OrderDishModel orderDish = buildOrderDish(1L, dish, 2);
        List<OrderDishModel> orderDishes = List.of(orderDish);

        // Act
        OrderModel order = new OrderModel(id, customerId, date, status, employeeId, restaurant, orderDishes);

        // Assert
        assertEquals(id, order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(date, order.getDate());
        assertEquals(status, order.getStatus());
        assertEquals(employeeId, order.getEmployeeId());
        assertEquals(restaurant, order.getRestaurant());
        assertEquals(orderDishes, order.getOrderDishes());
        assertEquals(1, order.getOrderDishes().size());
        assertEquals(orderDish, order.getOrderDishes().get(0));
    }

    @Test
    void when_createEmptyOrderModel_then_fieldsAreNull() {
        // Act
        OrderModel order = new OrderModel();

        // Assert
        assertNull(order.getId());
        assertNull(order.getCustomerId());
        assertNull(order.getDate());
        assertNull(order.getStatus());
        assertNull(order.getEmployeeId());
        assertNull(order.getRestaurant());
        assertNull(order.getOrderDishes());
    }

    @Test
    void when_settersAreUsed_then_fieldsAreUpdated() {
        // Arrange
        OrderModel order = new OrderModel();
        Long id = 2L;
        Long customerId = 789L;
        LocalDateTime date = LocalDateTime.now().minusHours(1);
        OrderStatusEnum status = OrderStatusEnum.IN_PREPARATION;
        Long employeeId = 101L;
        RestaurantModel restaurant = buildRestaurant(2L);
        CategoryModel category = buildCategory(1L);
        DishModel dish = buildDish(1L, restaurant, category);
        OrderDishModel orderDish = buildOrderDish(1L, dish, 3);
        List<OrderDishModel> orderDishes = new ArrayList<>();
        orderDishes.add(orderDish);

        // Act
        order.setId(id);
        order.setCustomerId(customerId);
        order.setDate(date);
        order.setStatus(status);
        order.setEmployeeId(employeeId);
        order.setRestaurant(restaurant);
        order.setOrderDishes(orderDishes);

        // Assert
        assertEquals(id, order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(date, order.getDate());
        assertEquals(status, order.getStatus());
        assertEquals(employeeId, order.getEmployeeId());
        assertEquals(restaurant, order.getRestaurant());
        assertEquals(orderDishes, order.getOrderDishes());
        assertEquals(1, order.getOrderDishes().size());
    }

    @Test
    void when_orderWithNullEmployeeId_then_employeeIdIsNull() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        List<OrderDishModel> orderDishes = new ArrayList<>();

        // Act
        OrderModel order = new OrderModel(1L, 123L, LocalDateTime.now(), 
                OrderStatusEnum.PENDING, null, restaurant, orderDishes);

        // Assert
        assertEquals(1L, order.getId());
        assertEquals(123L, order.getCustomerId());
        assertEquals(OrderStatusEnum.PENDING, order.getStatus());
        assertNull(order.getEmployeeId());
        assertEquals(restaurant, order.getRestaurant());
        assertEquals(orderDishes, order.getOrderDishes());
    }

    @Test
    void when_orderWithDifferentStatuses_then_statusesAreSetCorrectly() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        List<OrderDishModel> orderDishes = new ArrayList<>();

        // Act & Assert - PENDING
        OrderModel pendingOrder = new OrderModel(1L, 123L, LocalDateTime.now(), 
                OrderStatusEnum.PENDING, null, restaurant, orderDishes);
        assertEquals(OrderStatusEnum.PENDING, pendingOrder.getStatus());

        // Act & Assert - IN_PREPARATION
        OrderModel inPrepOrder = new OrderModel(2L, 123L, LocalDateTime.now(), 
                OrderStatusEnum.IN_PREPARATION, 456L, restaurant, orderDishes);
        assertEquals(OrderStatusEnum.IN_PREPARATION, inPrepOrder.getStatus());

        // Act & Assert - READY
        OrderModel readyOrder = new OrderModel(3L, 123L, LocalDateTime.now(), 
                OrderStatusEnum.READY, 456L, restaurant, orderDishes);
        assertEquals(OrderStatusEnum.READY, readyOrder.getStatus());

        // Act & Assert - DELIVERED
        OrderModel deliveredOrder = new OrderModel(4L, 123L, LocalDateTime.now(), 
                OrderStatusEnum.DELIVERED, 456L, restaurant, orderDishes);
        assertEquals(OrderStatusEnum.DELIVERED, deliveredOrder.getStatus());

        // Act & Assert - CANCELLED
        OrderModel cancelledOrder = new OrderModel(5L, 123L, LocalDateTime.now(), 
                OrderStatusEnum.CANCELLED, null, restaurant, orderDishes);
        assertEquals(OrderStatusEnum.CANCELLED, cancelledOrder.getStatus());
    }

    @Test
    void when_orderWithMultipleOrderDishes_then_orderDishesAreSetCorrectly() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        CategoryModel category = buildCategory(1L);
        DishModel dish1 = buildDish(1L, restaurant, category);
        DishModel dish2 = buildDish(2L, restaurant, category);
        OrderDishModel orderDish1 = buildOrderDish(1L, dish1, 2);
        OrderDishModel orderDish2 = buildOrderDish(2L, dish2, 1);
        List<OrderDishModel> orderDishes = List.of(orderDish1, orderDish2);

        // Act
        OrderModel order = new OrderModel(1L, 123L, LocalDateTime.now(), 
                OrderStatusEnum.PENDING, null, restaurant, orderDishes);

        // Assert
        assertEquals(2, order.getOrderDishes().size());
        assertTrue(order.getOrderDishes().contains(orderDish1));
        assertTrue(order.getOrderDishes().contains(orderDish2));
        assertEquals(orderDish1, order.getOrderDishes().get(0));
        assertEquals(orderDish2, order.getOrderDishes().get(1));
    }

    @Test
    void when_equalsAndHashCode_then_workCorrectly() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        LocalDateTime date = LocalDateTime.now();
        List<OrderDishModel> orderDishes = new ArrayList<>();

        OrderModel order1 = new OrderModel(1L, 123L, date, OrderStatusEnum.PENDING, 
                null, restaurant, orderDishes);
        OrderModel order2 = new OrderModel(1L, 123L, date, OrderStatusEnum.PENDING, 
                null, restaurant, orderDishes);
        OrderModel order3 = new OrderModel(2L, 123L, date, OrderStatusEnum.PENDING, 
                null, restaurant, orderDishes);

        // Assert
        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
        assertNotEquals(order1, order3);
        assertNotEquals(order1.hashCode(), order3.hashCode());
        assertEquals(order1, order2);
        assertNotEquals(order1, order3);
        assertNotEquals(null, order1);
        assertNotEquals("not an order", order1);
    }

    @Test
    void when_toString_then_containsAllFields() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        LocalDateTime date = LocalDateTime.now();
        List<OrderDishModel> orderDishes = new ArrayList<>();

        OrderModel order = new OrderModel(1L, 123L, date, OrderStatusEnum.PENDING, 
                456L, restaurant, orderDishes);

        // Act
        String toString = order.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("123"));
        assertTrue(toString.contains("PENDING"));
        assertTrue(toString.contains("456"));
    }
}
