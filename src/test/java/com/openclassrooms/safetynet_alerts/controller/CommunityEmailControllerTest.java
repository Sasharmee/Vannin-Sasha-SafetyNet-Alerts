package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.service.CommunityEmailService;
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

//extension permet d'utiliser @Mock et @InjectMocks
@ExtendWith(MockitoExtension.class)
public class CommunityEmailControllerTest {

    //on créé un faux service
    @Mock
    private CommunityEmailService service;

    //Mockito créé le controller et y injecte le service créé
    @InjectMocks
    private CommunityEmailController controller;

    //faux serveur HTTP qui simule les appels HTTP sans démarrer tomcat ni ouvrir le port
    private MockMvc mockMvc;


    //créé un MockMvc en standalone, charge uniquement le controller, on test ce controller et rien d'autre
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    //méthode testée et comportement attendu par la méthode
    @Test
    void getEmailsByCity_shouldReturnListOfEmails() throws Exception{
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

    //Test d'erreur lorsque notre service lève une exception
    @Test
    void getEmailsByCity_serviceThrowsIOException_shouldReturn500() throws Exception{
        //Données du test
        String city = "Paris";
        List<String> emails = List.of("Samy@mail.com", "cons@mail.com");
        when(service.getEmailsByCity(city)).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/communityEmail")
                .param("city", city))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //Test lorsque la ville n'est pas renseignée
    @Test
    void getEmailsByCity_missingCity_shouldReturn400() throws Exception {

        mockMvc.perform(get("/communityEmail"))
                .andExpect(status().isBadRequest());
    }
}
