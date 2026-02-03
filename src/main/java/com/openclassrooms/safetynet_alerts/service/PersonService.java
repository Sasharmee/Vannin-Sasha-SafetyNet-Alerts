package com.openclassrooms.safetynet_alerts.service;

import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PersonService {

    //On va utiliser PersonRepository pour avoir les données récupérées par ce dernier
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    //GETALL
    public List<PersonModel> getAllPersons() throws IOException {
        List<PersonModel> persons = personRepository.findAll();
        return persons;
    }
    //ADD
    public PersonModel addPerson(PersonModel person) throws IOException{
        List<PersonModel> persons = personRepository.findAll();
//on obeserve si y'a match entre p (existe) et person(ajouté)
        boolean exists = persons.stream().anyMatch(p->p.getFirstName().equals(person.getFirstName())&& p.getLastName().equals(person.getLastName()));
        if (exists){
            throw new IllegalArgumentException("Person already exists");
        }
        persons.add(person);
        personRepository.saveAll(persons);
        return person;
    }

    //UPDATE
    public PersonModel updatePerson(PersonModel person) throws IOException{
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
                return p;
            }
        }
        return null;
    }

    //DELETE
    public boolean deletePerson(String firstName, String lastName) throws IOException {
        List<PersonModel> persons = personRepository.findAll();
//on récupère toutes les personnes du repository et on conserve dans une List
        boolean removed = persons.removeIf(p->p.getFirstName().equals(firstName)&&p.getLastName().equals(lastName));

        if(removed){
            personRepository.saveAll(persons);
        }
        return removed;
    }
}