package com.openclassrooms.safetynet_alerts.service;


import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommunityEmailServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CommunityEmailService service;

    private List<PersonModel> persons;

    @BeforeEach
    void setUp(){
        persons = new ArrayList<>();

        PersonModel p1 = new PersonModel();
        p1.setFirstName("Samy");
        p1.setLastName("Ymas");
        p1.setCity("Paris");
        p1.setEmail("Samy@mail.com");

        PersonModel p2 = new PersonModel();
        p2.setFirstName("Cons");
        p2.setLastName("Snoc");
        p2.setCity("PARIS"); // test equalsIgnoreCase
        p2.setEmail("Cons@mail.com");

        PersonModel p3 = new PersonModel();
        p3.setFirstName("Other");
        p3.setLastName("Rehto");
        p3.setCity("Lyon");
        p3.setEmail("other@mail.com");

        persons.add(p1);
        persons.add(p2);
        persons.add(p3);
    }

    @Test
    void getEmailsByCity_shouldReturnEmailsForParis() throws Exception{
        when(personRepository.findAll()).thenReturn(persons);

        List<String> emails = service.getEmailsByCity("Paris");

        assertThat(emails)
                .containsExactlyInAnyOrder("Samy@mail.com", "Cons@mail.com");
    }

    @Test
    void getEmailsByCity_shouldReturnEmptyList_whenNoMatch() throws Exception{
        when(personRepository.findAll()).thenReturn(persons);

        List<String> emails = service.getEmailsByCity("Londres");

        assertThat(emails).isEmpty();
    }

    @Test
    void getEmailsByCity_shouldHandleNullEmail() throws Exception {
        persons.get(0).setEmail(null);

        when(personRepository.findAll()).thenReturn(persons);

        List<String> emails = service.getEmailsByCity("Paris");

        assertThat(emails)
                .containsExactly("Cons@mail.com");
    }

    @Test
    void getEmailsByCity_shouldDeduplicateEmails() throws Exception {
        persons.add(persons.get(0)); // ajoute un doublon de p1

        when(personRepository.findAll()).thenReturn(persons);

        List<String> emails = service.getEmailsByCity("Paris");

        assertThat(emails).containsExactlyInAnyOrder("Samy@mail.com", "Cons@mail.com");
    }


}
