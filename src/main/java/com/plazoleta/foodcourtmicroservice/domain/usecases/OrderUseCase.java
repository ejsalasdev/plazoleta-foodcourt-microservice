package com.plazoleta.foodcourtmicroservice.domain.usecases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.CustomOrderException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.CustomerHasActiveOrderException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.OrderDishValidationException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderDishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.OrderServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.AuthenticatedUserPort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.NotificationServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.OrderPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.UserServicePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.SecurityPinGenerator;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;
import com.plazoleta.foodcourtmicroservice.domain.validation.pagination.PaginationValidatorChain;

public class OrderUseCase implements OrderServicePort {

    private final OrderPersistencePort orderPersistencePort;
    private final RestaurantPersistencePort restaurantPersistencePort;
    private final DishPersistencePort dishPersistencePort;
    private final AuthenticatedUserPort authenticatedUserPort;
    private final UserServicePort userServicePort;
    private final NotificationServicePort notificationServicePort;
    private final PaginationValidatorChain paginationValidatorChain;

    public OrderUseCase(OrderPersistencePort orderPersistencePort,
            RestaurantPersistencePort restaurantPersistencePort,
            DishPersistencePort dishPersistencePort,
            AuthenticatedUserPort authenticatedUserPort,
            UserServicePort userServicePort,
            NotificationServicePort notificationServicePort,
            PaginationValidatorChain paginationValidatorChain) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.authenticatedUserPort = authenticatedUserPort;
        this.userServicePort = userServicePort;
        this.notificationServicePort = notificationServicePort;
        this.paginationValidatorChain = paginationValidatorChain;
    }

    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        Long customerId = authenticatedUserPort.getCurrentUserId();

        if (orderPersistencePort.hasActiveOrdersForCustomer(customerId)) {
            throw new CustomerHasActiveOrderException(DomainMessagesConstants.ORDER_CUSTOMER_HAS_ACTIVE_ORDER);
        }

        Optional<RestaurantModel> restaurant = restaurantPersistencePort
                .findRestaurantById(orderModel.getRestaurant().getId());
        if (restaurant.isEmpty()) {
            throw new CustomOrderException(DomainMessagesConstants.RESTAURANT_NOT_FOUND_MESSAGE);
        }

        List<OrderDishModel> validatedOrderDishes = new ArrayList<>();
        for (OrderDishModel orderDish : orderModel.getOrderDishes()) {
            Optional<DishModel> dish = dishPersistencePort.findDishById(orderDish.getDish().getId());
            if (dish.isEmpty()) {
                throw new CustomOrderException(DomainMessagesConstants.DISH_NOT_FOUND_MESSAGE);
            }

            if (!dish.get().getRestaurant().getId().equals(orderModel.getRestaurant().getId())) {
                throw new OrderDishValidationException(DomainMessagesConstants.ORDER_DISH_DIFFERENT_RESTAURANT);
            }

            if (Boolean.FALSE.equals(dish.get().getActive())) {
                throw new OrderDishValidationException(DomainMessagesConstants.ORDER_DISH_NOT_ACTIVE);
            }

            OrderDishModel validatedOrderDish = new OrderDishModel();
            validatedOrderDish.setDish(dish.get());
            validatedOrderDish.setQuantity(orderDish.getQuantity());
            validatedOrderDish.setOrder(orderModel);
            validatedOrderDishes.add(validatedOrderDish);
        }

        orderModel.setCustomerId(customerId);
        orderModel.setDate(LocalDateTime.now());
        orderModel.setStatus(OrderStatusEnum.PENDING);
        orderModel.setEmployeeId(null);
        orderModel.setRestaurant(restaurant.get());
        orderModel.setOrderDishes(validatedOrderDishes);

        return orderPersistencePort.saveOrder(orderModel);
    }

    @Override
    public PageInfo<OrderModel> getOrdersByRestaurantAndStatus(OrderStatusEnum status, Integer page, Integer size) {
        paginationValidatorChain.validate(page, size);

        List<String> userRoles = authenticatedUserPort.getCurrentUserRoles();
        if (!userRoles.contains(DomainMessagesConstants.EMPLOYEE_ROLE)) {
            throw new CustomOrderException(DomainMessagesConstants.EMPLOYEE_NOT_AUTHORIZED);
        }

        Long currentUserId = authenticatedUserPort.getCurrentUserId();

        Long restaurantId = userServicePort.getUserRestaurantId(currentUserId);

        if (restaurantId == null) {
            throw new CustomOrderException(DomainMessagesConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT);
        }

        return orderPersistencePort.findOrdersByRestaurantIdAndStatus(restaurantId, status, page, size);
    }

    @Override
    public OrderModel assignOrderToEmployeeAndChangeStatus(Long orderId) {

        List<String> userRoles = authenticatedUserPort.getCurrentUserRoles();
        if (!userRoles.contains(DomainMessagesConstants.EMPLOYEE_ROLE)) {
            throw new CustomOrderException(DomainMessagesConstants.EMPLOYEE_NOT_AUTHORIZED);
        }

        Long currentEmployeeId = authenticatedUserPort.getCurrentUserId();

        Long employeeRestaurantId = userServicePort.getUserRestaurantId(currentEmployeeId);
        if (employeeRestaurantId == null) {
            throw new CustomOrderException(DomainMessagesConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT);
        }

        Optional<OrderModel> orderOptional = orderPersistencePort.findOrderById(orderId);
        if (orderOptional.isEmpty()) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_FOUND);
        }

        OrderModel order = orderOptional.get();

        if (!order.getRestaurant().getId().equals(employeeRestaurantId)) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_FROM_EMPLOYEE_RESTAURANT);
        }

        if (order.getStatus() != OrderStatusEnum.PENDING) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_PENDING);
        }

        order.setEmployeeId(currentEmployeeId);
        order.setStatus(OrderStatusEnum.IN_PREPARATION);

        return orderPersistencePort.updateOrder(order);
    }

    @Override
    public OrderModel markOrderAsReady(Long orderId) {
        Long currentEmployeeId = authenticatedUserPort.getCurrentUserId();
        Long employeeRestaurantId = userServicePort.getUserRestaurantId(currentEmployeeId);

        if (employeeRestaurantId == null) {
            throw new CustomOrderException(DomainMessagesConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT);
        }

        Optional<OrderModel> orderOptional = orderPersistencePort.findOrderById(orderId);
        if (orderOptional.isEmpty()) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_FOUND);
        }

        OrderModel order = orderOptional.get();

        if (!order.getRestaurant().getId().equals(employeeRestaurantId)) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_FROM_EMPLOYEE_RESTAURANT);
        }

        if (order.getStatus() != OrderStatusEnum.IN_PREPARATION) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_IN_PREPARATION);
        }

        String securityPin = SecurityPinGenerator.generateSecurityPin();
        order.setSecurityPin(securityPin);
        order.setStatus(OrderStatusEnum.READY);

        OrderModel updatedOrder = orderPersistencePort.updateOrder(order);

        String customerPhoneNumber = userServicePort.getUserPhoneNumber(order.getCustomerId());
        notificationServicePort.sendOrderReadyNotification(order.getId(), customerPhoneNumber, securityPin);

        return updatedOrder;
    }

    @Override
    public OrderModel deliverOrder(Long orderId, String enteredSecurityPin) {
        Long currentEmployeeId = authenticatedUserPort.getCurrentUserId();
        Long employeeRestaurantId = userServicePort.getUserRestaurantId(currentEmployeeId);

        if (employeeRestaurantId == null) {
            throw new CustomOrderException(DomainMessagesConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT);
        }

        Optional<OrderModel> orderOptional = orderPersistencePort.findOrderById(orderId);
        if (orderOptional.isEmpty()) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_FOUND);
        }

        OrderModel order = orderOptional.get();

        if (!order.getRestaurant().getId().equals(employeeRestaurantId)) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_FROM_EMPLOYEE_RESTAURANT);
        }

        if (order.getStatus() != OrderStatusEnum.READY) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_READY);
        }

        if (order.getSecurityPin() == null || !order.getSecurityPin().equals(enteredSecurityPin)) {
            throw new CustomOrderException(DomainMessagesConstants.INVALID_SECURITY_PIN);
        }

        order.setStatus(OrderStatusEnum.DELIVERED);

        return orderPersistencePort.updateOrder(order);
    }

    @Override
    public OrderModel cancelOrder(Long orderId) {
        Long currentCustomerId = authenticatedUserPort.getCurrentUserId();

        Optional<OrderModel> orderOptional = orderPersistencePort.findOrderById(orderId);
        if (orderOptional.isEmpty()) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_FOUND);
        }

        OrderModel order = orderOptional.get();

        if (!order.getCustomerId().equals(currentCustomerId)) {
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_FROM_CUSTOMER);
        }

        if (order.getStatus() != OrderStatusEnum.PENDING) {
            String customerPhoneNumber = userServicePort.getUserPhoneNumber(order.getCustomerId());
            notificationServicePort.sendOrderCancelledNotification(order.getId(), customerPhoneNumber);
            throw new CustomOrderException(DomainMessagesConstants.ORDER_NOT_PENDING_FOR_CANCELLATION);
        }

        order.setStatus(OrderStatusEnum.CANCELLED);
        return orderPersistencePort.updateOrder(order);
    }
}
