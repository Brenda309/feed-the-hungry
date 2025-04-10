package com.brenda.FeedTheHungry.repository;

import com.brenda.FeedTheHungry.appuser.Role;
import com.brenda.FeedTheHungry.appuser.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppUserRole roleName);
}