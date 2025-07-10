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
import com.plazoleta.foodcourtmicroservice.domain.ports.out.OrderPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.RestaurantPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;


public class OrderUseCase implements OrderServicePort {

    private final OrderPersistencePort orderPersistencePort;
    private final RestaurantPersistencePort restaurantPersistencePort;
    private final DishPersistencePort dishPersistencePort;
    private final AuthenticatedUserPort authenticatedUserPort;

    public OrderUseCase(OrderPersistencePort orderPersistencePort,
                        RestaurantPersistencePort restaurantPersistencePort,
                        DishPersistencePort dishPersistencePort,
                        AuthenticatedUserPort authenticatedUserPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.authenticatedUserPort = authenticatedUserPort;
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
        // Validate that current user is an employee
        List<String> userRoles = authenticatedUserPort.getCurrentUserRoles();
        if (!userRoles.contains(DomainMessagesConstants.EMPLOYEE_ROLE)) {
            throw new CustomOrderException(DomainMessagesConstants.EMPLOYEE_NOT_AUTHORIZED);
        }

        Long currentUserId = authenticatedUserPort.getCurrentUserId();
        
        // Find the restaurant where the employee works
        // For this implementation, we assume that employees are associated with restaurants 
        // through the ownerId field (employee is the owner/manager of the restaurant)
        Optional<RestaurantModel> employeeRestaurant = restaurantPersistencePort.findRestaurantByOwnerId(currentUserId);
        
        if (employeeRestaurant.isEmpty()) {
            throw new CustomOrderException(DomainMessagesConstants.EMPLOYEE_NOT_ASSOCIATED_WITH_RESTAURANT);
        }

        Long restaurantId = employeeRestaurant.get().getId();
        
        // Validate pagination parameters
        if (page < 0) {
            throw new CustomOrderException(DomainMessagesConstants.PAGINATION_PAGE_NUMBER_INVALID);
        }
        if (size <= 0) {
            throw new CustomOrderException(DomainMessagesConstants.PAGINATION_PAGE_SIZE_INVALID);
        }

        return orderPersistencePort.findOrdersByRestaurantIdAndStatus(restaurantId, status, page, size);
    }
}
