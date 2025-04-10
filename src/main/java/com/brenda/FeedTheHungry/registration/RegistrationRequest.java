package com.brenda.FeedTheHungry.registration;

import com.brenda.FeedTheHungry.appuser.AppUserRole;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String password;
         private AppUserRole role;
         private final String phoneOrLocation;
}
