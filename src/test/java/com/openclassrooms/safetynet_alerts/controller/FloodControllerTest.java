package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.dto.FloodDTO;
import com.openclassrooms.safetynet_alerts.dto.ResidentInfoDTO;
import com.openclassrooms.safetynet_alerts.service.FloodService;
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
 * Classe de test du {@link FloodController}
 * <p>
 * Cette classe vérifie le bon fonctionnement de l'endpoint /flood
 */
@ExtendWith(MockitoExtension.class)
public class FloodControllerTest {
    /**
     * Service mocké pour isoler le controller
     */
    @Mock
    private FloodService floodService;
    /**
     * Controller injecté avec le service mocké
     */
    @InjectMocks
    private FloodController controller;
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
     * Vérifie que l'endpoint retourne correctement un {@link FloodDTO} lorsque les foyers desservis par les casernes renseignées sont retrouvés
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getFloodByStation_shouldReturnFloodDTO() throws Exception {
        //données du test
        String stations = "1,2";
        ResidentInfoDTO resident = new ResidentInfoDTO("Ymas", "123-456-789", 51, List.of("medication:50mg"), List.of("codeine"));
        FloodDTO floodDTO = new FloodDTO("77 Paris", List.of(resident));

        when(floodService.getFloodByStation(stations)).thenReturn(List.of(floodDTO));

        //WHEN +THEN: Appel HTTP et assertions
        mockMvc.perform(get("/flood/stations")
                        .param("stations", stations)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("77 Paris"))
                .andExpect(jsonPath("$[0].residents[0].lastName").value("Ymas"))
                .andExpect(jsonPath("$[0].residents[0].phone").value("123-456-789"))
                .andExpect(jsonPath("$[0].residents[0].age").value(51))
                .andExpect(jsonPath("$[0].residents[0].medications[0]").value("medication:50mg"))
                .andExpect(jsonPath("$[0].residents[0].allergies[0]").value("codeine"))
        ;

    }

    /**
     * Vérifie que l'endpoint retourne une erreur 400 lorsque le paramètre "stations" est manquant.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getFloodByStations_shouldReturn400_missingStations() throws Exception {
        mockMvc.perform(get("/flood/stations"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Vérifie que l'endpoint retourne une erreur 500 lorsque le service lève une {@link IOException}
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getFloodByStations_serviceThrowsIOException_shouldReturn500() throws Exception {
        String stations = "1,2";
        ResidentInfoDTO resident = new ResidentInfoDTO("Ymas", "123-456-789", 51, List.of("medication:50mg"), List.of("codeine"));
        FloodDTO floodDTO = new FloodDTO("77 Paris", List.of(resident));

        when(floodService.getFloodByStation(stations)).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/flood/stations")
                        .param("stations", stations))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }
}
