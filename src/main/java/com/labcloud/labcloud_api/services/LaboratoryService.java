package com.labcloud.labcloud_api.services;

import org.springframework.stereotype.Service;

import com.labcloud.labcloud_api.repositories.LaboratoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaboratoryService {

    private final LaboratoryRepository laboratoryRepository;

}
