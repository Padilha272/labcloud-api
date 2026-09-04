package com.labcloud.labcloud_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String Token;
    private String RefreshToken;
    private String userId;
    private String tenantId;
    private String name;
    private String email;
    private String role;
    private String laboratoryName;

}
