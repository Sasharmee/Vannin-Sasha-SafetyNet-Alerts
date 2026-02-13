package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.service.MedicalrecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalrecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalrecordController.class);

    private final MedicalrecordService medicalrecordService;

    public MedicalrecordController(MedicalrecordService medicalrecordService){
        this.medicalrecordService = medicalrecordService;
    }

    //GET
    @GetMapping
    public List<MedicalrecordModel> getAllMedicalrecord() throws IOException{

        logger.info("GET /medicalRecord called");

        List<MedicalrecordModel> result = medicalrecordService.getAllMedicalrecord();

        logger.info("GET /medicalRecord success, {} medicalrecords returned", result.size());

        return result;
    }

    //POST
    @PostMapping
    public MedicalrecordModel addMedicalrecord(@RequestBody MedicalrecordModel medicalrecord) throws IOException{

        logger.info("POST /medicalRecord firstName={} lastName={}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        MedicalrecordModel result = medicalrecordService.addMedicalrecord(medicalrecord);

        logger.info("POST /medicalRecord success for firstName={} lastName={}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        return result;
    }

    //PUT
    @PutMapping
    public MedicalrecordModel updateMedicalrecord(@RequestBody MedicalrecordModel medicalrecord) throws IOException{
        logger.info("PUT /medicalRecord firstName={} lastName={}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        MedicalrecordModel result = medicalrecordService.updateMedicalrecord(medicalrecord);

        logger.info("PUT /medicalRecord success for firstName={} lastName={}", medicalrecord.getFirstName(), medicalrecord.getLastName());

        return result;
    }

    //DELETE
    @DeleteMapping
    public boolean deleteMedicalrecord(@RequestParam String firstName,
                                       @RequestParam String lastName) throws IOException {
        logger.info("DELETE /medicalRecord firstName={} lastName={}", firstName, lastName);

        boolean removed = medicalrecordService.deleteMedicalrecord(firstName, lastName);

        logger.info("DELETE /medicalRecord success removed={}", removed);

        return removed;
    }

}
