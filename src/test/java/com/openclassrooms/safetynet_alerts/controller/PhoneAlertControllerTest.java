package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.service.PhoneAlertService;
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
 * Classe de test du {@link PhoneAlertController}
 * <p>
 * Cette classe vérifie le bon fonctionnement de l'endpoint /phoneAlert
 */
@ExtendWith(MockitoExtension.class)
public class PhoneAlertControllerTest {
    /**
     * Service mocké pour isoler le controller
     */
    @Mock
    private PhoneAlertService service;
    /**
     * Controller injecté avec le service mocké
     */
    @InjectMocks
    private PhoneAlertController controller;
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
     * Vérifie que l'endpoint retourne correctement une liste de numéros de téléphone des résidents desservis par la station renseignée
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getPhoneByStation_shouldReturnListOfPhone() throws Exception {
        //données du test
        String firestation = "1";
        List<String> phones = List.of("123-456-789", "111-222-333");
        when(service.getPhoneByStation(firestation)).thenReturn(phones);

        //WHEN+THEN: HTTP et assertions
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", firestation)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("123-456-789"))
                .andExpect(jsonPath("$[1]").value("111-222-333"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    /**
     * Vérifie que l'endpoint retourne une erreur 500 lorsque le service lève une {@link IOException}
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getPhoneByStation_serviceThrowsIOException_shouldReturn500() throws Exception {
        //données du test
        String firestation = "1";
        List<String> phones = List.of("123-456-789", "111-222-333");
        when(service.getPhoneByStation(firestation)).thenThrow(new IOException("boom"));

        //WHEN+THEN
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", firestation))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint retourne une erreur 400 quand le paramètre "firestation" n'est pas renseigné
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getPhoneByStation_missingStation_shouldReturn400() throws Exception {

        mockMvc.perform(get("/phoneAlert"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Vérifie que l'endpoint retourne une liste vide lorsqe aucune personne n'est couverte par la station renseignée
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getPhoneByStation_whenNoPhoneFoundByStation_shouldReturnEmptyList() throws Exception {
        //données du test
        String firestation = "44";
        when(service.getPhoneByStation(firestation)).thenReturn(List.of());

        //WHEN+THEN
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", firestation)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(content().json("[]"));


    }
}
