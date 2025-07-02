package com.plazoleta.foodcourtmicroservice.domain.ports.in;

import com.plazoleta.foodcourtmicroservice.domain.model.DishModel;

public interface DishServicePort {
    void save(DishModel dishModel);
    // Agrega aquí otros métodos según necesidades futuras (listar, modificar, etc.)
}
