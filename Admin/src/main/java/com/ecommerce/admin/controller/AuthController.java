package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;

@Controller
public class AuthController {

    @Autowired
    private AdminService adminService;


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "index";
    }
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model){
        AdminDto adminDto = new AdminDto();
        model.addAttribute("adminDto", adminDto);
        return "register";
    }
    @RequestMapping(value = "/register-save", method = RequestMethod.POST)
    public String registerNew(@Valid @ModelAttribute("adminDto") AdminDto adminDto, BindingResult result, Model model, RedirectAttributes ra){
        if(result.hasErrors()){
            model.addAttribute("adminDto", adminDto);
            return "register";
        }
        Admin admin = adminService.findByUsername(adminDto.getUsername());
        if(admin != null){
            model.addAttribute("adminDto", adminDto);
            result.rejectValue("username", null,"There is already an account registered with that email");
            return "register";
        }
        if(!adminDto.getPassword().equals(adminDto.getRepeatPassword())){
            model.addAttribute("adminDto", adminDto);
            model.addAttribute("errorPass", "Wrong Password");
            return "register";
        }else{
            adminService.saveAdmin(adminDto);
            model.addAttribute("success", "Register successfully!");
            return "register";
        }
    }
    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public String forgotPassword(){
        return "forgot-password";
    }


}
