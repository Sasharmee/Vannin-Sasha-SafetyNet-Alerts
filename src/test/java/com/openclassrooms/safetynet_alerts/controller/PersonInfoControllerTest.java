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

//extension permet d'utiliser @Mock et @InjectMocks
@ExtendWith(MockitoExtension.class)
public class PersonInfoControllerTest {

    //on crée un faux service
    @Mock
    private PersonInfoService service;

    //Mockito crée le controller et y injecte le faux service crée au préalable
    @InjectMocks
    private PersonInfoController controller;

    //faux serveur HTTP qui simule les appels HTTP sans démarrer Tomcat ni ouvrir le port
    private MockMvc mockMvc;

    //crée un mockMvc en standalone, charge uniquement le controller, on test ce controller et rien d'autre.
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    //Premier test = comportement attendu par la méthode
    @Test
    void getPersonInfoByLastName_shouldReturnInfos() throws Exception{
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

    //Test lorsqu'on ne retrouve personne via le nom renseigné
    @Test
    void getPersonInfoByLastName_shouldReturn200AndEmptyList_personNotFound() throws Exception{
        String lastName = "XYZ";
        when(service.getPersonInfoByLastName(lastName)).thenReturn(List.of());

        //WHEN + THEN
        mockMvc.perform(get("/personInfolastName={lastName}", lastName)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    //Test d'erreur lorsque le service lève une exception
    @Test
    void getPersonInfoByLastName_shouldReturn500_serviceThrowsIOException() throws Exception{
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
