package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynet_alerts.service.PersonInfoService;
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
 * Classe de test du {@link PersonInfoController}
 * <p>
 * Cette classe vérifie le bon fonctionnement de l'endpoint /personInfoLastName avec comme paramètre lastName
 */
@ExtendWith(MockitoExtension.class)
public class PersonInfoControllerTest {
    /**
     * Service mocké pour isoler le controller
     */
    @Mock
    private PersonInfoService service;
    /**
     * Controller injecté avec le service mocké
     */
    @InjectMocks
    private PersonInfoController controller;
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
     * Vérifie que l'endpoint retourne correctement un {@link PersonInfoDTO} lorsque les informations de la personne portant le nom renseigné sont retrouvées
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getPersonInfoByLastName_shouldReturnInfos() throws Exception {
        //données du test
        String lastName = "Ymas";
        PersonInfoDTO personInfoDTO = new PersonInfoDTO(
                "Ymas",
                "77 Paris",
                51,
                "Samy@mail.com",
                List.of("medications:50mg"),
                List.of("codeine")
        );
        when(service.getPersonInfoByLastName(lastName)).thenReturn(List.of(personInfoDTO));

        //WHEN + THEN: Appel HTTP + assertions
        mockMvc.perform(get("/personInfolastName={lastName}", lastName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Ymas"))
                .andExpect(jsonPath("$[0].address").value("77 Paris"))
                .andExpect(jsonPath("$[0].age").value(51))
                .andExpect(jsonPath("$[0].email").value("Samy@mail.com"))
                .andExpect(jsonPath("$[0].medications[0]").value("medications:50mg"))
                .andExpect(jsonPath("$[0].allergies[0]").value("codeine"));
    }

    /**
     * Vérifie le comportement de l'endpoint lorsqu'aucune personne n'a le nom renseigné
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getPersonInfoByLastName_shouldReturn200AndEmptyList_personNotFound() throws Exception {
        String lastName = "XYZ";
        when(service.getPersonInfoByLastName(lastName)).thenReturn(List.of());

        //WHEN + THEN
        mockMvc.perform(get("/personInfolastName={lastName}", lastName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    /**
     * Vérifie que l'endpoint retourne une erreur 500 lorsque le service lève une {@link IOException}
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getPersonInfoByLastName_shouldReturn500_serviceThrowsIOException() throws Exception {
        //données du test
        String lastName = "Ymas";
        PersonInfoDTO personInfoDTO = new PersonInfoDTO(
                "Ymas",
                "77 Paris",
                51,
                "Samy@mail.com",
                List.of("medications:50mg"),
                List.of("codeine")
        );
        when(service.getPersonInfoByLastName(lastName)).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/personInfolastName={lastName}", lastName))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));

    }
}
