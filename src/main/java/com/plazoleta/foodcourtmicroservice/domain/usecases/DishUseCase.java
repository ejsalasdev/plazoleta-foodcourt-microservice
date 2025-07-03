package com.plazoleta.foodcourtmicroservice.domain.usecases;

import java.math.BigDecimal;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementAlreadyExistsException;
import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementNotFoundException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.model.RestaurantModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.DishServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.DishValidatorChain;

public class DishUseCase implements DishServicePort {

    private final DishPersistencePort dishPersistencePort;
    private final DishValidatorChain dishValidatorChain;

    public DishUseCase(DishPersistencePort dishPersistencePort,
            DishValidatorChain dishValidatorChain) {
        this.dishPersistencePort = dishPersistencePort;
        this.dishValidatorChain = dishValidatorChain;
    }

    @Override
    public void save(DishModel dishModel) {
        dishValidatorChain.validate(dishModel);

        Long restaurantId = dishModel.getRestaurant() != null ? dishModel.getRestaurant().getId() : null;
        if (dishPersistencePort.existsByNameAndRestaurantId(dishModel.getName(), restaurantId)) {
            throw new ElementAlreadyExistsException(
                    String.format(DomainMessagesConstants.DISH_ALREADY_EXISTS, dishModel.getName()));
        }

        dishPersistencePort.save(dishModel);
    }

    @Override
    public void updateDish(Long dishId, Long restaurantId, BigDecimal price, String description) {
        if (!dishPersistencePort.existsByIdAndRestaurantId(dishId, restaurantId)) {
            throw new ElementNotFoundException(
                    String.format(DomainMessagesConstants.DISH_NOT_FOUND_IN_RESTAURANT, dishId, restaurantId));
        }

        DishModel dishModel = new DishModel();
        dishModel.setId(dishId);
        dishModel.setPrice(price);
        dishModel.setDescription(description);
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);
        dishModel.setRestaurant(restaurant);

        dishValidatorChain.validate(dishModel);

        dishPersistencePort.updateDish(dishId, restaurantId, price, description);
    }
}
