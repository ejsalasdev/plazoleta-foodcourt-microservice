package com.plazoleta.foodcourtmicroservice.domain.model;

import java.util.Objects;

public class OrderDishModel {
    private Long id;
    private OrderModel order;
    private DishModel dish;
    private Integer quantity;

    public OrderDishModel() {
    }

    public OrderDishModel(Long id, OrderModel order, DishModel dish, Integer quantity) {
        this.id = id;
        this.order = order;
        this.dish = dish;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public DishModel getDish() {
        return dish;
    }

    public void setDish(DishModel dish) {
        this.dish = dish;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderDishModel orderDish = (OrderDishModel) o;
        return id != null && id.equals(orderDish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderDishModel{" +
                "id=" + id +
                ", orderId=" + (order != null ? order.getId() : null) +
                ", dishId=" + (dish != null ? dish.getId() : null) +
                ", quantity=" + quantity +
                '}';
    }
}
