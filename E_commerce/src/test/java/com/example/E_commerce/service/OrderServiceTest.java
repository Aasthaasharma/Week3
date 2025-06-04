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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer customer;
    private Product product;
    private OrderItem orderItem;
    private Order order;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1);
        customer.setUsername("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPassword("password");

        product = new Product();
        product.setId(1);
        product.setName("Test Product");

        orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        order = new Order(customer, "Pending");
        order.setOrderItems(Arrays.asList(orderItem));
    }

    @Test
    void getAllOrders() {
        orderService.getAllOrders();
        verify(orderRepository).findAll();
    }

    @Test
    void placeOrder() {
        // Setup order item quantity
        orderItem.setQuantity(2);
        order.setOrderItems(Arrays.asList(orderItem));
        // Mocks for customer and product lookup
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        // Mock saving order to return the same order object
        when(orderRepository.save(order)).thenReturn(order);
        Order result = orderService.placeOrder(order);
        assertNotNull(result);
        assertEquals("Pending", result.getStatus());
        verify(orderRepository).save(order);  // Ensure save was called
    }


    @Test
    void getOrderById() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        Order foundOrder = orderService.getOrderById(1);
        assertNotNull(foundOrder);
        assertEquals(order, foundOrder);
    }

    @Test
    void placeOrder_customerNotFound() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder(order));
    }

    @Test
    void placeOrder_productNotFound() {
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder(order));
    }

    @Test
    void placeOrder_invalidQuantity() {
        orderItem.setQuantity(0);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Assertions.assertThrows(BadRequestException.class, () -> orderService.placeOrder(order));
    }


    @Test
    void deleteOrder() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        orderService.deleteOrder(1);
        verify(orderRepository).delete(order);
    }


}