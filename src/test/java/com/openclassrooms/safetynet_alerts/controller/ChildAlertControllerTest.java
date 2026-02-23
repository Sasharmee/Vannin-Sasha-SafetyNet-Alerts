package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.dto.ChildAlertDTO;
import com.openclassrooms.safetynet_alerts.dto.HouseholdMemberDTO;
import com.openclassrooms.safetynet_alerts.service.ChildAlertService;
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
 * Classe de test du {@link ChildAlertController}
 * <p>
 * Cette classe vérifie le bon fonctionnement de l'endpoint /childAlert
 */
@ExtendWith(MockitoExtension.class)
public class ChildAlertControllerTest {
    /**
     * Service mocké pour isoler le controller
     */
    @Mock
    private ChildAlertService service;
    /**
     * Controller injecté avec le service mocké
     */
    @InjectMocks
    private ChildAlertController controller;
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
     * Vérifie que l'endpoint retourne correctement un {@link ChildAlertDTO} lorsque des enfants sont retrouvés pour une adresse donnée.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getChildrenByAddress_shouldReturnDTO() throws Exception {
        String address = "77 Paris";
        HouseholdMemberDTO householdMemberDTO = new HouseholdMemberDTO("Cons", "Snoc");
        List<ChildAlertDTO> children = List.of(
                new ChildAlertDTO("Samy", "Ymas", 10, List.of(householdMemberDTO))
        );
        when(service.getChildrenByAddress(address)).thenReturn(children);

        mockMvc.perform(get("/childAlert")
                        .param("address", address)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Samy"))
                .andExpect(jsonPath("$[0].lastName").value("Ymas"))
                .andExpect(jsonPath("$[0].age").value(10))
                .andExpect(jsonPath("$[0].householdMembers[0].firstName").value("Cons"))
                .andExpect(jsonPath("$[0].householdMembers[0].lastName").value("Snoc"));
    }

    /**
     * Vérifie que l'endpoint retourne une liste vide lorsque aucun enfant n'est trouvé à l'adresse
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */

    @Test
    void getChildrenByAddress_whenNoChildren_shouldReturnEmpty() throws Exception {
        String address = "77 Paris";
        List<ChildAlertDTO> children = List.of();

        when(service.getChildrenByAddress(address)).thenReturn(children);

        mockMvc.perform(get("/childAlert")
                        .param("address", address)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    /**
     * Vérifie que l'endpoint retourne une erreur 400 quand le paramètre "address" n'est pas renseigné
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */

    @Test
    void getChildrenByAddress_missingAddress_shouldReturn400() throws Exception {
        mockMvc.perform(get("/childAlert"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Vérifie que l'endpoint retourne une erreur 500 lorsque le service lève une {@link IOException}
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */

    @Test
    void getChildrenByAddress_serviceThrowsIOException_shouldReturn500() throws Exception {
        String address = "77 Paris";

        when(service.getChildrenByAddress(address)).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/childAlert").param("address", address))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }
}
