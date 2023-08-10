package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.shelters.Shelter;
import com.teamwork.telegrambotanimalshelter.service.ShelterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/shelter")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно"),
        @ApiResponse(responseCode = "400", description = "Ошибка в параметрах запроса"),
        @ApiResponse(responseCode = "404", description = "Несуществующий URL"),
        @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера")
})
public class ShelterController {
    private final ShelterService shelterService;

    @PostMapping
    @Operation(summary = "Создание приюта и добавдение в БД")
    public ResponseEntity<Shelter> create(@RequestParam String name,
                                          @RequestParam String location,
                                          @RequestParam String timetable,
                                          @RequestParam String aboutMe,
                                          @RequestParam String security,
                                          @RequestParam String safetyAdvice,
                                          @RequestParam AnimalType shelterType){
        Shelter shelter = new Shelter(name,location,timetable,aboutMe,security,safetyAdvice,shelterType, new ArrayList<>());
        shelterService.create(shelter);
        return ResponseEntity.ok(shelter);
    }

    @DeleteMapping
    @Operation(summary = "Удаление приюта из БД")
    void delete(@RequestParam Long id){
        shelterService.delete(id);
    }

    @GetMapping("/id")
    @Operation(summary = "Поиск приюта по его ID")
    public ResponseEntity<Shelter> getById(@RequestParam Long id){
        Shelter result = shelterService.getById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    @Operation(summary = "Обновление данных по приюту")
    public ResponseEntity<Shelter> update(@RequestParam Long id,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) String location,
                                          @RequestParam(required = false) String timetable,
                                          @RequestParam(required = false) String aboutMe,
                                          @RequestParam(required = false) String security,
                                          @RequestParam(required = false) String safetyAdvice,
                                          @RequestParam(required = false) AnimalType shelterType){
        Shelter shelter = shelterService.getById(id);
        if(name != null){
            shelter.setName(name);
        }
        if(location != null){
            shelter.setLocation(location);
        }
        if(timetable != null){
            shelter.setTimetable(timetable);
        }
        if(aboutMe != null){
            shelter.setAboutMe(aboutMe);
        }
        if (security != null){
            shelter.setSecurity(security);
        }
        if(safetyAdvice != null){
            shelter.setSafetyAdvice(safetyAdvice);
        }
        if( shelterType != null){
            shelter.setShelterType(shelterType);
        }
        shelterService.update(shelter);
        return ResponseEntity.ok(shelter);
    }

    @GetMapping("/animal_list")
    @Operation(summary = "Получение списка животных по ID приюта")
    ResponseEntity<List<Animal>> getAnimalsFromShelter(@RequestParam Long id){
        List<Animal> result = shelterService.getAnimals(id);
        return ResponseEntity.ok(result);
    }
}
