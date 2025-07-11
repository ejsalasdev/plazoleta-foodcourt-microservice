package com.plazoleta.foodcourtmicroservice.infrastructure.adapters.persistence;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.plazoleta.foodcourtmicroservice.domain.enums.OrderStatusEnum;
import com.plazoleta.foodcourtmicroservice.domain.model.OrderModel;
import com.plazoleta.foodcourtmicroservice.domain.ports.out.OrderPersistencePort;
import com.plazoleta.foodcourtmicroservice.domain.utils.pagination.PageInfo;
import com.plazoleta.foodcourtmicroservice.infrastructure.entities.OrderEntity;
import com.plazoleta.foodcourtmicroservice.infrastructure.mappers.OrderEntityMapper;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.OrderRepository;
import com.plazoleta.foodcourtmicroservice.infrastructure.repositories.postgres.specifications.OrderSpecifications;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistencePort {

    private final OrderRepository orderRepository;
    private final OrderEntityMapper orderEntityMapper;

    @Override
    public OrderModel saveOrder(OrderModel orderModel) {
        OrderEntity orderEntity = orderEntityMapper.modelToEntityWithDishes(orderModel);
        OrderEntity savedEntity = orderRepository.save(orderEntity);
        return orderEntityMapper.entityToModelWithDishes(savedEntity);
    }

    @Override
    public Optional<OrderModel> findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderEntityMapper::entityToModelWithDishes);
    }

    @Override
    public List<OrderModel> findOrdersByCustomerId(Long customerId) {
        Specification<OrderEntity> specification = OrderSpecifications.customerIdEquals(customerId);
        List<OrderEntity> orderEntities = findOrderEntitiesWithSort(specification);
        return convertEntityListToModelList(orderEntities);
    }

    @Override
    public List<OrderModel> findOrdersByRestaurantId(Long restaurantId) {
        Specification<OrderEntity> specification = OrderSpecifications.restaurantIdEquals(restaurantId);
        List<OrderEntity> orderEntities = findOrderEntitiesWithSort(specification);
        return convertEntityListToModelList(orderEntities);
    }

    @Override
    public boolean hasActiveOrdersForCustomer(Long customerId) {
        Specification<OrderEntity> specification = OrderSpecifications.customerIdEquals(customerId)
                .and(OrderSpecifications
                        .statusIn(Arrays.asList(OrderStatusEnum.PENDING, OrderStatusEnum.IN_PREPARATION)));
        return orderRepository.count(specification) > 0;
    }

    @Override
    public PageInfo<OrderModel> findOrdersByRestaurantIdAndStatus(Long restaurantId, OrderStatusEnum status,
            Integer page, Integer size) {
        Specification<OrderEntity> specification = OrderSpecifications.restaurantIdEquals(restaurantId);

        if (status != null) {
            specification = specification.and(OrderSpecifications.statusEquals(status));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<OrderEntity> orderEntityPage = orderRepository.findAll(specification, pageable);

        List<OrderModel> orderModels = convertEntityListToModelList(orderEntityPage.getContent());

        return new PageInfo<>(
                orderModels,
                orderEntityPage.getTotalElements(),
                orderEntityPage.getTotalPages(),
                orderEntityPage.getNumber(),
                orderEntityPage.getSize(),
                orderEntityPage.hasNext(),
                orderEntityPage.hasPrevious());
    }

    @Override
    public OrderModel updateOrder(OrderModel orderModel) {
        OrderEntity orderEntity = orderEntityMapper.modelToEntityWithDishes(orderModel);
        OrderEntity updatedEntity = orderRepository.save(orderEntity);
        return orderEntityMapper.entityToModelWithDishes(updatedEntity);
    }

    private List<OrderModel> convertEntityListToModelList(List<OrderEntity> orderEntities) {
        return orderEntities.stream()
                .map(orderEntityMapper::entityToModelWithDishes)
                .toList();
    }

    private List<OrderEntity> findOrderEntitiesWithSort(Specification<OrderEntity> specification) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        return orderRepository.findAll(specification, sort);
    }
}
