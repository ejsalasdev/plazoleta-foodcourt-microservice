package com.plazoleta.foodcourtmicroservice.domain.usecases;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.in.DishServicePort;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.DishPersistencePort;
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

        dishPersistencePort.save(dishModel);
    }}
