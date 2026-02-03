package com.openclassrooms.safetynet_alerts.repository;


import com.openclassrooms.safetynet_alerts.model.PersonModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Repository
public class PersonRepository {

    private final ObjectMapper objectMapper;
    public PersonRepository(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public List<PersonModel> findAll() throws IOException{

        ClassPathResource resource = new ClassPathResource("data/data.json");

        try (InputStream inputStream = resource.getInputStream()) {

            //On lit tout le fichier JSON, JsonNode = représentation Java de l'arbre Json
            JsonNode rootNode = objectMapper.readTree(inputStream);

            //on récupére uniquement la clé "persons"
            JsonNode personsNode = rootNode.get("persons");

            //on convertit "persons" en List<PersonModel>
            return objectMapper.convertValue(
                    personsNode,
                    new TypeReference<List<PersonModel>>() {
                    }
            );
        }


    }

    public void saveAll(List<PersonModel> persons) {
    }
}
