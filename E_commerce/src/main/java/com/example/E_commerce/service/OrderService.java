package com.example.E_commerce.service;

import com.example.E_commerce.exception.BadRequestException;
import com.example.E_commerce.exception.ResourceNotFoundException;
import com.example.E_commerce.model.Customer;
import com.example.E_commerce.model.Order;
import com.example.E_commerce.model.OrderItem;
import com.example.E_commerce.model.Product;
import com.example.E_commerce.repository.CustomerRepository;
import com.example.E_commerce.repository.OrderRepository;
import com.example.E_commerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    @Transactional
    public Order placeOrder(Order order) {
        System.out.println("Processing Order: " + order);
        System.out.println("Customer in Order: " + order.getCustomer());
        // Fetch and set managed Customer
        Customer customer = customerRepository.findById(order.getCustomer().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + order.getCustomer().getId()));
        order.setCustomer(customer);
        for (OrderItem item : order.getOrderItems()) {
            if (item.getQuantity() <= 0) {
                throw new BadRequestException("Order item quantity must be greater than zero for product ID: " + item.getProduct().getId());
            }
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + item.getProduct().getId()));
            item.setProduct(product);
            item.setOrder(order);
        }
        return orderRepository.save(order);
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }

    public void deleteOrder(int id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        orderRepository.delete(existingOrder);
    }

}
