package com.labcloud.labcloud_api.mapper;

import org.springframework.stereotype.Component;

import com.labcloud.labcloud_api.dto.request.LaboratoryRequest;
import com.labcloud.labcloud_api.dto.response.LaboratoryResponse;
import com.labcloud.labcloud_api.models.Laboratory;

@Component
public class LaboratoryMapper {

    // Request -> Entity
    public Laboratory toEntity(LaboratoryRequest request) {
        if (request == null)
            return null;
        Laboratory laboratory = new Laboratory();
        laboratory.setName(request.getName());
        laboratory.setDescription(request.getDescription());
        laboratory.setAddress(request.getAddress());
        laboratory.setPhone(request.getPhone());
        laboratory.setEmail(request.getEmail());

        return laboratory;
    }

    // Entity -> Response
    public LaboratoryResponse toResponse(Laboratory laboratory) {
        if (laboratory == null)
            return null;

        return LaboratoryResponse.builder()
                .id(laboratory.getId())
                .name(laboratory.getName())
                .tenantId(laboratory.getTenantId())
                .description(laboratory.getDescription())
                .address(laboratory.getAddress())
                .phone(laboratory.getPhone())
                .email(laboratory.getEmail())
                .active(laboratory.getActive())
                .createdAt(laboratory.getCreatedAt())
                .updatedAt(laboratory.getUpdatedAt())
                .build();

    }

    public void updateEntity(LaboratoryRequest request, Laboratory laboratory) {
        if (request == null || laboratory == null)
            return;

        if (request.getName() != null) {
            laboratory.setName(request.getName());
        }
        if (request.getDescription() != null) {
            laboratory.setDescription(request.getDescription());
        }
        if (request.getAddress() != null) {
            laboratory.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            laboratory.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            laboratory.setEmail(request.getEmail());
        }
    }
}
