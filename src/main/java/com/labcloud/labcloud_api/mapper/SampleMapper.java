package com.labcloud.labcloud_api.mapper;

import org.springframework.stereotype.Component;

import com.labcloud.labcloud_api.dto.request.SampleRequest;
import com.labcloud.labcloud_api.dto.response.SampleResponse;
import com.labcloud.labcloud_api.models.Sample;

@Component
public class SampleMapper {

    public Sample toEntity(SampleRequest request) {
        if (request == null)
            return null;

        Sample sample = new Sample();
        sample.setName(request.getName());
        sample.setType(request.getType());
        sample.setCollectionDate(request.getCollectionDate());
        sample.setCollectionMethod(request.getCollectionMethod());
        sample.setQuantity(request.getQuantity());
        sample.setUnit(request.getUnit());
        sample.setStorageConditions(request.getStorageConditions());
        sample.setLocation(request.getLocation());
        sample.setNotes(request.getNotes());

        return sample;

    }

    public SampleResponse toResponse(Sample sample) {
        if (sample == null)
            return null;

        return SampleResponse.builder()
                .id(sample.getId())
                .name(sample.getName())
                .type(sample.getType())
                .collectionDate(sample.getCollectionDate())
                .collectionMethod(sample.getCollectionMethod())
                .quantity(sample.getQuantity())
                .unit(sample.getUnit())
                .storageConditions(sample.getStorageConditions())
                .location(sample.getLocation())
                .notes(sample.getNotes())
                .tenantId(sample.getTenantId())
                .createdAt(sample.getCreatedAt())
                .updatedAt(sample.getUpdatedAt())
                .experimentId(sample.getExperiment() != null ? sample.getExperiment().getId() : null)
                .experimentName(sample.getExperiment() != null ? sample.getExperiment().getName() : null)
                .createdBy(sample.getCreatedBy() != null ? sample.getCreatedBy().getId() : null)
                .createdByName(sample.getCreatedBy() != null ? sample.getCreatedBy().getName() : null)
                .totalResults(sample.getResults() != null ? sample.getResults().size() : 0)
                .build();
    }

    public void updateEntity(SampleRequest request, Sample sample) {
        if (request == null || sample == null)
            return;

        if (request.getName() != null) {
            sample.setName(request.getName());
        }
        if (request.getType() != null) {
            sample.setType(request.getType());
        }
        if (request.getCollectionDate() != null) {
            sample.setCollectionDate(request.getCollectionDate());
        }
        if (request.getCollectionMethod() != null) {
            sample.setCollectionMethod(request.getCollectionMethod());
        }
        if (request.getQuantity() != null) {
            sample.setQuantity(request.getQuantity());
        }
        if (request.getUnit() != null) {
            sample.setUnit(request.getUnit());
        }
        if (request.getStorageConditions() != null) {
            sample.setStorageConditions(request.getStorageConditions());
        }
        if (request.getLocation() != null) {
            sample.setLocation(request.getLocation());
        }
        if (request.getNotes() != null) {
            sample.setNotes(request.getNotes());
        }
    }

}
