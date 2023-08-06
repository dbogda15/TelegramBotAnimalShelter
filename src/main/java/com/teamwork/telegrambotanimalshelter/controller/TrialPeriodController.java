package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.enums.TrialPeriodType;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.TrialPeriodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/trial_period")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно"),
        @ApiResponse(responseCode = "400", description = "Ошибка в параметрах запроса"),
        @ApiResponse(responseCode = "404", description = "Несуществующий URL"),
        @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера")
})
public class TrialPeriodController {
    private final TrialPeriodService trialPeriodService;
    private final AnimalService animalService;

    public TrialPeriodController(TrialPeriodService trialPeriodService, AnimalService animalService) {
        this.trialPeriodService = trialPeriodService;
        this.animalService = animalService;
    }

    @PostMapping
    @Operation(summary = "Создание испытательного срока")
    TrialPeriod create(@RequestParam @Parameter(example = "DD.MM.YYYY") LocalDate dateOfTheStart,
                       @RequestParam Long ownerId,
                       @RequestParam Long animalId,
                       @RequestParam TrialPeriodType type
                       ) {
        AnimalType animalType = animalService.getById(animalId).getAnimalType();
        return trialPeriodService.create(new TrialPeriod(dateOfTheStart, dateOfTheStart.plusDays(30),
                dateOfTheStart.minusDays(1), ownerId, animalId, animalType, new ArrayList<>(), type));
    }

    @DeleteMapping
    @Operation(summary = "")
    void delete(@RequestParam Long id){
        trialPeriodService.delete(id);
    }

    @GetMapping("/id")
    @Operation(summary = "")
    TrialPeriod getById(@RequestParam Long id){
        return trialPeriodService.findById(id);
    }

    @GetMapping("/all")
    @Operation(summary = "")
    List<TrialPeriod> getAll(){
        return trialPeriodService.findAll();
    }

    @GetMapping("/owner_id")
    @Operation(summary = "")
    List<TrialPeriod> getAllByOwnerId(@RequestParam Long ownerId){
        return trialPeriodService.findAllByOwnerId(ownerId);
    }

    @PutMapping
    @Operation(summary = "")
    TrialPeriod update(@RequestParam @Parameter(example = "DD.MM.YYYY") LocalDate dateOfTheStart,
                       @RequestParam @Parameter(example = "DD.MM.YYYY") LocalDate dateOfTheEnd,
                       @RequestParam @Parameter(example = "DD.MM.YYYY") LocalDate dateOfTheLAstReport,
                       @RequestParam Long ownerId,
                       @RequestParam Long animalId,
                       @RequestParam TrialPeriodType type) {
        AnimalType animalType = animalService.getById(animalId).getAnimalType();
        return trialPeriodService.update(new TrialPeriod(dateOfTheStart, dateOfTheEnd, dateOfTheLAstReport, ownerId, animalId, animalType, new ArrayList<>(), type));
    }
}
