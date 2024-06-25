package com.ecommerce.admin.config;

import com.ecommerce.library.model.Admin;
import com.ecommerce.library.model.Role;
import com.ecommerce.library.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomAdminDetailsService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username);
        if(admin == null){
            throw new UsernameNotFoundException("Couldn't find username");
        }else{
            return new User(admin.getUsername(), admin.getPassword(),
                    mapRolesToAuthorities(admin.getRoles()));
        }
    }

    private Collection < ? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        Collection < ?extends GrantedAuthority> mapRoles =roles.stream()
                .map((role -> new SimpleGrantedAuthority(role.getName())))
                .collect(Collectors.toList());
        return mapRoles;
    }
}
