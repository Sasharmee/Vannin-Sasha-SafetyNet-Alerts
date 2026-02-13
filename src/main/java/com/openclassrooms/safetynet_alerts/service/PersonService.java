package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    //On va utiliser PersonRepository pour avoir les données récupérées par ce dernier
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    //GETALL
    public List<PersonModel> getAllPersons() throws IOException {
        logger.debug("Fetching all persons");

        List<PersonModel> persons = personRepository.findAll();

        logger.debug("Found {} persons", persons.size());

        return persons;
    }
    //ADD
    public PersonModel addPerson(PersonModel person) throws IOException{

        logger.debug("Adding person {} {}", person.getFirstName(), person.getLastName());

        List<PersonModel> persons = personRepository.findAll();

//on obeserve si y'a match entre p (existe) et person(ajouté)
        boolean exists = persons.stream().anyMatch(p->p.getFirstName().equals(person.getFirstName())&& p.getLastName().equals(person.getLastName()));

        if (exists){
            logger.debug("Person already exists {} {}", person.getFirstName(), person.getLastName());
            throw new IllegalArgumentException("Person already exists");
        }
        persons.add(person);
        personRepository.saveAll(persons);

        logger.debug("Person added successfully {} {}", person.getFirstName(), person.getLastName());

        return person;
    }

    //UPDATE
    public PersonModel updatePerson(PersonModel person) throws IOException{

        logger.debug("Updating person {} {}", person.getFirstName(), person.getLastName());

        List<PersonModel> persons = personRepository.findAll();

        for (PersonModel p: persons){
            if (p.getFirstName().equals(person.getFirstName())&& p.getLastName().equals(person.getLastName())){
                p.setAddress(person.getAddress());
                p.setCity(person.getCity());
                p.setZip(person.getZip());
                p.setPhone(person.getPhone());
                p.setEmail(person.getEmail());

//p = personne existante, donc on modifie les champs existants
                personRepository.saveAll(persons);

                logger.debug("Person updated successfully {} {}", person.getFirstName(), person.getLastName());

                return p;
            }
        }

        logger.debug("No person found to update {} {}", person.getFirstName(), person.getLastName());

        return null;
    }

    //DELETE
    public boolean deletePerson(String firstName, String lastName) throws IOException {

        logger.debug("Deleting person {} {}", firstName, lastName);

        List<PersonModel> persons = personRepository.findAll();
//on récupère toutes les personnes du repository et on conserve dans une List
        boolean removed = persons.removeIf(p->p.getFirstName().equals(firstName)&&p.getLastName().equals(lastName));

        if(removed){
            logger.debug("Person deleted successfully {} {}", firstName, lastName);
            personRepository.saveAll(persons);
        }else {
            logger.debug("No person found to delete {} {}", firstName, lastName);
        }

        return removed;
    }
}