package com.openclassrooms.safetynet_alerts.service;


import com.openclassrooms.safetynet_alerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynet_alerts.dto.HouseholdMemberDTO;
import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChildAlertService {

    private static final Logger logger = LoggerFactory.getLogger(ChildAlertService.class);

    private final PersonRepository personRepository;
    private final AgeService ageService;

    public ChildAlertService(PersonRepository personRepository, AgeService ageService){
        this.personRepository = personRepository;
        this.ageService = ageService;
    }

    public List<ChildAlertDTO> getChildrenByAddress(String address) throws IOException {

        logger.debug("Starting childAlert search for address={}", address);

        //Récuperer les persons via respository
        List<PersonModel> persons = personRepository.findAll();
        logger.debug("Loaded {} persons from repository", persons.size());

        //Garder persons de l'address renseignée
        List<PersonModel> household = new ArrayList<>();
        for (PersonModel person: persons){
            if (person.getAddress()!=null&& person.getAddress().equalsIgnoreCase(address)){household.add(person);
            }
        }
        logger.debug("Household size for address={} is {}", address, household.size());

        //Preparation list DTO
        List<ChildAlertDTO> result = new ArrayList<>();

        //Parcourir persons du household
        for (PersonModel person: household){
            //si enfant
            if (ageService.isChild(person)){
                //calcul age
                int age = ageService.calculateAge(person);
                //création list othermembers
                List<HouseholdMemberDTO> otherMembers = new ArrayList<>();

                for (PersonModel member : household){
                    //vérification si member = person
                    boolean samePerson = member.getFirstName().equalsIgnoreCase(person.getFirstName())&&member.getLastName().equalsIgnoreCase(person.getLastName());
                    //si person différent member alors member autre membre du foyer
                    if (!samePerson){
                        otherMembers.add(new HouseholdMemberDTO(member.getFirstName(), member.getLastName()));
                    }
                }
                //Construction DTO pour l'enfant
                ChildAlertDTO dto = new ChildAlertDTO(
                        person.getFirstName(), person.getLastName(), age, otherMembers
                );

                //ajoute du dto à la liste result du début de la boucle
                result.add(dto);
            }
        }
        logger.debug("ChildAlert search completed for address={}, childrenFound={}", address, result.size());


        //si aucun enfant result = vide et controller renvoie " "
        return result;
    }


}
