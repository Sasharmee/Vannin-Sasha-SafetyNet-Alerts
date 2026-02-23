package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private final PersonService service;

    /**
     * Contrôleur REST exposant l'endpoint /person permettant l'association entre personnes et leurs données personnelles.
     * Il permet de récupérer les associations, en ajouter, en supprimer et modifier les données personnelles des personnes existantes.
     *
     * @param service service où se retrouve la logique métier de l'endpoint CRUD
     */
    public PersonController(PersonService service) {
        this.service = service;

    }

    /**
     * Récupère la liste des associations entre personnes et données personnelles.
     *
     * @return la liste des personnes
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    //GET
    @GetMapping
    public List<PersonModel> getAllPersons() throws IOException {

        logger.info("GET /person called");

        List<PersonModel> result = service.getAllPersons();

        logger.info("GET /person success, {} persons returned", result.size());

        return result;
    }

    /**
     * Ajoute une nouvelle association.
     *
     * @param person objet concernant la personne et ses données personnelles qu'on souhaite ajouter.
     * @return la personne créée
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    //POST
    @PostMapping
    public PersonModel addPerson(@RequestBody PersonModel person) throws IOException {

        logger.info("POST /person firstName={} lastName={}",
                person.getFirstName(), person.getLastName());

        PersonModel result = service.addPerson(person);

        logger.info("POST /person success for {} {}",
                person.getFirstName(), person.getLastName());

        return result;
    }

    /**
     * Mise à jour de données personnelles d'une personne existante.
     *
     * @param person objet concernant l'association à modifier
     * @return la personne mise à jour, ou null si aucune personne correspondante n'est trouvée
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    //PUT
    @PutMapping
    public PersonModel updatePerson(@RequestBody PersonModel person) throws IOException {

        logger.info("PUT /person firstName={} lastName={}",
                person.getFirstName(), person.getLastName());

        PersonModel result = service.updatePerson(person);

        logger.info("PUT /person success for {} {}",
                person.getFirstName(), person.getLastName());

        return result;
    }

    /**
     * Suppression d'une personne.
     * La suppression nécessite obligatoirement le prénom ET le nom.
     *
     * @param firstName prénom à renseigner pour supprimer
     * @param lastName  nom à renseigner pour supprimer
     * @return true si la suppression a eu lieu, sinon false
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    //DELETE
    @DeleteMapping
    public boolean deletePerson(
            @RequestParam String firstName,
            @RequestParam String lastName
    ) throws IOException {
        logger.info("DELETE /person firstName={} lastName={}", firstName, lastName);

        boolean removed = service.deletePerson(firstName, lastName);

        logger.info("DELETE /person success removed={}", removed);

        return removed;
    }
}

