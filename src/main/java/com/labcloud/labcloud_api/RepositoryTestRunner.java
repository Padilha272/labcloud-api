package com.labcloud.labcloud_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.labcloud.labcloud_api.repositories.LaboratoryRepository;

@Component
public class RepositoryTestRunner implements CommandLineRunner {

    private final LaboratoryRepository laboratoryRepository;

    public RepositoryTestRunner(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("✅ Repositories criados com sucesso!");
        System.out.println("📊 Total de laboratórios: " + laboratoryRepository.count());
    }
}
