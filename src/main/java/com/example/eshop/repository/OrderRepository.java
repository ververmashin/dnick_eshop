package com.example.eshop.repository;

import com.example.eshop.entity.UserEntity;
import com.example.eshop.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    List<OrderEntity> findAllByUser(UserEntity user);
}
