package com.plazoleta.foodcourtmicroservice.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;

public class OrderModel {
    private Long id;
    private Long customerId;
    private LocalDateTime date;
    private OrderStatusEnum status;
    private Long employeeId;
    private RestaurantModel restaurant;
    private List<OrderDishModel> orderDishes;

    public OrderModel() {
    }

    public OrderModel(Long id, Long customerId, LocalDateTime date, OrderStatusEnum status,
            Long employeeId, RestaurantModel restaurant, List<OrderDishModel> orderDishes) {
        this.id = id;
        this.customerId = customerId;
        this.date = date;
        this.status = status;
        this.employeeId = employeeId;
        this.restaurant = restaurant;
        this.orderDishes = orderDishes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public RestaurantModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantModel restaurant) {
        this.restaurant = restaurant;
    }

    public List<OrderDishModel> getOrderDishes() {
        return orderDishes;
    }

    public void setOrderDishes(List<OrderDishModel> orderDishes) {
        this.orderDishes = orderDishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderModel order = (OrderModel) o;
        return id != null && id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderModel{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", date=" + date +
                ", status=" + status +
                ", employeeId=" + employeeId +
                ", restaurantId=" + (restaurant != null ? restaurant.getId() : null) +
                ", orderDishesCount=" + (orderDishes != null ? orderDishes.size() : 0) +
                '}';
    }
}