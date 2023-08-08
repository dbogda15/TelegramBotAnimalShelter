package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {
    private final AnimalService animalService;
    private final OwnerService ownerService;

    public AnimalController(AnimalService animalService, OwnerService ownerService) {
        this.animalService = animalService;
        this.ownerService = ownerService;
    }

    @PostMapping
    @Operation(summary = "Создание нового животного и добавление его в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка в параметрах запроса"),
            @ApiResponse(responseCode = "404", description = "Несуществующий URL"),
            @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера")
    })
    public ResponseEntity<Animal> create(@RequestParam AnimalType animalType,
                                         @RequestParam String name,
                                         @RequestParam Integer age,
                                         @RequestParam Long shelterId) {
        Animal newAnimal = new Animal();
        newAnimal.setAnimalType(animalType);
        newAnimal.setName(name);
        newAnimal.setAge(age);
        newAnimal.setShelterId(shelterId);
        animalService.create(newAnimal);
        return ResponseEntity.ok(newAnimal);
    }

    @DeleteMapping("/id")
    @Operation(summary = "Удаление животного из БД по его ID")
    public void delete(@RequestParam Long id) {
        animalService.delete(id);
    }

    @GetMapping("/owner")
    @Operation(summary = "Найти владельца по ID животного")
    public ResponseEntity<Owner> getOwnerByAnimalId(@RequestParam Long animalId) {
        return ResponseEntity.ok(animalService.getById(animalId).getOwner());
    }

    @GetMapping(value = "/getAll")
    @Operation(summary = "Получить список всех животных")
    public ResponseEntity<List<Animal>> getAll() {
        List<Animal> result = animalService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/free_animal")
    @Operation(summary = "Получить список свободных животных")
    public ResponseEntity<List<Animal>> getFreeAnimalList() {
        List<Animal> result = animalService.freeAnimalList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/animalId")
    @Operation(summary = "Получить информацию о животном по его ID")
    public ResponseEntity<Animal> getAnimalById(@RequestParam Long id) {
        Animal animal = animalService.getById(id);
        return ResponseEntity.ok(animal);
    }

    @PutMapping
    @Operation(summary = "Обновить информацию о животном")
    public ResponseEntity<Animal> update(@RequestParam Long id,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) Integer age) {
        Animal animal = animalService.getById(id);
        if (name != null) {
            animal.setName(name);
        }
        if (age != null) {
            animal.setAge(age);
        }
        animalService.update(animal);
        return ResponseEntity.ok(animal);
    }

    @PutMapping("/owner_id")
    @Operation(summary = "Назначить животному хозяина")
    public ResponseEntity<Animal> setOwnerId(@RequestParam Long id,
                                             @RequestParam Long owner_id) {
        Animal animal = animalService.getById(id);
        animal.setOwner(ownerService.getById(owner_id));
        animalService.update(animal);
        return ResponseEntity.ok(animal);
    }
}
