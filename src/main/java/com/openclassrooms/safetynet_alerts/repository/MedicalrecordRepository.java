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

/**
 * Repository permettant l'accès aux données provenant du fichier {@code data.json}.
 * Les données sont lues depuis ce fichier et converties en liste de {@link MedicalrecordModel}.
 */

@Repository
public class MedicalrecordRepository {

    private final ObjectMapper objectMapper;

    /**
     * Construit ce repository avec un {@link ObjectMapper} injecté par Spring.
     *
     * @param objectMapper mapper Jackson utilisé pour lire le JSON et convertir les nœuds en objets Java
     */

    public MedicalrecordRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Récupère toutes les associations personne/données médicales depuis le fichier JSON
     *
     * <p>
     * Le fichier {@code data/data.json} est lu depuis le classpath, puis le nœud
     * {@code "medicalrecords"} est extrait et converti en {@link List} de {@link MedicalrecordModel}.
     * </p>
     *
     * @return liste complète des {@link MedicalrecordModel} présents dans {@code  data/data.json}
     * @throws IOException si le fichier ne peut pas être lu ou si le contenu JSON est invalide
     */

    public List<MedicalrecordModel> findAll() throws IOException {

        ClassPathResource resource = new ClassPathResource("data/data.json");

        try (InputStream inputStream = resource.getInputStream()) {

            JsonNode rootNode = objectMapper.readTree(inputStream);

            JsonNode medicalRecordNode = rootNode.get("medicalrecords");

            return objectMapper.convertValue(
                    medicalRecordNode, new TypeReference<List<MedicalrecordModel>>() {
                    }
            );
        }
    }

    /**
     * Sauvegarde la liste des medicalrecords.
     *
     * @param medicalrecords liste des {@link MedicalrecordModel} à sauvegarder
     */
    public void saveAll(List<MedicalrecordModel> medicalrecords) {

    }
}
