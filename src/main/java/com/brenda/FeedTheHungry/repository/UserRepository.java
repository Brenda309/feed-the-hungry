package com.brenda.FeedTheHungry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.brenda.FeedTheHungry.appuser.AppUser;
import com.brenda.FeedTheHungry.appuser.AppUserRole;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

        // Add this method to check if any user has the ADMIN role
        @Query("SELECT COUNT(u) > 0 FROM AppUser u JOIN u.role r WHERE r.roleName = 'ADMIN'")
     boolean existsByRoleName(@Param("roleName") AppUserRole roleName);
    
}
