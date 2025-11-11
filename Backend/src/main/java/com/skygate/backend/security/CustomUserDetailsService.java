package com.skygate.backend.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final Map<String, UserDetails> users;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.users = new HashMap<>();
        initializeUsers();
    }

    private void initializeUsers() {
        users.put("admin", User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .authorities(Arrays.asList(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("ROLE_USER")
                ))
                .build());

        users.put("operator", User.builder()
                .username("operator")
                .password(passwordEncoder.encode("operator123"))
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build());

        users.put("monitor", User.builder()
                .username("monitor")
                .password(passwordEncoder.encode("monitor123"))
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_MONITOR")))
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = users.get(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user;
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public Map<String, UserDetails> getAllUsers() {
        return new HashMap<>(users);
    }
}
