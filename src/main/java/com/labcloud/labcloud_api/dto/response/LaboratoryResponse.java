package com.labcloud.labcloud_api.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryResponse {

    private String id;
    private String tenantId;
    private String name;
    private String email;
    private String description;
    private String address;
    private String phone;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer totaUsers;
    private Integer totalExperiments;

}
