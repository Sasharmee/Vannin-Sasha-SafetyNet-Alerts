package com.openclassrooms.safetynet_alerts.controller;


import com.openclassrooms.safetynet_alerts.service.ChildAlertService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ChildAlertController {

    private final ChildAlertService childAlertService;

    public ChildAlertController(ChildAlertService childAlertService){
        this.childAlertService = childAlertService;
    }

    //GET
}
