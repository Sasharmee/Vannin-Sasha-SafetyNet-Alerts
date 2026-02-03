package com.openclassrooms.safetynet_alerts.repository;


import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Repository
public class FirestationRepository {

    private final ObjectMapper objectMapper;
    public FirestationRepository(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public List<FirestationModel> findAll() throws IOException{

        ClassPathResource resource = new ClassPathResource("data/data.json");

        try(InputStream inputStream = resource.getInputStream()){

        JsonNode rootNode = objectMapper.readTree(inputStream);

        JsonNode firestationNode = rootNode.get("firestations");

        return objectMapper.convertValue(
                firestationNode,
                new TypeReference<List<FirestationModel>>() {
                }
        );

        }
    }

    public void saveAll(List<FirestationModel> firestation) {
    }
}
