package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.animals.Dog;
import com.teamwork.telegrambotanimalshelter.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dogs")
public class DogController {
    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostMapping
    @Operation(summary = "Создание новой собаки и добавление ее в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка в параметрах запроса"),
            @ApiResponse(responseCode = "404", description = "Несуществующий URL"),
            @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера")
    })
    public ResponseEntity<Dog> create(@RequestBody Dog dog){
        Dog newDog = dogService.create(dog);
        return ResponseEntity.ok(newDog);
    }

    @DeleteMapping("/id")
    public void delete(@RequestParam Long id){
        dogService.delete(id);
    }

    @GetMapping("/ownerId")
    public ResponseEntity<List<Dog>> getDogListByOwnerId(@RequestParam Long id){
        List<Dog> result = dogService.getAllByUserId(id);
        if(result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Dog>> getAll (){
        List<Dog> getAll = dogService.getAll();
        if (getAll.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(getAll);
    }

    @GetMapping("/dogId")
    public ResponseEntity<Dog> getDogById (@RequestParam Long id){
        Dog dog = dogService.getById(id);
        return ResponseEntity.ok(dog);
    }

    @PutMapping
    public ResponseEntity<Dog> update (@RequestParam Long id,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) Integer age){
        Dog dog = dogService.getById(id);
        if (name != null) {
            dog.setName(name);
        }
        if(age != null) {
            dog.setAge(age);
        }
        dogService.update(dog);
        return ResponseEntity.ok(dog);
    }
}
