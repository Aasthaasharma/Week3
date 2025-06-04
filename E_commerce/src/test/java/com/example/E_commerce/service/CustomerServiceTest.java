package com.example.E_commerce.service;

import com.example.E_commerce.exception.BadRequestException;
import com.example.E_commerce.model.Customer;
import com.example.E_commerce.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1);
        customer.setUsername("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPassword("password");
    }

    @Test
    void getAllCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAll()).thenReturn(customers);
        List<Customer> result = customerService.getAllCustomers();
        assertEquals(1, result.size());
        assertEquals(customer, result.get(0));
    }

    @Test
    void getCustomerById() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        Customer found = customerService.getCustomerById(1);
        assertNotNull(found);
        assertEquals(customer, found);
    }

    @Test
    void registerCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer saved = customerService.registerCustomer(customer);
        assertNotNull(saved);
        assertEquals(customer, saved);
    }


    @Test
    void registerCustomer_nullName() {
        customer.setUsername(null);
        assertThrows(BadRequestException.class, () -> customerService.registerCustomer(customer));
    }

    @Test
    void registerCustomer_emptyName() {
        customer.setUsername("");
        assertThrows(BadRequestException.class, () -> customerService.registerCustomer(customer));
    }


    @Test
    void deleteCustomer() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);
        customerService.deleteCustomer(1);
        verify(customerRepository).delete(customer);
    }
}