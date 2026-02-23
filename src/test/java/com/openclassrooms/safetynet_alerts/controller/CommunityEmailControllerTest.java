package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.service.CommunityEmailService;
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
 * Classe de test du {@link CommunityEmailController}
 * <p>
 * Cette classe vérifie le bon fonctionnement de l'endpoint /communityEmail
 */

@ExtendWith(MockitoExtension.class)
public class CommunityEmailControllerTest {

    /**
     * Service mocké pour isoler le controller
     */
    @Mock
    private CommunityEmailService service;

    /**
     * Controller injecté avec le service mocké
     */
    @InjectMocks
    private CommunityEmailController controller;

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
     * Vérifie que l'endpoint retourne correctement une liste d'e-mails lorsque des personnes
     * sont trouvées pour une ville donnée.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getEmailsByCity_shouldReturnListOfEmails() throws Exception {
        //Données du test
        String city = "Paris";
        List<String> emails = List.of("Samy@mail.com", "cons@mail.com");
        when(service.getEmailsByCity(city)).thenReturn(emails);

        //Appel HTTP + assertions
        mockMvc.perform(get("/communityEmail")
                        .param("city", city)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Samy@mail.com"))
                .andExpect(jsonPath("$[1]").value("cons@mail.com"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    /**
     * Vérifie que l'endpoint retourne une erreur 500 lorsque le service lève une {@link IOException}
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getEmailsByCity_serviceThrowsIOException_shouldReturn500() throws Exception {
        //Données du test
        String city = "Paris";
        List<String> emails = List.of("Samy@mail.com", "cons@mail.com");
        when(service.getEmailsByCity(city)).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/communityEmail")
                        .param("city", city))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint retourne une erreur 400 quand le paramètre "city" n'est pas renseigné
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getEmailsByCity_missingCity_shouldReturn400() throws Exception {

        mockMvc.perform(get("/communityEmail"))
                .andExpect(status().isBadRequest());
    }
}
