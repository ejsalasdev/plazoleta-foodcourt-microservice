package com.plazoleta.foodcourtmicroservice.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class DishModelTest {

    private RestaurantModel buildRestaurant(Long id) {
        return new RestaurantModel(id, "Restaurante Prueba", "NIT123", "Calle 1", "123456789", "https://logo.com/logo.jpg", 1L);
    }

    @Test
    void when_createDishModelWithAllFields_then_fieldsAreSetCorrectly() {
        DishModel dish = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "url", 2L, buildRestaurant(10L), true);
        assertEquals(1L, dish.getId());
        assertEquals("Pizza", dish.getName());
        assertEquals(new BigDecimal("10000"), dish.getPrice());
        assertEquals("desc", dish.getDescription());
        assertEquals("url", dish.getUrlImage());
        assertEquals(2L, dish.getCategoryId());
        assertEquals(10L, dish.getRestaurant().getId());
        assertTrue(dish.getActive());
    }

    @Test
    void when_settersAreUsed_then_fieldsAreUpdated() {
        DishModel dish = new DishModel();
        dish.setId(2L);
        dish.setName("Burger");
        dish.setPrice(new BigDecimal("5000"));
        dish.setDescription("desc2");
        dish.setUrlImage("img");
        dish.setCategoryId(3L);
        dish.setRestaurant(buildRestaurant(11L));
        dish.setActive(false);
        assertEquals(2L, dish.getId());
        assertEquals("Burger", dish.getName());
        assertEquals(new BigDecimal("5000"), dish.getPrice());
        assertEquals("desc2", dish.getDescription());
        assertEquals("img", dish.getUrlImage());
        assertEquals(3L, dish.getCategoryId());
        assertEquals(11L, dish.getRestaurant().getId());
        assertFalse(dish.getActive());
    }

    @Test
    void when_equalsAndHashCode_then_worksById() {
        DishModel d1 = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "url", 2L, buildRestaurant(10L), true);
        DishModel d2 = new DishModel(1L, "Other", new BigDecimal("20000"), "desc2", "url2", 3L, buildRestaurant(11L), false);
        DishModel d3 = new DishModel(2L, "Pizza", new BigDecimal("10000"), "desc", "url", 2L, buildRestaurant(10L), true);
        assertEquals(d1, d2);
        assertNotEquals(d1, d3);
        assertEquals(d1.hashCode(), d2.hashCode());
        assertNotEquals(d1.hashCode(), d3.hashCode());
    }

    @Test
    void when_toString_then_containsAllFields() {
        DishModel dish = new DishModel(1L, "Pizza", new BigDecimal("10000"), "desc", "url", 2L, buildRestaurant(10L), true);
        String str = dish.toString();
        assertTrue(str.contains("Pizza"));
        assertTrue(str.contains("10000"));
        assertTrue(str.contains("desc"));
        assertTrue(str.contains("url"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("true"));
    }
}
