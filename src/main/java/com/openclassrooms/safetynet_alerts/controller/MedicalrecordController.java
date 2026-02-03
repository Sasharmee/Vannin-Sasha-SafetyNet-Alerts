package com.openclassrooms.safetynet_alerts.controller;

import com.openclassrooms.safetynet_alerts.model.MedicalrecordModel;
import com.openclassrooms.safetynet_alerts.service.MedicalrecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalrecordController {

    private final MedicalrecordService medicalrecordService;

    public MedicalrecordController(MedicalrecordService medicalrecordService){
        this.medicalrecordService = medicalrecordService;
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
    public boolean deleteMedicalrecord(@RequestParam String firstName, @RequestParam String lastName) throws IOException{
        medicalrecordService.deleteMedicalrecord(firstName, lastName);
        return ResponseEntity.noContent().build().hasBody(); // à vérifier ici sinon on met comme dans personController
    }
}
