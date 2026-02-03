package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.model.PersonModel;
import com.openclassrooms.safetynet_alerts.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService service;

    public PersonController (PersonService service){
        this.service = service;

    }

    //GET
    @GetMapping
    public List<PersonModel> getAllPersons() throws IOException {
        return service.getAllPersons();
    }

    //POST
    @PostMapping
    public PersonModel addPerson(@RequestBody PersonModel person) throws IOException {
        return service.addPerson(person);
    }

    //PUT
    @PutMapping
    public PersonModel updatePerson(@RequestBody PersonModel person) throws IOException {
        return service.updatePerson(person);
    }

    //DELETE
    @DeleteMapping
    public boolean deletePerson(
            @RequestParam String firstName,
            @RequestParam String lastName
    ) throws IOException {
        return service.deletePerson(firstName, lastName);
    }
}

