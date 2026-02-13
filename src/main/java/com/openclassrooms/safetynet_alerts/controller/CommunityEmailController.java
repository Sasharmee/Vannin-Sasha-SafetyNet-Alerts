package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.service.CommunityEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class CommunityEmailController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);

    private final CommunityEmailService communityEmailService;

    public CommunityEmailController(CommunityEmailService communityEmailService){
        this.communityEmailService = communityEmailService;
    }

    @GetMapping("/communityEmail")
    public List<String> getEmailsByCity(@RequestParam String city) throws IOException{

        logger.info("GET /communityEmail city={}", city);

        List<String> emails = communityEmailService.getEmailsByCity(city);

        logger.info("GET /communityEmail succes, {} emails returned", emails.size());

        return emails;
    }
}
