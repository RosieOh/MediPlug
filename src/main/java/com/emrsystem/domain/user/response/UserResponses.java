package com.emrsystem.domain.user.response;

import com.emrsystem.domain.user.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

public class UserResponses {

    @Getter
    @AllArgsConstructor
    public static class UserSummary {
        private Long id;
        private String username;
        private String name;
        private String email;
        private String phone;
        private Set<String> roles;
        private boolean active;
        private boolean locked;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static UserSummary from(UserAccount userAccount) {
            return new UserSummary(
                    userAccount.getId(),
                    userAccount.getUsername(),
                    userAccount.getName(),
                    userAccount.getEmail(),
                    userAccount.getPhone(),
                    userAccount.getRoles(),
                    userAccount.isActive(),
                    userAccount.isLocked(),
                    userAccount.getCreatedAt(),
                    userAccount.getUpdatedAt()
            );
        }
    }
}

