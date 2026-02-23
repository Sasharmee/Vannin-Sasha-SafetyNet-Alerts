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

/**
 * Service responsable de la logique métier de l'endpoint /childAlert.
 * <p>
 * Ce service permet d'identifier les enfants (≤ 18 ans) vivant à une adresse donnée,
 * ainsi que les autres membres du foyer.
 */

@Service
public class ChildAlertService {

    private static final Logger logger = LoggerFactory.getLogger(ChildAlertService.class);

    private final PersonRepository personRepository;
    private final AgeService ageService;

    /**
     * Construit le service ChildAlert
     *
     * @param personRepository repository permettant d'accéder aux données personnelles des personnes
     * @param ageService       service permettant de calculer l'âge et déterminer si une personne est mineure ou non
     */

    public ChildAlertService(PersonRepository personRepository, AgeService ageService) {
        this.personRepository = personRepository;
        this.ageService = ageService;
    }

    /**
     * Récupère la liste des enfants vivant à une adresse donnée
     *
     * <p>
     * Pour chaque enfant identifié :
     * <ul>
     *     <li>Son âge est calculé.</li>
     *     <li>Les autres membres du foyer sont listés (hors lui-même)</li>
     * </ul>
     * </p>
     *
     * <p>
     * Si aucun enfant n’est trouvé à l’adresse indiquée,
     * une liste vide est retournée.
     * </p>
     *
     * @param address adresse du foyer à analyser
     * @return liste de {@link ChildAlertDTO} correspondant aux enfants trouvés
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */
    public List<ChildAlertDTO> getChildrenByAddress(String address) throws IOException {

        logger.debug("Starting childAlert search for address={}", address);

        List<PersonModel> persons = personRepository.findAll();
        logger.debug("Loaded {} persons from repository", persons.size());

        List<PersonModel> household = new ArrayList<>();
        for (PersonModel person : persons) {
            if (person.getAddress() != null && person.getAddress().equalsIgnoreCase(address)) {
                household.add(person);
            }
        }
        logger.debug("Household size for address={} is {}", address, household.size());

        List<ChildAlertDTO> result = new ArrayList<>();

        for (PersonModel person : household) {
            if (ageService.isChild(person)) {
                int age = ageService.calculateAge(person);
                List<HouseholdMemberDTO> otherMembers = new ArrayList<>();

                for (PersonModel member : household) {
                    boolean samePerson = member.getFirstName().equalsIgnoreCase(person.getFirstName()) && member.getLastName().equalsIgnoreCase(person.getLastName());
                    if (!samePerson) {
                        otherMembers.add(new HouseholdMemberDTO(member.getFirstName(), member.getLastName()));
                    }
                }

                ChildAlertDTO dto = new ChildAlertDTO(
                        person.getFirstName(), person.getLastName(), age, otherMembers
                );

                result.add(dto);
            }
        }
        logger.debug("ChildAlert search completed for address={}, childrenFound={}", address, result.size());


        return result;
    }


}
