package com.plazoleta.foodcourtmicroservice.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantModelTest {

    @Test
    void testGettersAndSetters() {
        RestaurantModel model = new RestaurantModel();
        model.setId(1L);
        model.setName("Pizza Place");
        model.setNit("123456789");
        model.setAddress("Street 1");
        model.setPhoneNumber("+573001234567");
        model.setUrlLogo("logo.png");
        model.setOwnerId(10L);

        assertEquals(1L, model.getId());
        assertEquals("Pizza Place", model.getName());
        assertEquals("123456789", model.getNit());
        assertEquals("Street 1", model.getAddress());
        assertEquals("+573001234567", model.getPhoneNumber());
        assertEquals("logo.png", model.getUrlLogo());
        assertEquals(10L, model.getOwnerId());
    }

    @Test
    void testAllArgsConstructorAndEqualsHashCodeToString() {
        RestaurantModel model1 = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        RestaurantModel model2 = new RestaurantModel(1L, "Pizza Place", "123456789", "Street 1", "+573001234567", "logo.png", 10L);
        RestaurantModel model3 = new RestaurantModel(2L, "Other", "987654321", "Street 2", "+573009876543", "logo2.png", 20L);

        assertEquals(model1, model2);
        assertNotEquals(model1, model3);
        assertEquals(model1.hashCode(), model2.hashCode());
        assertNotEquals(model1.hashCode(), model3.hashCode());
        assertTrue(model1.toString().contains("Pizza Place"));
    }
}
