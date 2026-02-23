package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsable de la logique métier de l'endpoint /communityEmail.
 * <p>
 * Ce service permet de récupérer les adresses e-mail de tous les habitants d'une ville donnée.
 */

@Service
public class CommunityEmailService {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailService.class);

    private final PersonRepository personRepository;

    /**
     * Construit le service CommunityEmail
     *
     * @param personRepository repository permettant l'accès aux données des personnes
     */
    public CommunityEmailService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Récupère la liste des adresses e-mail des personnes vivant dans une ville donnée.
     * Si aucune personne n'est trouvée dans la ville indiquée, une liste vide est retournée.
     *
     * @param city ville à analyser
     * @return liste des adresses e-mail des personnes (sans doublons)
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    public List<String> getEmailsByCity(String city) throws IOException {

        logger.debug("Starting email search for city={}", city);

        List<PersonModel> persons = personRepository.findAll();
        logger.debug("Loaded {} persons from repository", persons.size());

        List<String> emails = new ArrayList<>();

        for (PersonModel person : persons) {
            if (person.getCity() != null && person.getCity().equalsIgnoreCase(city)) {
                if (person.getEmail() != null && !emails.contains(person.getEmail())) {
                    emails.add(person.getEmail());
                }
            }
        }
        logger.debug("Found {} unique emails for city={}", emails.size(), city);

        return emails;
    }
}
