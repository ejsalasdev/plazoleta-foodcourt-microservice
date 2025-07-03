package com.plazoleta.foodcourtmicroservice.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class DishModel {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String urlImage;
    private Long categoryId;
    private RestaurantModel restaurant;
    private Boolean active;

    public DishModel() {
        this.active = true;
    }

    public DishModel(Long id, String name, BigDecimal price, String description, String urlImage, Long categoryId,
            RestaurantModel restaurant, Boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.urlImage = urlImage;
        this.categoryId = categoryId;
        this.restaurant = restaurant;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public RestaurantModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantModel restaurant) {
        this.restaurant = restaurant;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DishModel dish = (DishModel) o;
        return Objects.equals(id, dish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DishModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", categoryId=" + categoryId +
                ", restaurant=" + restaurant +
                ", active=" + active +
                '}';
    }
}
