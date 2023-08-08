package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import com.teamwork.telegrambotanimalshelter.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RequiredArgsConstructor
@RestController
@RequestMapping("/shelter")
public class ShelterController {
    private final ShelterService shelterService;

    @PostMapping
    @Operation(summary = "создание приюта")
    public ResponseEntity<Shelter> create(@RequestParam String name,
                                          @RequestParam String location,
                                          @RequestParam String timetable,
                                          @RequestParam String aboutMe,
                                          @RequestParam String security,
                                          @RequestParam String safetyAdvice,
                                          @RequestParam AnimalType shelterType){
       Shelter shelter1 = new Shelter(name,location,timetable,aboutMe,security,safetyAdvice,shelterType, new ArrayList<>());
               shelterService.create(shelter1);
       return ResponseEntity.ok(shelter1);
    }

}
