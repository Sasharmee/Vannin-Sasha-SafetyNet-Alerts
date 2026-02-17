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

@ExtendWith(MockitoExtension.class)
public class FirestationControllerTest {

    @Mock
    private FirestationService service;

    @InjectMocks
    private FirestationController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    //GET : Test Happy Path
    @Test
    void getAllFirestation_shouldReturnListOfFirestations() throws Exception{
        //GIVEN: données du test
        FirestationModel f1 =new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        FirestationModel f2 = new FirestationModel();
        f2.setAddress("88 Lyon");
        f2.setStation("2");

        List<FirestationModel> firestations = List.of(f1, f2);

        when(service.getAllFirestation(any(FirestationModel.class))).thenReturn(firestations);

        //WHEN + THEN:HTTP et asseertions
        mockMvc.perform(get("/firestation")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].station").value("1"))
                .andExpect(jsonPath("$[0].address").value("77 Paris"));
    }

    //GET: Test service renvoie IOException
    @Test
    void getAllFirestation_serviceThrowsIOException_shouldReturn500() throws Exception{
        when(service.getAllFirestation(any(FirestationModel.class))).thenThrow(new IOException());

        mockMvc.perform(get("/firestation"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //POST: Test Happy path
    @Test
    void addFirestation_shouldReturnCreatedFirestation() throws Exception{
        //GIVEN
        FirestationModel f1 =new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        when(service.addFirestation(any(FirestationModel.class))).thenReturn(List.of(f1));

        String json = mapper.writeValueAsString(f1);

        //WHEN+THEN
        mockMvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("77 Paris"))
                .andExpect(jsonPath("$[0].station").value("1"));
    }

    //POST : Test service renvoie IOException
    @Test
    void addFirestation_serviceThrowsIOException_shouldReturn500() throws Exception{
        //GIVEN
        FirestationModel f1 =new FirestationModel();
        f1.setAddress("77 Paris");
        f1.setStation("1");

        when(service.addFirestation(any(FirestationModel.class))).thenThrow(new IOException("boom"));

        String json = mapper.writeValueAsString(f1);

        //WHEN+THEN
        mockMvc.perform(post("/firestation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //PUT: Test Happy path
    @Test
    void updateFirestation_whenMatching_shouldReturnUpdatedStation() throws Exception{
        //GIVEN
        FirestationModel updated = new FirestationModel();
        updated.setStation("1");
        updated.setAddress("78 Paris");

        when(service.updateFirestation(any(FirestationModel.class))).thenReturn(List.of(updated));

        String json = mapper.writeValueAsString(updated);

        //WHEN+THEN
        mockMvc.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].station").value("1"))
                .andExpect(jsonPath("$[0].address").value("78 Paris"));
    }

    //PUT: Test service renvoie IOException
    @Test
    void updateFiresation_serviceThrowsIOException_shouldReturn500() throws Exception{
        //GIVEN
        FirestationModel updated = new FirestationModel();
        updated.setStation("1");
        updated.setAddress("78 Paris");

        when(service.updateFirestation(any(FirestationModel.class))).thenThrow(new IOException());

        String json = mapper.writeValueAsString(updated);

        //WHEN+THEN
        mockMvc.perform(put("/firestation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //DELETE: Test Happy path
    @Test
    void deleteFirestation_whenRemovedTrue_shouldReturnTrue() throws Exception{
        when(service.deleteFirestation("77 Paris", "1")).thenReturn(true);

        //WHEN+THEN
        mockMvc.perform(delete("/firestation").param("address", "77 Paris").param("station", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    //DELETE: Test suppresion n'est pas effectuée
    @Test
    void deleteFirestation_whenRemovedFalse_shouldReturnFalse() throws Exception{
        when(service.deleteFirestation("77 Paris", "1")).thenReturn(false);

        //WHEN+THEN
        mockMvc.perform(delete("/firestation").param("address", "77 Paris").param("station", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    //DELETE: Test service renvoie une IOException
    @Test
    void deleteFirestation_serviceThrowsIOException_shouldReturn500() throws Exception{
        when(service.deleteFirestation("77 Paris", "1")).thenThrow(new IOException());

        mockMvc.perform(delete("/firestation").param("address", "77 Paris").param("station", "1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }
}
