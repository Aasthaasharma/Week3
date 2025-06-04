package com.example.E_commerce.repository;

import com.example.E_commerce.model.Order;
import com.example.E_commerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
}
