package com.ecommerce.library.service;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;

public interface AdminService {
    void saveAdmin(AdminDto adminDto);
    Admin findByUsername(String username);
}
