package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;

public interface CustomerService {
    Customer findByUsername(String username);

    void saveCustomer(CustomerDto customerDto);

    CustomerDto getCustomer(String username);
    void updateInfo(CustomerDto customerDto);
}
