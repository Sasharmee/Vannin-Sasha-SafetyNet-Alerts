package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.service.MedicalrecordService;
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
public class MedicalrecordControllerTest {

    @Mock
    private MedicalrecordService service;

    @InjectMocks
    MedicalrecordController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mockMvc= MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    //GET: Test Happy Path
    @Test
    void getAllMedicalrecord_shouldReturnListOfMedicalrecords() throws  Exception{
        //GIVEN: données du test
        MedicalrecordModel mr1= new MedicalrecordModel();
        mr1.setFirstName("Samy");
        mr1.setLastName("Ymas");
        mr1.setBirthdate("03/06/1985");
        mr1.setMedications(List.of("medication:50mg"));
        mr1.setAllergies(List.of("codeine"));

        MedicalrecordModel mr2= new MedicalrecordModel();
        mr2.setFirstName("Cons");
        mr2.setLastName("Snoc");
        mr2.setBirthdate("03/06/1990");
        mr2.setMedications(List.of("medication:100mg"));
        mr2.setAllergies(List.of("fish"));

        List<MedicalrecordModel> medicalrecords = List.of(mr1, mr2);

        when(service.getAllMedicalrecord()).thenReturn(medicalrecords);

        //WHEN+THEN
        mockMvc.perform(get("/medicalRecord")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Samy"))
                .andExpect(jsonPath("$[0].lastName").value("Ymas"))
                .andExpect(jsonPath("$[0].birthdate").value("03/06/1985"))
                .andExpect(jsonPath("$[0].medications[0]").value("medication:50mg"))
                .andExpect(jsonPath("$[0].allergies[0]").value("codeine"));
    }

    //GET: Service renvoie IOException
    @Test
    void getAllMedicalrecord_serviceThrowsIOException_shouldReturn500() throws Exception{
        when(service.getAllMedicalrecord()).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //POST: Test Happy Path
    @Test
    void addMedicalrecord_shouldReturnCreatedMedicalrecord() throws Exception{
        //GIVEN: données du test
        MedicalrecordModel mr1= new MedicalrecordModel();
        mr1.setFirstName("Samy");
        mr1.setLastName("Ymas");
        mr1.setBirthdate("03/06/1985");
        mr1.setMedications(List.of("medication:50mg"));
        mr1.setAllergies(List.of("codeine"));

        when(service.addMedicalrecord(any(MedicalrecordModel.class))).thenReturn(mr1);

        String json = mapper.writeValueAsString(mr1);

        //WHEN+THEN
        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Samy"))
                .andExpect(jsonPath("$.lastName").value("Ymas"))
                .andExpect(jsonPath("$.birthdate").value("03/06/1985"))
                .andExpect(jsonPath("$.medications[0]").value("medication:50mg"))
                .andExpect(jsonPath("$.allergies[0]").value("codeine"));
    }

    //POST: Test service renvoie IOException
    @Test
    void addMedicalrecord_serviceThrowsIOException_shouldReturn500() throws Exception{
        MedicalrecordModel mr1= new MedicalrecordModel();
        mr1.setFirstName("Samy");
        mr1.setLastName("Ymas");
        mr1.setBirthdate("03/06/1985");
        mr1.setMedications(List.of("medication:50mg"));
        mr1.setAllergies(List.of("codeine"));

        when(service.addMedicalrecord(any(MedicalrecordModel.class))).thenThrow(new IOException("boom"));
        String json = mapper.writeValueAsString(mr1);

        mockMvc.perform(post("/medicalRecord").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //PUT: Test Happy Path
    @Test
    void updateMedicalrecord_shouldReturnUpdatedMedicalrecord() throws Exception{
        //GIVEN
        MedicalrecordModel updated= new MedicalrecordModel();
        updated.setFirstName("Samy");
        updated.setLastName("Ymas");
        updated.setBirthdate("03/06/1985");
        updated.setMedications(List.of("medication:50mg"));
        updated.setAllergies(List.of("codeine"));

        when(service.updateMedicalrecord(any(MedicalrecordModel.class))).thenReturn(updated);

        String json = mapper.writeValueAsString(updated);

        //WHEN+THEN
        mockMvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Samy"))
                .andExpect(jsonPath("$.lastName").value("Ymas"))
                .andExpect(jsonPath("$.birthdate").value("03/06/1985"))
                .andExpect(jsonPath("$.medications[0]").value("medication:50mg"))
                .andExpect(jsonPath("$.allergies[0]").value("codeine"));
    }

    //PUT: Test service renvoie IOException
    @Test
    void updateMedicalrecord_serviceThrowsIOException_shouldReturn500() throws Exception{
        //GIVEN
        MedicalrecordModel updated= new MedicalrecordModel();
        updated.setFirstName("Samy");
        updated.setLastName("Ymas");
        updated.setBirthdate("03/06/1985");
        updated.setMedications(List.of("medication:50mg"));
        updated.setAllergies(List.of("codeine"));

        when(service.updateMedicalrecord(any(MedicalrecordModel.class))).thenThrow(new IOException("boom"));

        String json = mapper.writeValueAsString(updated);

        mockMvc.perform(put("/medicalRecord").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //DELETE: Test Happy path
    @Test
    void deleteMedicalrecord_shouldReturnTrue() throws Exception{
        when(service.deleteMedicalrecord("Samy", "Ymas")).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord").param("firstName", "Samy").param("lastName", "Ymas"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    //DELETE: Test suppression echec
    @Test
    void deleteMedicalrecord_shouldReturnFalse() throws Exception{
        when(service.deleteMedicalrecord("Samy", "Ymas")).thenReturn(false);

        mockMvc.perform(delete("/medicalRecord").param("firstName", "Samy").param("lastName", "Ymas"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    //DELETE: Test service renvoie IOException
    @Test
    void deleteMedicalRecord_serviceThrowsIOException_shouldReturn500() throws Exception{
        when(service.deleteMedicalrecord("Samy", "Ymas")).thenThrow(new IOException("boom"));

        mockMvc.perform(delete("/medicalRecord").param("firstName", "Samy").param("lastName", "Ymas"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //PEUT ajouter missing param en test
}
