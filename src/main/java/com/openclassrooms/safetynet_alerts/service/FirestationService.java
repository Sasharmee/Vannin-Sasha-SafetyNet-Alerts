package com.openclassrooms.safetynet_alerts.service;



import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import com.openclassrooms.safetynet_alerts.repository.FirestationRepository;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Service responsable de la logique métier liée à l'endpoint CRUD /firestation.
 *
 * Ce service permet de récupérer les associations, d'ajouter une association adresse/caserne,
 * de mettre à jour une association existante et de supprimer une correspondance.
 */

@Service
public class FirestationService {

    private static final Logger logger = LoggerFactory.getLogger(FirestationService.class);

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final AgeService ageService;

    /**
     * Construit le service Firestation
     *
     * @param firestationRepository repository permettant d'accéder aux données des casernes
     * @param personRepository repository permettant d'accéder aux données des personnes
     * @param ageService service permettant de calculer l'âge et de déterminer si une personne est majeure ou mineure
     */
    public FirestationService(FirestationRepository firestationRepository, PersonRepository personRepository, AgeService ageService){
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.ageService = ageService;
    }

    /**
     * Récupère la liste complète des correspondances
     *
     * @param firestation paramètre non utilisé mais présent pour compatibilité controller
     * @return liste de toutes les casernes
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //GET
    public List<FirestationModel> getAllFirestation(FirestationModel firestation) throws IOException{
        logger.debug("Fetching all firestations");

        List<FirestationModel> firestations = firestationRepository.findAll();

        logger.debug("Found {} firestations", firestations.size());

        return firestations;
    }

    /**
     * Ajoute une nouvelle association adresse/caserne
     *
     * @param firestation objet contenant l'adresse et le numéro de station
     * @return liste mise à jour des casernes
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //ADD
    public List<FirestationModel> addFirestation(FirestationModel firestation) throws IOException{
        logger.debug("Adding firestation for address={} station={}",
                firestation.getAddress(), firestation.getStation());

        List<FirestationModel> firestations = firestationRepository.findAll();
        boolean exists = firestations.stream().anyMatch(f->f.getAddress().equals(firestation.getAddress()));
        if (exists){
            logger.debug("Firestation already exists for address={}", firestation.getAddress());
            throw new IllegalArgumentException("address already covered by a station");
        }
        firestations.add(firestation);
        firestationRepository.saveAll(firestations);

        logger.debug("Firestation added successfully, total firestations={}", firestations.size());

        return firestations;
    }

    /**
     * Met à jour le numéro de caserne associé à une adresse existante
     *
     * @param firestation objet contenant l'adresse cible et le nouveau numéro de station
     * @return liste mise à jour des casernes ou {@code null} si aucune adresse correspondante n'existe
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //UPDATE
    public List<FirestationModel> updateFirestation(FirestationModel firestation) throws IOException{
        logger.debug("Updating firestation for address={} newStation={}",
                firestation.getAddress(), firestation.getStation());


        List<FirestationModel> firestations = firestationRepository.findAll();

        for (FirestationModel f: firestations){
            if (f.getAddress().equals(firestation.getAddress())){
                f.setStation(firestation.getStation());
                firestationRepository.saveAll(firestations);

                logger.debug("Firestation updated for address={}", firestation.getAddress());
                return firestations;
            }
        }
        logger.debug("No firestation found for address={}", firestation.getAddress());
        return null;
    }

    /**
     * Supprime une association selon l'adresse ou le numéro de la caserne
     *
     * @param address adresse à supprimer
     * @param station numéro de station à supprimer
     * @return {@code true} si la suppression a eu lieu, sinon {@code false}
     * @throws IOException en cas d'erreur lors de l'accès aux données
     */

    //DELETE
    public boolean deleteFirestation(String address, String station) throws IOException{

        logger.debug("Deleting firestation with address={} or station={}", address, station);

        List<FirestationModel> firestations = firestationRepository.findAll();

        boolean removed = firestations.removeIf(f ->
                (address != null && f.getAddress().equals(address)) ||
                        (station != null && f.getStation().equals(station)) //attention
        );

        if (removed){
            logger.debug("Firestation deleted successfully");
            firestationRepository.saveAll(firestations);
        }else {
            logger.debug("No firestation matched deletion criteria");
        }
        return removed;
    }

}
