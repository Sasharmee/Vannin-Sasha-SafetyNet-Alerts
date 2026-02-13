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

@ExtendWith(MockitoExtension.class)
public class ChildAlertControllerTest {

    @Mock
    private ChildAlertService service;

    @InjectMocks
    private ChildAlertController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc= MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    //Test comportement attendu, happy path
    @Test
    void getChildrenByAddress_shouldReturnDTO() throws Exception{
        //données du test
        String address = "77 Paris";
        HouseholdMemberDTO householdMemberDTO = new HouseholdMemberDTO("Cons", "Snoc");
        List<ChildAlertDTO> children = List.of(
                new ChildAlertDTO("Samy", "Ymas", 10, List.of(householdMemberDTO))
        );
        when(service.getChildrenByAddress(address)).thenReturn(children);

        //WHEN+THEN:HTTP et assertions
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

    //Test avec 0 child dans le foyer
    @Test
    void getChildrenByAddress_whenNoChildren_shouldReturnEmpty() throws Exception{
        String address = "77 Paris";
        List<ChildAlertDTO> children = List.of();

        when(service.getChildrenByAddress(address)).thenReturn(children);

        //WHEN+THEN: HTTP et assertions
        mockMvc.perform(get("/childAlert")
                .param("address", address)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    //Test paramètre manquant, address pas renseignée
    @Test
    void getChildrenByAddress_missingAddress_shouldReturn400() throws Exception{
        mockMvc.perform(get("/childAlert"))
                .andExpect(status().isBadRequest());
    }

    //Test d'erreur lorsque service lève une exception
    @Test
    void getChildrenByAddress_serviceThrowsIOException_shouldReturn500() throws Exception{
        //données du test
        String address = "77 Paris";

        when(service.getChildrenByAddress(address)).thenThrow(new IOException("boom"));

        //WHEN+THEN
        mockMvc.perform(get("/childAlert").param("address", address))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }
}
