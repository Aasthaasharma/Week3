package com.example.E_commerce.service;

import com.example.E_commerce.exception.BadRequestException;
import com.example.E_commerce.exception.ResourceNotFoundException;
import com.example.E_commerce.model.Customer;
import com.example.E_commerce.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }

    //creating a customer
    public Customer registerCustomer(Customer customer) {
        if (customer.getUsername() == null || customer.getUsername().isEmpty()) {
            throw new BadRequestException("Customer name cannot be null or empty");
        }
        customer.setPassword(encoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }


    public void deleteCustomer(int id) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        customerRepository.delete(existingCustomer);
    }

    //to check if user is registered or not and if valid generate token
    public String verify(@Valid Customer customer) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customer.getUsername(), customer.getPassword()));
        if (auth.isAuthenticated())
            return jwtService.generateToken(customer.getUsername());
        return "failed";
    }
}
