package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.dto.PersonInfoDTO;
import com.openclassrooms.safetynet_alerts.service.PersonInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class PersonInfoController {

    private static final Logger logger = LoggerFactory.getLogger(PersonInfoController.class);

    private final PersonInfoService personInfoService;

    public PersonInfoController(PersonInfoService personInfoService){
        this.personInfoService = personInfoService;
    }

    @GetMapping("/personInfolastName={lastName}")
    public List<PersonInfoDTO> getPersonInfoByLastName(@PathVariable String lastName) throws IOException {

        logger.info("GET /personInfolastName={} called", lastName);

        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName(lastName);

        logger.info("GET /personInfolastName={} success, {} results returned", lastName, result.size());

        return result;
    }
}
