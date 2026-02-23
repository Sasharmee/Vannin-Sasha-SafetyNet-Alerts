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

/**
 * Repository permettant l'accès aux données provenant du fichier {@code data.json}.
 * Les données sont lues depuis ce fichier et converties en liste de {@link PersonModel}.
 */

@Repository
public class PersonRepository {

    private final ObjectMapper objectMapper;

    /**
     * Construit ce repository avec un {@link ObjectMapper} injecté par Spring.
     *
     * @param objectMapper mapper Jackson utilisé pour lire le JSON et convertir les nœuds en objets Java
     */
    public PersonRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Récupère toutes les associations personne/données personnelles depuis le fichier JSON
     *
     * <p>
     * Le fichier {@code data/data.json} est lu depuis le classpath, puis le nœud
     * {@code "persons"} est extrait et converti en {@link List} de {@link PersonModel}.
     * </p>
     *
     * @return liste complète des {@link PersonModel} présents dans {@code data/data.json}
     * @throws IOException si le fichier ne peut pas être lu ou si le contenu JSON est invalide
     */
    public List<PersonModel> findAll() throws IOException {

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

    /**
     * Sauvegarde la liste des persons.
     *
     * @param persons liste des {@link PersonModel} à sauvegarder
     */
    public void saveAll(List<PersonModel> persons) {
    }
}
