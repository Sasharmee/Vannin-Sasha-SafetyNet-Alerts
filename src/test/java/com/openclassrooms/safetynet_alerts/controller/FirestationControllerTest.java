package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.model.FirestationModel;
import com.openclassrooms.safetynet_alerts.service.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test du {@link FirestationController}
 * <p>
 * Cette classe vérifie le bon fonctionnement des endpoints CRUD exposés par l'URL /firestation
 */
@ExtendWith(MockitoExtension.class)
public class FirestationControllerTest {
    /**
     * Service mocké pour isoler le controller
     */
    @Mock
    private FirestationService service;
    /**
     * Controller injecté avec le service mocké
     */
    @InjectMocks
    private FirestationController controller;
    /**
     * Outil permettant de simuler les requêtes HTTP
     */
    private MockMvc mockMvc;
    /**
     * Outil permettant de convertir un modèle en chaine JSON
     */
    private ObjectMapper mapper;

    /**
     * Initialise l'environnement de test avant chaque exécution.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    /**
     * Vérifie que l'endpoint GET /firestation retourne la liste complète des casernes lorsque le service renvoie des données valides.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void getAllFirestation_shouldReturnListOfFirestations() throws Exception {
        FirestationModel f1 = new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        FirestationModel f2 = new FirestationModel();
        f2.setAddress("88 Lyon");
        f2.setStation("2");

        List<FirestationModel> firestations = List.of(f1, f2);

        when(service.getAllFirestation(any(FirestationModel.class))).thenReturn(firestations);

        mockMvc.perform(get("/firestation")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].station").value("1"))
                .andExpect(jsonPath("$[0].address").value("77 Paris"));
    }

    /**
     * Vérifie que l'endpoint GET /firestation retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */

    @Test
    void getAllFirestation_serviceThrowsIOException_shouldReturn500() throws Exception {
        when(service.getAllFirestation(any(FirestationModel.class))).thenThrow(new IOException());

        mockMvc.perform(get("/firestation"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint POST /firestation ajoute correctement une nouvelle caserne.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void addFirestation_shouldReturnCreatedFirestation() throws Exception {
        FirestationModel f1 = new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        when(service.addFirestation(any(FirestationModel.class))).thenReturn(List.of(f1));

        String json = mapper.writeValueAsString(f1);

        mockMvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("77 Paris"))
                .andExpect(jsonPath("$[0].station").value("1"));
    }

    /**
     * Vérifie que l'endpoint POST /firestation retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */

    @Test
    void addFirestation_serviceThrowsIOException_shouldReturn500() throws Exception {
        FirestationModel f1 = new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        when(service.addFirestation(any(FirestationModel.class))).thenThrow(new IOException("boom"));

        String json = mapper.writeValueAsString(f1);

        mockMvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint PUT /firestation met à jour correctement une caserne existante.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void updateFirestation_whenMatching_shouldReturnUpdatedStation() throws Exception {
        FirestationModel updated = new FirestationModel();
        updated.setStation("1");
        updated.setAddress("78 Paris");

        when(service.updateFirestation(any(FirestationModel.class))).thenReturn(List.of(updated));

        String json = mapper.writeValueAsString(updated);

        mockMvc.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].station").value("1"))
                .andExpect(jsonPath("$[0].address").value("78 Paris"));
    }

    /**
     * Vérifie que l'endpoint PUT /firestation retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void updateFiresation_serviceThrowsIOException_shouldReturn500() throws Exception {
        FirestationModel updated = new FirestationModel();
        updated.setStation("1");
        updated.setAddress("78 Paris");

        when(service.updateFirestation(any(FirestationModel.class))).thenThrow(new IOException());

        String json = mapper.writeValueAsString(updated);

        mockMvc.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    /**
     * Vérifie que l'endpoint DELETE /firestation supprime correctement une caserne existante.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void deleteFirestation_whenRemovedTrue_shouldReturnTrue() throws Exception {
        when(service.deleteFirestation("77 Paris", "1")).thenReturn(true);

        mockMvc.perform(delete("/firestation").param("address", "77 Paris").param("station", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * Vérifie que l'endpoint DELETE /firestation retourne false lorsque la suppression ne correspond à aucune caserne.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void deleteFirestation_whenRemovedFalse_shouldReturnFalse() throws Exception {
        when(service.deleteFirestation("77 Paris", "1")).thenReturn(false);

        mockMvc.perform(delete("/firestation").param("address", "77 Paris").param("station", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    /**
     * Vérifie que l'endpoint DELETE /firestation retourne un statut HTTP 500 lorsque le service lève une IOException.
     *
     * @throws Exception en cas d'erreur lors de l'exécution du test
     */
    @Test
    void deleteFirestation_serviceThrowsIOException_shouldReturn500() throws Exception {
        when(service.deleteFirestation("77 Paris", "1")).thenThrow(new IOException());

        mockMvc.perform(delete("/firestation").param("address", "77 Paris").param("station", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }
}
