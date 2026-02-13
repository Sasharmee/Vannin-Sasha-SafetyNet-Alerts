package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.dto.FloodDTO;
import com.openclassrooms.safetynet_alerts.dto.ResidentInfoDTO;
import com.openclassrooms.safetynet_alerts.service.FloodService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FloodControllerTest {

    //on crée un faux service
    @Mock
    private FloodService floodService;

    //mockito crée le controller et y injecte le service
    @InjectMocks
    private FloodController controller;

    //faux serveur HTTP qui simule les appels sans démarrer Tomcat ni ouvrir le port
    private MockMvc mockMvc;

    //créer un mockMvc en standalone, charge uniquement le controller, on test ce controller et rien d'autre
    @BeforeEach
    void setUp(){
        mockMvc= MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    //Premier test = comportement attendu par la méthode
    @Test
    void getFloodByStation_shouldReturnFloodDTO() throws Exception{
        //données du test
        String stations ="1,2";
        ResidentInfoDTO resident = new ResidentInfoDTO("Ymas","123-456-789", 51, List.of("medication:50mg"), List.of("codeine"));
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

    //Test= paramètre manquant
    @Test
    void getFloodByStations_shouldReturn400_missingStations() throws Exception{
        mockMvc.perform(get("/flood/stations"))
                .andExpect(status().isBadRequest());
    }

    //Test d'erreur lorsque notre service lève une exception
    @Test
    void getFloodByStations_serviceThrowsIOException_shouldReturn500() throws Exception{
        String stations ="1,2";
        ResidentInfoDTO resident = new ResidentInfoDTO("Ymas","123-456-789", 51, List.of("medication:50mg"), List.of("codeine"));
        FloodDTO floodDTO = new FloodDTO("77 Paris", List.of(resident));

        when(floodService.getFloodByStation(stations)).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/flood/stations")
                .param("stations", stations))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }
}
