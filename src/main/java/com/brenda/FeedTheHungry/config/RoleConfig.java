package com.brenda.FeedTheHungry.config;

import com.brenda.FeedTheHungry.appuser.AppUserRole;
import com.brenda.FeedTheHungry.appuser.Role;
import com.brenda.FeedTheHungry.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleConfig {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            for (AppUserRole roleName : AppUserRole.values()) {
                roleRepository.findByRoleName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(null, roleName)));
            }
        };
    }
}