package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.enums.AnimalType;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Создание нового животного и добавление его в БД без привязки к приюту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка в параметрах запроса"),
            @ApiResponse(responseCode = "404", description = "Несуществующий URL"),
            @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера")
    })
    public ResponseEntity<Animal> create(@RequestParam AnimalType animalType,
                                         @RequestParam String name,
                                         @RequestParam Integer age) {
        Animal newAnimal = new Animal();
        newAnimal.setAnimalType(animalType);
        newAnimal.setName(name);
        newAnimal.setAge(age);
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
                                         @RequestParam(required = false) AnimalType animalType,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) Integer age) {
        Animal animal = animalService.update(new Animal(id, animalType, name, age));
        return ResponseEntity.ok(animal);
    }

    @PutMapping("/owner_id")
    @Operation(summary = "Назначить животному хозяина")
    public ResponseEntity<Animal> setOwnerId(@RequestParam Long id,
                                             @RequestParam Long owner_id) {
        Animal updated = animalService.setOwner(id, ownerService.getById(owner_id));
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/shelter_id")
    @Operation(summary = "Пристроить животное в приют")
    public ResponseEntity<Animal> setShelterId(@RequestParam Long animalId,
                                               @RequestParam Long shelterId) {
        Animal updated = animalService.setShelterId(animalId, shelterId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/cat_list")
    @Operation(summary = "Получение списка котов")
    public ResponseEntity<List<Animal>> getCatList() {
        List<Animal> result = animalService.getAnimalsByType(AnimalType.CAT);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/dog_list")
    @Operation(summary = "Получение списка собак")
    public ResponseEntity<List<Animal>> getDogList() {
        List<Animal> result = animalService.getAnimalsByType(AnimalType.DOG);
        return ResponseEntity.ok(result);
    }
}