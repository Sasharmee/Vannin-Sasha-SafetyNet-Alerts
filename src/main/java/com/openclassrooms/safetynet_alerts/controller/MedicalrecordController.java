package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.service.MedicalrecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalrecordController {

    private final MedicalrecordService medicalrecordService;

    public MedicalrecordController(MedicalrecordService medicalrecordService){
        this.medicalrecordService = medicalrecordService;
    }

    //GET
    @GetMapping
    public List<MedicalrecordModel> getAllMedicalrecord() throws IOException{
        return medicalrecordService.getAllMedicalrecord();
    }

    //POST
    @PostMapping
    public MedicalrecordModel addMedicalrecord(@RequestBody MedicalrecordModel medicalrecord) throws IOException{
        return medicalrecordService.addMedicalrecord(medicalrecord);
    }

    //PUT
    @PutMapping
    public MedicalrecordModel updateMedicalrecord(@RequestBody MedicalrecordModel medicalrecord) throws IOException{
        return medicalrecordService.updateMedicalrecord(medicalrecord);
    }

    //DELETE
    @DeleteMapping
    public boolean deleteMedicalrecord(@RequestParam String firstName,
                                       @RequestParam String lastName) throws IOException {
        return medicalrecordService.deleteMedicalrecord(firstName, lastName);
    }

}
