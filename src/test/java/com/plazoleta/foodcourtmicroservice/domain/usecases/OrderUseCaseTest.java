package com.plazoleta.foodcourtmicroservice.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
import com.plazoleta.foodcourtmicroservice.domain.ports.out.NotificationServicePort;
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
    NotificationServicePort notificationServicePort;

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

    // Tests for assignOrderToEmployeeAndChangeStatus

    @Test
    void when_AssignOrderToEmployee_WithValidEmployeeAndPendingOrder_Expect_OrderAssignedAndStatusChanged() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;
        List<String> roles = List.of(DomainMessagesConstants.EMPLOYEE_ROLE);

        RestaurantModel testRestaurant = buildRestaurant(restaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, testRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel pendingOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        pendingOrder.setStatus(OrderStatusEnum.PENDING);
        pendingOrder.setEmployeeId(null);

        OrderModel assignedOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        assignedOrder.setStatus(OrderStatusEnum.IN_PREPARATION);
        assignedOrder.setEmployeeId(employeeId);

        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(roles);
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(orderPersistencePort.updateOrder(any(OrderModel.class))).thenReturn(assignedOrder);

        // Act
        OrderModel result = orderUseCase.assignOrderToEmployeeAndChangeStatus(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatusEnum.IN_PREPARATION, result.getStatus());
        assertEquals(employeeId, result.getEmployeeId());

        verify(authenticatedUserPort).getCurrentUserRoles();
        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort).updateOrder(any(OrderModel.class));
    }

    @Test
    void when_AssignOrderToEmployee_WithNonEmployeeRole_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        List<String> roles = List.of("CUSTOMER");

        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(roles);

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.assignOrderToEmployeeAndChangeStatus(orderId));

        assertEquals(DomainMessagesConstants.EMPLOYEE_NOT_AUTHORIZED, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserRoles();
        verify(authenticatedUserPort, never()).getCurrentUserId();
        verify(userServicePort, never()).getUserRestaurantId(anyLong());
        verify(orderPersistencePort, never()).findOrderById(anyLong());
        verify(orderPersistencePort, never()).updateOrder(any(OrderModel.class));
    }

    @Test
    void when_AssignOrderToEmployee_WithEmployeeNotAssociatedWithRestaurant_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        List<String> roles = List.of(DomainMessagesConstants.EMPLOYEE_ROLE);

        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(roles);
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(null);

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.assignOrderToEmployeeAndChangeStatus(orderId));

        assertEquals(DomainMessagesConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserRoles();
        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort, never()).findOrderById(anyLong());
        verify(orderPersistencePort, never()).updateOrder(any(OrderModel.class));
    }

    @Test
    void when_AssignOrderToEmployee_WithOrderNotFound_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;
        List<String> roles = List.of(DomainMessagesConstants.EMPLOYEE_ROLE);

        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(roles);
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.assignOrderToEmployeeAndChangeStatus(orderId));

        assertEquals(DomainMessagesConstants.ORDER_NOT_FOUND, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserRoles();
        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any(OrderModel.class));
    }

    @Test
    void when_AssignOrderToEmployee_WithOrderFromDifferentRestaurant_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long employeeRestaurantId = 1L;
        Long orderRestaurantId = 2L; // Different restaurant
        List<String> roles = List.of(DomainMessagesConstants.EMPLOYEE_ROLE);

        RestaurantModel orderRestaurant = buildRestaurant(orderRestaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, orderRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel pendingOrder = buildSavedOrder(orderId, 456L, orderRestaurant, List.of(orderDish));
        pendingOrder.setStatus(OrderStatusEnum.PENDING);

        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(roles);
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(employeeRestaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(pendingOrder));

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.assignOrderToEmployeeAndChangeStatus(orderId));

        assertEquals(DomainMessagesConstants.ORDER_NOT_FROM_EMPLOYEE_RESTAURANT, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserRoles();
        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any(OrderModel.class));
    }

    @Test
    void when_AssignOrderToEmployee_WithOrderNotInPendingStatus_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;
        List<String> roles = List.of(DomainMessagesConstants.EMPLOYEE_ROLE);

        RestaurantModel testRestaurant = buildRestaurant(restaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, testRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel inPreparationOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        inPreparationOrder.setStatus(OrderStatusEnum.IN_PREPARATION); // Not PENDING

        when(authenticatedUserPort.getCurrentUserRoles()).thenReturn(roles);
        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(inPreparationOrder));

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.assignOrderToEmployeeAndChangeStatus(orderId));

        assertEquals(DomainMessagesConstants.ORDER_NOT_PENDING, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserRoles();
        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any(OrderModel.class));
    }

    @Test
    void when_MarkOrderAsReady_Expect_Success() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;
        String customerPhoneNumber = "+573001234567";

        RestaurantModel testRestaurant = buildRestaurant(restaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, testRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel inPreparationOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        inPreparationOrder.setStatus(OrderStatusEnum.IN_PREPARATION);

        OrderModel readyOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        readyOrder.setStatus(OrderStatusEnum.READY);
        readyOrder.setSecurityPin("1234");

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(inPreparationOrder));
        when(orderPersistencePort.updateOrder(any(OrderModel.class))).thenReturn(readyOrder);
        when(userServicePort.getUserPhoneNumber(456L)).thenReturn(customerPhoneNumber);

        // Act
        OrderModel result = orderUseCase.markOrderAsReady(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatusEnum.READY, result.getStatus());
        assertNotNull(result.getSecurityPin());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort).updateOrder(any(OrderModel.class));
        verify(userServicePort).getUserPhoneNumber(456L);
        verify(notificationServicePort).sendOrderReadyNotification(eq(orderId), eq(customerPhoneNumber),
                any(String.class));
    }

    @Test
    void when_MarkOrderAsReady_WithEmployeeNotAssociatedWithRestaurant_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(null);

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.markOrderAsReady(orderId));

        assertEquals(DomainMessagesConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort, never()).findOrderById(any());
        verify(orderPersistencePort, never()).updateOrder(any());
        verify(userServicePort, never()).getUserPhoneNumber(any());
        verify(notificationServicePort, never()).sendOrderReadyNotification(any(), any(), any());
    }

    @Test
    void when_MarkOrderAsReady_WithOrderNotFound_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.markOrderAsReady(orderId));

        assertEquals(DomainMessagesConstants.ORDER_NOT_FOUND, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any());
        verify(userServicePort, never()).getUserPhoneNumber(any());
        verify(notificationServicePort, never()).sendOrderReadyNotification(any(), any(), any());
    }

    @Test
    void when_MarkOrderAsReady_WithOrderNotFromEmployeeRestaurant_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long employeeRestaurantId = 1L;
        Long orderRestaurantId = 2L; // Different restaurant

        RestaurantModel orderRestaurant = buildRestaurant(orderRestaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, orderRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel inPreparationOrder = buildSavedOrder(orderId, 456L, orderRestaurant, List.of(orderDish));
        inPreparationOrder.setStatus(OrderStatusEnum.IN_PREPARATION);

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(employeeRestaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(inPreparationOrder));

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.markOrderAsReady(orderId));

        assertEquals(DomainMessagesConstants.ORDER_NOT_FROM_EMPLOYEE_RESTAURANT, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any());
        verify(userServicePort, never()).getUserPhoneNumber(any());
        verify(notificationServicePort, never()).sendOrderReadyNotification(any(), any(), any());
    }

    @Test
    void when_MarkOrderAsReady_WithOrderNotInPreparation_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;

        RestaurantModel testRestaurant = buildRestaurant(restaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, testRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel pendingOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        pendingOrder.setStatus(OrderStatusEnum.PENDING); // Not IN_PREPARATION

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(pendingOrder));

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.markOrderAsReady(orderId));

        assertEquals(DomainMessagesConstants.ORDER_NOT_IN_PREPARATION, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any());
        verify(userServicePort, never()).getUserPhoneNumber(any());
        verify(notificationServicePort, never()).sendOrderReadyNotification(any(), any(), any());
    }

    @Test
    void when_DeliverOrder_Expect_Success() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;
        String enteredPin = "1234";

        RestaurantModel testRestaurant = buildRestaurant(restaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, testRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel readyOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        readyOrder.setStatus(OrderStatusEnum.READY);
        readyOrder.setSecurityPin(enteredPin);

        OrderModel deliveredOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        deliveredOrder.setStatus(OrderStatusEnum.DELIVERED);
        deliveredOrder.setSecurityPin(enteredPin);

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(readyOrder));
        when(orderPersistencePort.updateOrder(any(OrderModel.class))).thenReturn(deliveredOrder);

        // Act
        OrderModel result = orderUseCase.deliverOrder(orderId, enteredPin);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatusEnum.DELIVERED, result.getStatus());
        assertEquals(orderId, result.getId());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort).updateOrder(any(OrderModel.class));
    }

    @Test
    void when_DeliverOrder_WithEmployeeNotAssociatedWithRestaurant_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        String enteredPin = "1234";

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(null);

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.deliverOrder(orderId, enteredPin));

        assertEquals(DomainMessagesConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort, never()).findOrderById(any());
        verify(orderPersistencePort, never()).updateOrder(any());
    }

    @Test
    void when_DeliverOrder_WithOrderNotFound_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;
        String enteredPin = "1234";

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.deliverOrder(orderId, enteredPin));

        assertEquals(DomainMessagesConstants.ORDER_NOT_FOUND, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any());
    }

    @Test
    void when_DeliverOrder_WithOrderNotFromEmployeeRestaurant_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long employeeRestaurantId = 1L;
        Long orderRestaurantId = 2L;
        String enteredPin = "1234";

        RestaurantModel orderRestaurant = buildRestaurant(orderRestaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, orderRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel readyOrder = buildSavedOrder(orderId, 456L, orderRestaurant, List.of(orderDish));
        readyOrder.setStatus(OrderStatusEnum.READY);
        readyOrder.setSecurityPin(enteredPin);

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(employeeRestaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(readyOrder));

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.deliverOrder(orderId, enteredPin));

        assertEquals(DomainMessagesConstants.ORDER_NOT_FROM_EMPLOYEE_RESTAURANT, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any());
    }

    @Test
    void when_DeliverOrder_WithOrderNotReady_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;
        String enteredPin = "1234";

        RestaurantModel testRestaurant = buildRestaurant(restaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, testRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel inPreparationOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        inPreparationOrder.setStatus(OrderStatusEnum.IN_PREPARATION); // Not READY

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(inPreparationOrder));

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.deliverOrder(orderId, enteredPin));

        assertEquals(DomainMessagesConstants.ORDER_NOT_READY, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any());
    }

    @Test
    void when_DeliverOrder_WithInvalidSecurityPin_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;
        String correctPin = "1234";
        String wrongPin = "5678";

        RestaurantModel testRestaurant = buildRestaurant(restaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, testRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel readyOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        readyOrder.setStatus(OrderStatusEnum.READY);
        readyOrder.setSecurityPin(correctPin);

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(readyOrder));

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.deliverOrder(orderId, wrongPin));

        assertEquals(DomainMessagesConstants.INVALID_SECURITY_PIN, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any());
    }

    @Test
    void when_DeliverOrder_WithNullSecurityPin_Expect_CustomOrderException() {
        // Arrange
        Long orderId = 1L;
        Long employeeId = 123L;
        Long restaurantId = 1L;
        String enteredPin = "1234";

        RestaurantModel testRestaurant = buildRestaurant(restaurantId);
        CategoryModel testCategory = buildCategory(1L);
        DishModel testDish = buildDish(1L, testRestaurant, testCategory);
        OrderDishModel orderDish = buildOrderDish(1L, testDish, 2);

        OrderModel readyOrder = buildSavedOrder(orderId, 456L, testRestaurant, List.of(orderDish));
        readyOrder.setStatus(OrderStatusEnum.READY);
        readyOrder.setSecurityPin(null); // No PIN set

        when(authenticatedUserPort.getCurrentUserId()).thenReturn(employeeId);
        when(userServicePort.getUserRestaurantId(employeeId)).thenReturn(restaurantId);
        when(orderPersistencePort.findOrderById(orderId)).thenReturn(Optional.of(readyOrder));

        // Act & Assert
        CustomOrderException exception = assertThrows(CustomOrderException.class,
                () -> orderUseCase.deliverOrder(orderId, enteredPin));

        assertEquals(DomainMessagesConstants.INVALID_SECURITY_PIN, exception.getMessage());

        verify(authenticatedUserPort).getCurrentUserId();
        verify(userServicePort).getUserRestaurantId(employeeId);
        verify(orderPersistencePort).findOrderById(orderId);
        verify(orderPersistencePort, never()).updateOrder(any());
    }
}
