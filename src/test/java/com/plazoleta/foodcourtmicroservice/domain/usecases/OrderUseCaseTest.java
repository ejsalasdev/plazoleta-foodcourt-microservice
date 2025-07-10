package com.plazoleta.foodcourtmicroservice.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.CustomOrderException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.CustomerHasActiveOrderException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.OrderDishValidationException;
import com.plazoleta.foodcourtmicroservice.domain.model.CategoryModel;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderDishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.AuthenticatedUserPort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.OrderPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.UserServicePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.PaginationValidatorChain;

class OrderUseCaseTest {

    @Mock
    private OrderPersistencePort orderPersistencePort;

    @Mock
    private RestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private DishPersistencePort dishPersistencePort;

    @Mock
    private AuthenticatedUserPort authenticatedUserPort;

    @Mock
    private UserServicePort userServicePort;

    @Mock
    private PaginationValidatorChain paginationValidatorChain;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private RestaurantModel restaurant;
    private DishModel dish;
    private CategoryModel category;
    private OrderModel orderModel;
    private OrderDishModel orderDishModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Build test data
        category = buildCategory(1L);
        restaurant = buildRestaurant(1L);
        dish = buildDish(1L, restaurant, category);
        orderDishModel = buildOrderDish(1L, dish, 2);
        orderModel = buildOrder(1L, restaurant, List.of(orderDishModel));
    }

    @Test
    void when_CreateOrder_Expect_Success() {
        // Arrange
        Long customerId = 123L;
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(customerId);
        when(orderPersistencePort.hasActiveOrdersForCustomer(customerId)).thenReturn(false);
        when(restaurantPersistencePort.findRestaurantById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(dishPersistencePort.findDishById(dish.getId())).thenReturn(Optional.of(dish));

        OrderModel savedOrder = buildSavedOrder(1L, customerId, restaurant, List.of(orderDishModel));
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(savedOrder);

        // Act
        OrderModel result = orderUseCase.createOrder(orderModel);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(customerId, result.getCustomerId());
        assertEquals(OrderStatusEnum.PENDING, result.getStatus());
        assertEquals(restaurant.getId(), result.getRestaurant().getId());
        assertEquals(1, result.getOrderDishes().size());
        assertEquals(2, result.getOrderDishes().get(0).getQuantity());

        verify(orderPersistencePort, times(1)).hasActiveOrdersForCustomer(customerId);
        verify(restaurantPersistencePort, times(1)).findRestaurantById(restaurant.getId());
        verify(dishPersistencePort, times(1)).findDishById(dish.getId());
        verify(orderPersistencePort, times(1)).saveOrder(any(OrderModel.class));
    }

    @Test
    void when_CreateOrderWithActiveOrder_Expect_CustomerHasActiveOrderException() {
        // Arrange
        Long customerId = 123L;
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(customerId);
        when(orderPersistencePort.hasActiveOrdersForCustomer(customerId)).thenReturn(true);

        // Act & Assert
        CustomerHasActiveOrderException exception = assertThrows(
                CustomerHasActiveOrderException.class,
                () -> orderUseCase.createOrder(orderModel));

        assertEquals(DomainMessagesConstants.ORDER_CUSTOMER_HAS_ACTIVE_ORDER, exception.getMessage());

        verify(orderPersistencePort, times(1)).hasActiveOrdersForCustomer(customerId);
        verify(restaurantPersistencePort, never()).findRestaurantById(anyLong());
        verify(dishPersistencePort, never()).findDishById(anyLong());
        verify(orderPersistencePort, never()).saveOrder(any(OrderModel.class));
    }

    @Test
    void when_CreateOrderWithInvalidRestaurant_Expect_CustomOrderException() {
        // Arrange
        Long customerId = 123L;
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(customerId);
        when(orderPersistencePort.hasActiveOrdersForCustomer(customerId)).thenReturn(false);
        when(restaurantPersistencePort.findRestaurantById(restaurant.getId())).thenReturn(Optional.empty());

        // Act & Assert
        CustomOrderException exception = assertThrows(
                CustomOrderException.class,
                () -> orderUseCase.createOrder(orderModel));

        assertEquals(DomainMessagesConstants.RESTAURANT_NOT_FOUND_MESSAGE, exception.getMessage());

        verify(orderPersistencePort, times(1)).hasActiveOrdersForCustomer(customerId);
        verify(restaurantPersistencePort, times(1)).findRestaurantById(restaurant.getId());
        verify(dishPersistencePort, never()).findDishById(anyLong());
        verify(orderPersistencePort, never()).saveOrder(any(OrderModel.class));
    }

    @Test
    void when_CreateOrderWithInvalidDish_Expect_CustomOrderException() {
        // Arrange
        Long customerId = 123L;
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(customerId);
        when(orderPersistencePort.hasActiveOrdersForCustomer(customerId)).thenReturn(false);
        when(restaurantPersistencePort.findRestaurantById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(dishPersistencePort.findDishById(dish.getId())).thenReturn(Optional.empty());

        // Act & Assert
        CustomOrderException exception = assertThrows(
                CustomOrderException.class,
                () -> orderUseCase.createOrder(orderModel));

        assertEquals(DomainMessagesConstants.DISH_NOT_FOUND_MESSAGE, exception.getMessage());

        verify(orderPersistencePort, times(1)).hasActiveOrdersForCustomer(customerId);
        verify(restaurantPersistencePort, times(1)).findRestaurantById(restaurant.getId());
        verify(dishPersistencePort, times(1)).findDishById(dish.getId());
        verify(orderPersistencePort, never()).saveOrder(any(OrderModel.class));
    }

    @Test
    void when_CreateOrderWithDishFromDifferentRestaurant_Expect_OrderDishValidationException() {
        // Arrange
        Long customerId = 123L;
        RestaurantModel differentRestaurant = buildRestaurant(2L);
        DishModel dishFromDifferentRestaurant = buildDish(1L, differentRestaurant, category);

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(customerId);
        when(orderPersistencePort.hasActiveOrdersForCustomer(customerId)).thenReturn(false);
        when(restaurantPersistencePort.findRestaurantById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(dishPersistencePort.findDishById(dish.getId())).thenReturn(Optional.of(dishFromDifferentRestaurant));

        // Act & Assert
        OrderDishValidationException exception = assertThrows(
                OrderDishValidationException.class,
                () -> orderUseCase.createOrder(orderModel));

        assertEquals(DomainMessagesConstants.ORDER_DISH_DIFFERENT_RESTAURANT, exception.getMessage());

        verify(orderPersistencePort, times(1)).hasActiveOrdersForCustomer(customerId);
        verify(restaurantPersistencePort, times(1)).findRestaurantById(restaurant.getId());
        verify(dishPersistencePort, times(1)).findDishById(dish.getId());
        verify(orderPersistencePort, never()).saveOrder(any(OrderModel.class));
    }

    @Test
    void when_CreateOrderWithInactiveDish_Expect_OrderDishValidationException() {
        // Arrange
        Long customerId = 123L;
        DishModel inactiveDish = buildInactiveDish(1L, restaurant, category);

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(customerId);
        when(orderPersistencePort.hasActiveOrdersForCustomer(customerId)).thenReturn(false);
        when(restaurantPersistencePort.findRestaurantById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(dishPersistencePort.findDishById(dish.getId())).thenReturn(Optional.of(inactiveDish));

        // Act & Assert
        OrderDishValidationException exception = assertThrows(
                OrderDishValidationException.class,
                () -> orderUseCase.createOrder(orderModel));

        assertEquals(DomainMessagesConstants.ORDER_DISH_NOT_ACTIVE, exception.getMessage());

        verify(orderPersistencePort, times(1)).hasActiveOrdersForCustomer(customerId);
        verify(restaurantPersistencePort, times(1)).findRestaurantById(restaurant.getId());
        verify(dishPersistencePort, times(1)).findDishById(dish.getId());
        verify(orderPersistencePort, never()).saveOrder(any(OrderModel.class));
    }

    @Test
    void when_CreateOrderWithMultipleDishes_Expect_Success() {
        // Arrange
        Long customerId = 123L;
        DishModel dish2 = buildDish(2L, restaurant, category);
        OrderDishModel orderDish2 = buildOrderDish(2L, dish2, 1);
        OrderModel orderWithMultipleDishes = buildOrder(1L, restaurant, List.of(orderDishModel, orderDish2));

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(customerId);
        when(orderPersistencePort.hasActiveOrdersForCustomer(customerId)).thenReturn(false);
        when(restaurantPersistencePort.findRestaurantById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(dishPersistencePort.findDishById(dish.getId())).thenReturn(Optional.of(dish));
        when(dishPersistencePort.findDishById(dish2.getId())).thenReturn(Optional.of(dish2));

        OrderModel savedOrder = buildSavedOrder(1L, customerId, restaurant, List.of(orderDishModel, orderDish2));
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(savedOrder);

        // Act
        OrderModel result = orderUseCase.createOrder(orderWithMultipleDishes);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(customerId, result.getCustomerId());
        assertEquals(OrderStatusEnum.PENDING, result.getStatus());
        assertEquals(2, result.getOrderDishes().size());

        verify(orderPersistencePort, times(1)).hasActiveOrdersForCustomer(customerId);
        verify(restaurantPersistencePort, times(1)).findRestaurantById(restaurant.getId());
        verify(dishPersistencePort, times(1)).findDishById(dish.getId());
        verify(dishPersistencePort, times(1)).findDishById(dish2.getId());
        verify(orderPersistencePort, times(1)).saveOrder(any(OrderModel.class));
    }

    @Test
    void when_GetOrdersByRestaurantAndStatus_WithValidEmployeeAndStatus_Expect_PagedOrdersReturned() {
        // Arrange
        Long employeeId = 123L;
        List<String> roles = List.of(DomainMessagesConstants.EMPLOYEE_ROLE);
        OrderStatusEnum status = OrderStatusEnum.PENDING;
        Integer page = 0;
        Integer size = 10;
        Long restaurantId = 1L;

        RestaurantModel testRestaurant = buildRestaurant(restaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, testRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);
        OrderModel order = buildSavedOrder(1L, 456L, testRestaurant, List.of(orderDish));

        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(roles);
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrdersByRestaurantIdAndStatus(restaurantId, status, page, size))
                .thenReturn(createPageInfo(List.of(order), 1, 1, page, size));

        // Act
        var result = orderUseCase.getOrdersByRestaurantAndStatus(status, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(order.getId(), result.getContent().get(0).getId());

        verify(authenticatedUserPort).getCurrentUserRoles();
        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrdersByRestaurantIdAndStatus(restaurantId, status, page, size);
    }

    @Test
    void when_GetOrdersByRestaurantAndStatus_WithNonEmployeeRole_Expect_CustomOrderException() {
        // Arrange
        List<String> roles = List.of("CUSTOMER");
        OrderStatusEnum status = OrderStatusEnum.PENDING;
        Integer page = 0;
        Integer size = 10;

        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(roles);

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.getOrdersByRestaurantAndStatus(status, page, size));

        assertEquals(DomainMessagesConstants.EMPLOYEE_NOT_AUTHORIZED, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserRoles();
        verify(authenticatedUserPort, never()).getCurrentUserId();
        verify(userServicePort, never()).getUserRestaurantId(anyLong());
        verify(orderPersistencePort, never()).findOrdersByRestaurantIdAndStatus(anyLong(), any(), any(), any());
    }

    @Test
    void when_GetOrdersByRestaurantAndStatus_WithEmployeeNotAssociatedWithRestaurant_Expect_CustomOrderException() {
        // Arrange
        Long employeeId = 123L;
        List<String> roles = List.of(DomainMessagesConstants.EMPLOYEE_ROLE);
        OrderStatusEnum status = OrderStatusEnum.PENDING;
        Integer page = 0;
        Integer size = 10;

        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(roles);
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(null);

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.getOrdersByRestaurantAndStatus(status, page, size));

        assertEquals(DomainMessagesConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserRoles();
        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort, never()).findOrdersByRestaurantIdAndStatus(anyLong(), any(), any(), any());
    }

    @Test
    void when_GetOrdersByRestaurantAndStatus_WithInvalidPageNumber_Expect_CustomOrderException() {
        // Arrange
        OrderStatusEnum status = OrderStatusEnum.PENDING;
        Integer page = -1; // Invalid page number
        Integer size = 10;

        doThrow(new CustomOrderException(DomainMessagesConstants.PAGINATION_PAGE_NUMBER_INVALID))
                .when(paginationValidatorChain).validate(page, size);

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.getOrdersByRestaurantAndStatus(status, page, size));

        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_NUMBER_INVALID, exception.getMessage());

        verify(paginationValidatorChain).validate(page, size);
        verify(authenticatedUserPort, never()).getCurrentUserRoles();
        verify(authenticatedUserPort, never()).getCurrentUserId();
        verify(userServicePort, never()).getUserRestaurantId(anyLong());
        verify(orderPersistencePort, never()).findOrdersByRestaurantIdAndStatus(anyLong(), any(), any(), any());
    }

    @Test
    void when_GetOrdersByRestaurantAndStatus_WithInvalidPageSize_Expect_CustomOrderException() {
        // Arrange
        OrderStatusEnum status = OrderStatusEnum.PENDING;
        Integer page = 0;
        Integer size = 0; // Invalid page size

        doThrow(new CustomOrderException(DomainMessagesConstants.PAGINATION_PAGE_SIZE_INVALID))
                .when(paginationValidatorChain).validate(page, size);

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.getOrdersByRestaurantAndStatus(status, page, size));

        assertEquals(DomainMessagesConstants.PAGINATION_PAGE_SIZE_INVALID, exception.getMessage());

        verify(paginationValidatorChain).validate(page, size);
        verify(authenticatedUserPort, never()).getCurrentUserRoles();
        verify(authenticatedUserPort, never()).getCurrentUserId();
        verify(userServicePort, never()).getUserRestaurantId(anyLong());
        verify(orderPersistencePort, never()).findOrdersByRestaurantIdAndStatus(anyLong(), any(), any(), any());
    }

    @Test
    void when_GetOrdersByRestaurantAndStatus_WithValidParametersAndEmptyResult_Expect_EmptyPagedResponse() {
        // Arrange
        Long employeeId = 123L;
        List<String> roles = List.of(DomainMessagesConstants.EMPLOYEE_ROLE);
        OrderStatusEnum status = OrderStatusEnum.PENDING;
        Integer page = 0;
        Integer size = 10;
        Long restaurantId = 1L;

        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(roles);
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrdersByRestaurantIdAndStatus(restaurantId, status, page, size))
                .thenReturn(createPageInfo(List.of(), 0, 0, page, size));

        // Act
        var result = orderUseCase.getOrdersByRestaurantAndStatus(status, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());

        verify(authenticatedUserPort).getCurrentUserRoles();
        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrdersByRestaurantIdAndStatus(restaurantId, status, page, size);
    }

    // Helper methods for building test data
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

    private DishModel buildInactiveDish(Long id, RestaurantModel restaurant, CategoryModel category) {
        return new DishModel(id, "Inactive Dish", BigDecimal.valueOf(15000), "Inactive Description",
                "https://image.test.com", category, restaurant, false);
    }

    private OrderDishModel buildOrderDish(Long id, DishModel dish, Integer quantity) {
        OrderDishModel orderDish = new OrderDishModel();
        orderDish.setId(id);
        orderDish.setDish(dish);
        orderDish.setQuantity(quantity);
        return orderDish;
    }

    private OrderModel buildOrder(Long id, RestaurantModel restaurant, List<OrderDishModel> orderDishes) {
        OrderModel order = new OrderModel();
        order.setId(id);
        order.setRestaurant(restaurant);
        order.setOrderDishes(new ArrayList<>(orderDishes));
        return order;
    }

    private OrderModel buildSavedOrder(Long id, Long customerId, RestaurantModel restaurant,
            List<OrderDishModel> orderDishes) {
        OrderModel order = new OrderModel();
        order.setId(id);
        order.setCustomerId(customerId);
        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.PENDING);
        order.setEmployeeId(null);
        order.setRestaurant(restaurant);
        order.setOrderDishes(new ArrayList<>(orderDishes));
        return order;
    }

    private com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo<OrderModel> createPageInfo(
            List<OrderModel> content, long totalElements, int totalPages, int currentPage, int pageSize) {
        boolean hasNext = currentPage < totalPages - 1;
        boolean hasPrevious = currentPage > 0;
        return new com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo<>(
                content, totalElements, totalPages, currentPage, pageSize, hasNext, hasPrevious);
    }
}
