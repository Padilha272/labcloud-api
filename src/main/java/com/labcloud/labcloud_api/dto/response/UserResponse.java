package com.labcloud.labcloud_api.dto.response;

import java.time.LocalDateTime;

import com.labcloud.labcloud_api.enums.UserRole;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private UserRole role;
    private Boolean active;
    private String tenantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;

    private String laboratoryId;
    private String laboratoryName;

}
