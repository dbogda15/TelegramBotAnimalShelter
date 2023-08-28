package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.TrialPeriod;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
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

    public TrialPeriodController(TrialPeriodService trialPeriodService) {
        this.trialPeriodService = trialPeriodService;
    }

    @PostMapping
    @Operation(summary = "Создание испытательного срока")
    TrialPeriod create(@RequestParam @Parameter(example = "DD.MM.YYYY") LocalDate dateOfTheStart,
                       @RequestParam Long ownerId,
                       @RequestParam Long animalId,
                       @RequestParam AnimalType animalType,
                       @RequestParam TrialPeriodType type
                       ) {
        return trialPeriodService.create(new TrialPeriod(dateOfTheStart, dateOfTheStart.plusDays(30),
                dateOfTheStart.minusDays(1), ownerId, animalId, animalType, new ArrayList<>(), type));
    }

    @DeleteMapping
    @Operation(summary = "Удаление испытательного срока по ID")
    void delete(@RequestParam Long id){
        trialPeriodService.deleteById(id);
    }

    @GetMapping("/id")
    @Operation(summary = "Получение испытательного срока по ID")
    TrialPeriod getById(@RequestParam Long id){
        return trialPeriodService.findById(id);
    }

    @GetMapping("/all")
    @Operation(summary = "Получение списка всех испытательных сроков")
    List<TrialPeriod> getAll(){
        return trialPeriodService.findAll();
    }

    @GetMapping("/owner_id")
    @Operation(summary = "Получение списка испытательных сроков по ID владельца")
    List<TrialPeriod> getAllByOwnerId(@RequestParam Long ownerId){
        return trialPeriodService.findAllByOwnerId(ownerId);
    }

    @PutMapping
    @Operation(summary = "Обновление испытательного срока по ID")
    TrialPeriod update(@RequestParam Long id,
                       @RequestParam (required = false) @Parameter(example = "DD.MM.YYYY") LocalDate dateOfTheStart,
                       @RequestParam (required = false) @Parameter(example = "DD.MM.YYYY") LocalDate dateOfTheEnd,
                       @RequestParam (required = false) @Parameter(example = "DD.MM.YYYY") LocalDate dateOfTheLastReport,
                       @RequestParam (required = false) Long ownerId,
                       @RequestParam (required = false) Long animalId,
                       @RequestParam (required = false) AnimalType animalType,
                       @RequestParam (required = false) TrialPeriodType type) {
        return trialPeriodService.update(new TrialPeriod(id, dateOfTheStart, dateOfTheEnd, dateOfTheLastReport, ownerId, animalId,
                animalType, new ArrayList<>(), type));
    }
}
