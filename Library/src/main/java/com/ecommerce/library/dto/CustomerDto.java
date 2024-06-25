package com.ecommerce.library.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @Size(min = 3, max = 15, message = "First Name contains 3-15 characters")
    public String firstName;
    @Size(min = 3, max = 15, message = "Last Name contains 3-15 characters")
    public String lastName;
    @Size(min = 3, max = 15, message = "Username contains 3-15 characters")
    public String username;
    @Size(min = 5, max = 15, message = "Password contains 3-15 characters")
    public String password;

    public String confirmPassword;
    public String city;
    private String country;
    private String phoneNumber;
    private String address;
}
