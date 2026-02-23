package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet_alerts.dto.PersonFirestationDTO;
import com.openclassrooms.safetynet_alerts.service.FirestationResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test du {@link FirestationResponseController}
 * <p>
 * Cette classe vérifie le bon fonctionnement de l'endpoint /firestation avec le paramètre stationNumber
 */
@ExtendWith(MockitoExtension.class)
public class FirestationResponseControllerTest {
    /**
     * Service mocké pour isoler le controller
     */
    @Mock
    private FirestationResponseService service;
    /**
     * Controller injecté avec le service mocké
     */
    @InjectMocks
    private FirestationResponseController controller;
    /**
     * Outil permettant de simuler les requêtes HTTP
     */
    private MockMvc mockMvc;

    /**
     * Initialise MockMvc avant chaque test avec le GlobalExceptionHandler configuré
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    /**
     * Vérifie que l'endpoint retourne correctement un {@link FirestationResponseDTO} lorsque les personnes couvertes par la caserne renseignée sont retrouvées
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getPersonsByFirestation_shouldReturnDTO() throws Exception {
        //données du test
        String stationNumber = "1";
        PersonFirestationDTO persons = new PersonFirestationDTO("Samy", "Ymas", "77 Paris", "123-456-789");
        FirestationResponseDTO firestationResponseDTO = new FirestationResponseDTO(List.of(persons), 1, 0);

        when(service.getPersonsCoveredByStation(stationNumber)).thenReturn(firestationResponseDTO);

        //WHEN+THEN: HTTP + assertions
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", stationNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adultCount").value(1))
                .andExpect(jsonPath("$.childCount").value(0))
                .andExpect(jsonPath("$.persons[0].firstName").value("Samy"))
                .andExpect(jsonPath("$.persons[0].lastName").value("Ymas"))
                .andExpect(jsonPath("$.persons[0].address").value("77 Paris"))
                .andExpect(jsonPath("$.persons[0].phone").value("123-456-789"));
    }

    /**
     * Vérifie le comportement de l'endpoint lorsqu'aucune personne n'est couverte par la station renseignée
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getPersonsByFirestation_personsNotFound_shouldReturn200AndEmptyList() throws Exception {
        String stationNumber = "99";
        when(service.getPersonsCoveredByStation(stationNumber)).thenReturn(new FirestationResponseDTO(List.of(), 0, 0));
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", stationNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adultCount").value(0))
                .andExpect(jsonPath("$.childCount").value(0))
                .andExpect(jsonPath("$.persons").isArray())
                .andExpect(jsonPath("$.persons").isEmpty());

    }

    /**
     * Vérifie que l'endpoint retourne une erreur 500 lorsque le service lève une {@link IOException}
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getPersonsByFirestation_serviceThrowsIOException_shouldReturn500() throws Exception {
        //données du test
        String stationNumber = "1";
        PersonFirestationDTO persons = new PersonFirestationDTO("Samy", "Ymas", "77 Paris", "123-456-789");
        FirestationResponseDTO firestationResponseDTO = new FirestationResponseDTO(List.of(persons), 1, 0);

        when(service.getPersonsCoveredByStation(stationNumber)).thenThrow(new IOException("boom"));

        //WHEN+THEN
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", stationNumber))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

}

