package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.dto.FireDTO;
import com.openclassrooms.safetynet_alerts.dto.ResidentInfoDTO;
import com.openclassrooms.safetynet_alerts.service.FireService;
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
public class FireControllerTest {

    @Mock
    private FireService service;

    @InjectMocks
    private FireController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();

    }

    //Test comportement attendu, happy path
    @Test
    void getFireByAddress_shouldReturnResident() throws Exception{
        //données du test
        String address = "77 Paris";
        ResidentInfoDTO resident = new ResidentInfoDTO("Ymas", "123-456-789", 51, List.of("medication:50mg"), List.of("codeine"));
        FireDTO fireDTO = new FireDTO(List.of(resident), "1");

        when(service.getFireByAddress(address)).thenReturn(fireDTO);

        //WHEN + THEN: appel et assertions
        mockMvc.perform(get("/fire")
                .param("address", address)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value("1"))
                .andExpect(jsonPath("$.residents[0].lastName").value("Ymas"))
                .andExpect(jsonPath("$.residents[0].phone").value("123-456-789"))
                .andExpect(jsonPath("$.residents[0].age").value(51))
                .andExpect(jsonPath("$.residents[0].medications[0]").value("medication:50mg"))
                .andExpect(jsonPath("$.residents[0].allergies[0]").value("codeine"));
    }

    //Test quand on ne retrouve pas de person avec l'addres
    @Test
    void getFireByAddress_whenNoPersonFoundByAddress_shouldReturn200AndEmptyList() throws Exception{
        String address = "XYZ";
        FireDTO empty = new FireDTO(List.of(), "1");
        when(service.getFireByAddress(address)).thenReturn(empty);

        mockMvc.perform(get("/fire")
                .param("address", address)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.residents").isArray())
                .andExpect(jsonPath("$.residents").isEmpty());
    }

    //Test lorsque le service lève une exception
    @Test
    void getFirebyAdress_serviceThrowsIOException_shouldReturn500() throws Exception{
        //données du test
        String address = "77 Paris";
        ResidentInfoDTO resident = new ResidentInfoDTO("Ymas", "123-456-789", 51, List.of("medication:50mg"), List.of("codeine"));
        FireDTO fireDTO = new FireDTO(List.of(resident), "1");

        when(service.getFireByAddress(address)).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/fire")
                .param("address", address))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

}
