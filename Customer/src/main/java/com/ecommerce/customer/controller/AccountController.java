package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class AccountController {
    @Autowired
    private CustomerService customerService;
    @GetMapping("/my-account")
    public String accountHome(Principal principal, Model model){
        if (principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        CustomerDto customerDto = customerService.getCustomer(username);
        model.addAttribute("customerDto", customerDto);
        return "my-account";
    }
    @RequestMapping(value = "/update-info", method = RequestMethod.POST)
    public String updateInfo(@Valid @ModelAttribute("customerDto") CustomerDto customerDto, Model model, RedirectAttributes redirectAttributes){

        customerService.updateInfo(customerDto);
        CustomerDto customerDtoUpdated = customerService.getCustomer(customerDto.getUsername());
        model.addAttribute("customerDto", customerDtoUpdated);
        redirectAttributes.addFlashAttribute("success", "Updated successfully");
        return "redirect:/my-account";
    }



}
