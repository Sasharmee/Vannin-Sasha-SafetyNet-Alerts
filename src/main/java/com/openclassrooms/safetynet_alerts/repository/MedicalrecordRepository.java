package com.openclassrooms.safetynet_alerts.repository;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Repository
public class MedicalrecordRepository {

    private final ObjectMapper objectMapper;
    public MedicalrecordRepository(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public List<MedicalrecordModel> findAll() throws IOException{

        ClassPathResource resource = new ClassPathResource("data/data.json");

        try (InputStream inputStream = resource.getInputStream()){

            JsonNode rootNode = objectMapper.readTree(inputStream);

            JsonNode medicalRecordNode = rootNode.get("medicalrecords");

            return objectMapper.convertValue(
                    medicalRecordNode, new TypeReference<List<MedicalrecordModel>>() {
                    }
            );
        }
    }

    public void saveAll(List<MedicalrecordModel> medicalrecords) {

    }
}
