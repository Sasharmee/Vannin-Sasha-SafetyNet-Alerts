package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.service.PersonService;
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
public class PersonControllerTest {

    @Mock
    private PersonService service;

    @InjectMocks
    private PersonController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp(){
        mockMvc= MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    //GET: TEST Happy path
    @Test
    void getAllPersons_shouldReturnListOfPersons() throws Exception{
        //GIVEN: données du test
        PersonModel p1 = new PersonModel();
        p1.setFirstName("Samy");
        p1.setLastName("Ymas");
        p1.setAddress("77 Paris");
        p1.setPhone("123-456-789");

        PersonModel p2 = new PersonModel();
        p2.setFirstName("Cons");
        p2.setLastName("Snoc");
        p2.setAddress("77 Paris");
        p2.setPhone("111-222-333");

        List<PersonModel> persons = List.of(p1, p2);

        when(service.getAllPersons()).thenReturn(persons);

        //WHEN+THEN
        mockMvc.perform(get("/person")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Samy"))
                .andExpect(jsonPath("$[0].lastName").value("Ymas"))
                .andExpect(jsonPath("$[0].address").value("77 Paris"))
                .andExpect(jsonPath("$[0].phone").value("123-456-789"));
    }

    //GET : Test Service renvoie IOException
    @Test
    void getAllPersons_serviceThrowsIOException_shouldReturn500() throws Exception{
        when(service.getAllPersons()).thenThrow(new IOException("boom"));

        mockMvc.perform(get("/person"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //POST: Test Happy path
    @Test
    void addPerson_shouldReturnCreatedPerson() throws Exception{
        //GIVEN
        PersonModel person = new PersonModel();
        person.setFirstName("Samy");
        person.setLastName("Ymas");
        person.setAddress("77 Paris");
        person.setPhone("123-456-789");

        when(service.addPerson(any(PersonModel.class))).thenReturn(person);


        String json = mapper.writeValueAsString(person);

        //WHEN+THEN
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Samy"))
                .andExpect(jsonPath("$.lastName").value("Ymas"))
                .andExpect(jsonPath("$.address").value("77 Paris"))
                .andExpect(jsonPath("$.phone").value("123-456-789"));
    }

    //POST : Test service renvoie IOException
    @Test
    void addPerson_serviceThrowsIOException_shouldReturn500() throws Exception{
        PersonModel p1 = new PersonModel();
        p1.setFirstName("Samy");
        p1.setLastName("Ymas");
        p1.setAddress("77 Paris");
        p1.setPhone("123-456-789");
        when(service.addPerson(any(PersonModel.class))).thenThrow(new IOException("boom"));

        String json = mapper.writeValueAsString(p1);

        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //PUT: Test Happy path (update person)
    @Test
    void updatePerson_shouldReturnUpdatedPerson()  throws Exception{
        //GIVEN
        PersonModel updated = new PersonModel();
        updated.setFirstName("Samy");
        updated.setLastName("Ymas");
        updated.setAddress("77 Paris");
        updated.setPhone("123-456-789");

        when(service.updatePerson(any(PersonModel.class))).thenReturn(updated);

        String json = mapper.writeValueAsString(updated);

        //WHEN+THEN
        mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Samy"))
                .andExpect(jsonPath("$.lastName").value("Ymas"))
                .andExpect(jsonPath("$.address").value("77 Paris"))
                .andExpect(jsonPath("$.phone").value("123-456-789"));
    }

    //PUT : Test service renvoie IOException
    @Test
    void updatePerson_serviceThrowsIOException_shouldReturn500() throws Exception{
        //GIVEN
        PersonModel updated = new PersonModel();
        updated.setFirstName("Samy");
        updated.setLastName("Ymas");
        updated.setAddress("77 Paris");
        updated.setPhone("123-456-789");

        when(service.updatePerson(any(PersonModel.class))).thenThrow(new IOException("boom"));

        String json = mapper.writeValueAsString(updated);

        mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }

    //DELETE : Test Happy path
    @Test
    void deletePerson_whenRemovedTrue_shouldReturnTrue() throws Exception{
        when(service.deletePerson("Samy", "Ymas")).thenReturn(true);

        mockMvc.perform(delete("/person")
                .param("firstName", "Samy")
                .param("lastName", "Ymas"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    //DELETE : Test quand la suppresion ne fonctionne pas
    @Test
    void deletePerson_whenRemovedFalse_shouldReturnFalse() throws Exception{
        when(service.deletePerson("Samy", "Ymas")).thenReturn(false);

        mockMvc.perform(delete("/person")
                        .param("firstName", "Samy")
                        .param("lastName", "Ymas"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    //DELETE : Test quand la person à supprimer n'est pas renseignée
    @Test
    void deletePerson_missingParam_shouldReturn400() throws Exception{
        mockMvc.perform(delete("/person"))
                .andExpect(status().isBadRequest());
    }

    //DELETE : test service renvoie une IOException
    @Test
    void deletePerson_serviceThrowsIOException_shouldReturn500() throws Exception{
        when(service.deletePerson("Samy", "Ymas")).thenThrow(new IOException("boom"));

        mockMvc.perform(delete("/person").param("firstName", "Samy").param("lastName", "Ymas"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));
    }
}
