package com.plazoleta.foodcourtmicroservice.domain.usecases;

import com.plazoleta.foodcourtmicroservice.domain.exceptions.ElementAlreadyExistsException;
import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.DishServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.constants.DomainMessagesConstants;
import com.plazoleta.foodcourtmicroservice.domain.validation.dish.DishValidatorChain;

public class DishUseCase implements DishServicePort {

    private final DishPersistencePort dishPersistencePort;
    private final DishValidatorChain dishValidatorChain;

    public DishUseCase(DishPersistencePort dishPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.dishValidatorChain = new DishValidatorChain();
    }

    @Override
    public void save(DishModel dishModel) {
        dishValidatorChain.validate(dishModel);

        if (dishPersistencePort.existsByNameAndRestaurantId(dishModel.getName(), dishModel.getRestaurantId())) {
            throw new ElementAlreadyExistsException(
                    String.format(DomainMessagesConstants.DISH_ALREADY_EXISTS, dishModel.getName()));
        }

        dishPersistencePort.save(dishModel);
    }
}
