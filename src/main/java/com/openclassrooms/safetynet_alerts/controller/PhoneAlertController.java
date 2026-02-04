package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.service.PhoneAlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlertController {

    private final PhoneAlertService phoneAlertService;

    public PhoneAlertController(PhoneAlertService phoneAlertService){
        this.phoneAlertService = phoneAlertService;
    }

    //GET
    @GetMapping
    public List<String> getPhoneByStation(@RequestParam("firestation") String stationNumber) throws IOException{
        return phoneAlertService.getPhoneByStation(stationNumber);
    }
}
