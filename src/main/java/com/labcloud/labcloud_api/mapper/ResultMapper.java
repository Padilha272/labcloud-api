package com.labcloud.labcloud_api.mapper;

import org.springframework.stereotype.Component;

import com.labcloud.labcloud_api.dto.request.ResultRequest;
import com.labcloud.labcloud_api.dto.response.ResultResponse;
import com.labcloud.labcloud_api.models.Result;

@Component
public class ResultMapper {

    public Result toEntity(ResultRequest request) {

        Result result = new Result();
        result.setParameter(request.getParameter());
        result.setValue(request.getValue());
        result.setUnit(request.getUnit());
        result.setMeasurementDate(request.getMeasurementDate());
        result.setInstrument(request.getInstrument());
        result.setMethod(request.getMethod());
        result.setObservations(request.getObservations());
        result.setIsValid(request.getIsValid() != null ? request.getIsValid() : true);
        result.setQualityControl(request.getQualityControl());

        return result;
    }

    public ResultResponse toResponse(Result result) {
        if (result == null)
            return null;

        return ResultResponse.builder()
                .id(result.getId())
                .parameter(result.getParameter())
                .value(result.getValue())
                .unit(result.getUnit())
                .measurementDate(result.getMeasurementDate())
                .instrument(result.getInstrument())
                .method(result.getMethod())
                .observations(result.getObservations())
                .isValid(result.getIsValid())
                .qualityControl(result.getQualityControl())
                .tenantId(result.getTenantId())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .sampleId(result.getSample() != null ? result.getSample().getId() : null)
                .sampleName(result.getSample() != null ? result.getSample().getName() : null)
                .createdBy(result.getCreatedBy() != null ? result.getCreatedBy().getId() : null)
                .createdByName(result.getCreatedBy() != null ? result.getCreatedBy().getName() : null)
                .build();
    }

    public void updateEntity(ResultRequest request, Result result) {
        if (request == null || result == null)
            return;

        if (request.getParameter() != null) {
            result.setParameter(request.getParameter());
        }
        if (request.getValue() != null) {
            result.setValue(request.getValue());
        }
        if (request.getUnit() != null) {
            result.setUnit(request.getUnit());
        }
        if (request.getMeasurementDate() != null) {
            result.setMeasurementDate(request.getMeasurementDate());
        }
        if (request.getInstrument() != null) {
            result.setInstrument(request.getInstrument());
        }
        if (request.getMethod() != null) {
            result.setMethod(request.getMethod());
        }
        if (request.getObservations() != null) {
            result.setObservations(request.getObservations());
        }
        if (request.getIsValid() != null) {
            result.setIsValid(request.getIsValid());
        }
        if (request.getQualityControl() != null) {
            result.setQualityControl(request.getQualityControl());
        }
    }

}
