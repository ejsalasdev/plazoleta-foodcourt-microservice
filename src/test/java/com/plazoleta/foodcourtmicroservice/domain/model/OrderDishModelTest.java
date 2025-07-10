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

class OrderDishModelTest {

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

    private OrderModel buildOrder(Long id, RestaurantModel restaurant) {
        List<OrderDishModel> orderDishes = new ArrayList<>();
        return new OrderModel(id, 123L, LocalDateTime.now(), OrderStatusEnum.PENDING, 
                null, restaurant, orderDishes);
    }

    @Test
    void when_createOrderDishModelWithAllFields_then_fieldsAreSetCorrectly() {
        // Arrange
        Long id = 1L;
        RestaurantModel restaurant = buildRestaurant(1L);
        CategoryModel category = buildCategory(1L);
        OrderModel order = buildOrder(1L, restaurant);
        DishModel dish = buildDish(1L, restaurant, category);
        Integer quantity = 3;

        // Act
        OrderDishModel orderDish = new OrderDishModel(id, order, dish, quantity);

        // Assert
        assertEquals(id, orderDish.getId());
        assertEquals(order, orderDish.getOrder());
        assertEquals(dish, orderDish.getDish());
        assertEquals(quantity, orderDish.getQuantity());
    }

    @Test
    void when_createEmptyOrderDishModel_then_fieldsAreNull() {
        // Act
        OrderDishModel orderDish = new OrderDishModel();

        // Assert
        assertNull(orderDish.getId());
        assertNull(orderDish.getOrder());
        assertNull(orderDish.getDish());
        assertNull(orderDish.getQuantity());
    }

    @Test
    void when_settersAreUsed_then_fieldsAreUpdated() {
        // Arrange
        OrderDishModel orderDish = new OrderDishModel();
        Long id = 2L;
        RestaurantModel restaurant = buildRestaurant(2L);
        CategoryModel category = buildCategory(1L);
        OrderModel order = buildOrder(2L, restaurant);
        DishModel dish = buildDish(2L, restaurant, category);
        Integer quantity = 5;

        // Act
        orderDish.setId(id);
        orderDish.setOrder(order);
        orderDish.setDish(dish);
        orderDish.setQuantity(quantity);

        // Assert
        assertEquals(id, orderDish.getId());
        assertEquals(order, orderDish.getOrder());
        assertEquals(dish, orderDish.getDish());
        assertEquals(quantity, orderDish.getQuantity());
    }

    @Test
    void when_orderDishWithDifferentQuantities_then_quantitiesAreSetCorrectly() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        CategoryModel category = buildCategory(1L);
        OrderModel order = buildOrder(1L, restaurant);
        DishModel dish = buildDish(1L, restaurant, category);

        // Act & Assert - Quantity 1
        OrderDishModel orderDish1 = new OrderDishModel(1L, order, dish, 1);
        assertEquals(Integer.valueOf(1), orderDish1.getQuantity());

        // Act & Assert - Quantity 10
        OrderDishModel orderDish10 = new OrderDishModel(2L, order, dish, 10);
        assertEquals(Integer.valueOf(10), orderDish10.getQuantity());

        // Act & Assert - Large quantity
        OrderDishModel orderDishLarge = new OrderDishModel(3L, order, dish, 100);
        assertEquals(Integer.valueOf(100), orderDishLarge.getQuantity());
    }

    @Test
    void when_orderDishWithSameDishDifferentQuantities_then_orderDishesAreDifferent() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        CategoryModel category = buildCategory(1L);
        OrderModel order = buildOrder(1L, restaurant);
        DishModel dish = buildDish(1L, restaurant, category);

        // Act
        OrderDishModel orderDish1 = new OrderDishModel(1L, order, dish, 2);
        OrderDishModel orderDish2 = new OrderDishModel(2L, order, dish, 3);

        // Assert
        assertNotEquals(orderDish1.getId(), orderDish2.getId());
        assertEquals(orderDish1.getOrder(), orderDish2.getOrder());
        assertEquals(orderDish1.getDish(), orderDish2.getDish());
        assertNotEquals(orderDish1.getQuantity(), orderDish2.getQuantity());
    }

    @Test
    void when_orderDishWithDifferentDishes_then_dishesAreSetCorrectly() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        CategoryModel category = buildCategory(1L);
        OrderModel order = buildOrder(1L, restaurant);
        DishModel dish1 = buildDish(1L, restaurant, category);
        DishModel dish2 = buildDish(2L, restaurant, category);

        // Act
        OrderDishModel orderDish1 = new OrderDishModel(1L, order, dish1, 2);
        OrderDishModel orderDish2 = new OrderDishModel(2L, order, dish2, 2);

        // Assert
        assertNotEquals(orderDish1.getDish().getId(), orderDish2.getDish().getId());
        assertEquals(orderDish1.getQuantity(), orderDish2.getQuantity());
        assertEquals(orderDish1.getOrder(), orderDish2.getOrder());
    }

    @Test
    void when_orderDishWithDifferentOrders_then_ordersAreSetCorrectly() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        CategoryModel category = buildCategory(1L);
        OrderModel order1 = buildOrder(1L, restaurant);
        OrderModel order2 = buildOrder(2L, restaurant);
        DishModel dish = buildDish(1L, restaurant, category);

        // Act
        OrderDishModel orderDish1 = new OrderDishModel(1L, order1, dish, 2);
        OrderDishModel orderDish2 = new OrderDishModel(2L, order2, dish, 2);

        // Assert
        assertNotEquals(orderDish1.getOrder().getId(), orderDish2.getOrder().getId());
        assertEquals(orderDish1.getDish(), orderDish2.getDish());
        assertEquals(orderDish1.getQuantity(), orderDish2.getQuantity());
    }

    @Test
    void when_equalsAndHashCode_then_workCorrectly() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        CategoryModel category = buildCategory(1L);
        OrderModel order = buildOrder(1L, restaurant);
        DishModel dish = buildDish(1L, restaurant, category);

        OrderDishModel orderDish1 = new OrderDishModel(1L, order, dish, 2);
        OrderDishModel orderDish2 = new OrderDishModel(1L, order, dish, 2);
        OrderDishModel orderDish3 = new OrderDishModel(2L, order, dish, 2);

        // Assert
        assertEquals(orderDish1, orderDish2);
        assertEquals(orderDish1.hashCode(), orderDish2.hashCode());
        assertNotEquals(orderDish1, orderDish3);
        assertNotEquals(orderDish1.hashCode(), orderDish3.hashCode());
        assertEquals(orderDish1, orderDish2);
        assertNotEquals(orderDish1, orderDish3);
        assertNotEquals(null, orderDish1);
        assertNotEquals("not an order dish", orderDish1);
    }

    @Test
    void when_toString_then_containsAllFields() {
        // Arrange
        RestaurantModel restaurant = buildRestaurant(1L);
        CategoryModel category = buildCategory(1L);
        OrderModel order = buildOrder(1L, restaurant);
        DishModel dish = buildDish(1L, restaurant, category);

        OrderDishModel orderDish = new OrderDishModel(1L, order, dish, 2);

        // Act
        String toString = orderDish.toString();

        // Assert
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("2"));
    }

    @Test
    void when_settingNullFields_then_fieldsAreNull() {
        // Arrange
        OrderDishModel orderDish = new OrderDishModel();

        // Act
        orderDish.setId(null);
        orderDish.setOrder(null);
        orderDish.setDish(null);
        orderDish.setQuantity(null);

        // Assert
        assertNull(orderDish.getId());
        assertNull(orderDish.getOrder());
        assertNull(orderDish.getDish());
        assertNull(orderDish.getQuantity());
    }

    @Test
    void when_creatingOrderDishWithValidData_then_allFieldsAreMaintained() {
        // Arrange
        Long expectedId = 100L;
        RestaurantModel restaurant = buildRestaurant(5L);
        CategoryModel category = buildCategory(3L);
        OrderModel order = buildOrder(10L, restaurant);
        DishModel dish = buildDish(15L, restaurant, category);
        Integer expectedQuantity = 7;

        // Act
        OrderDishModel orderDish = new OrderDishModel(expectedId, order, dish, expectedQuantity);

        // Assert
        assertEquals(expectedId, orderDish.getId());
        assertEquals(order, orderDish.getOrder());
        assertEquals(10L, orderDish.getOrder().getId());
        assertEquals(dish, orderDish.getDish());
        assertEquals(15L, orderDish.getDish().getId());
        assertEquals(expectedQuantity, orderDish.getQuantity());
        assertEquals(restaurant, orderDish.getDish().getRestaurant());
        assertEquals(category, orderDish.getDish().getCategory());
    }
}
