package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private CustomerService customerService;
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        CustomerDto customerDto = new CustomerDto();
        model.addAttribute("customerDto", customerDto);
        return "signup";
    }

    @PostMapping("/do-register")
    public String registerNew(@Valid @ModelAttribute("customerDto") CustomerDto customerDto, Model model, BindingResult result){
        if(result.hasErrors()){
            model.addAttribute("customerDto", customerDto);
            return "signup";
        }
        Customer customer = customerService.findByUsername(customerDto.getUsername());
        if(customer != null){
            model.addAttribute("customerDto", customerDto);
            result.rejectValue("username", "", "username existed");
            return "signup";
        }else if(!customerDto.getPassword().equals(customerDto.getConfirmPassword())){
            model.addAttribute("customerDto", customerDto);
            result.rejectValue("confirmPassword",null, "Wrong Password");
            return "signup";
        }else{
            model.addAttribute("success", "Sign up successfully");
            customerService.saveCustomer(customerDto);
            return "signup";
        }
    }
}
