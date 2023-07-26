package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.model.animals.Dog;
import com.teamwork.telegrambotanimalshelter.service.DogService;
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
    public ResponseEntity<Dog> create(@RequestParam String name,
                                      @RequestParam Integer age){
        Dog newDog = dogService.create(new Dog(name,age));
        return ResponseEntity.ok(newDog);
    }

    @DeleteMapping

    public void delete(@RequestParam Long id){
        dogService.delete(id);
    }

    @GetMapping
    public ResponseEntity<List<Dog>> getDogListByOwnerId(@RequestParam Long id){
        List<Dog> result = dogService.getAllByUserId(id);
        if(result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<Dog>> getAll (){
        List<Dog> getAll = dogService.getAll();
        if (getAll.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(getAll);
    }

    @GetMapping
    public ResponseEntity<Dog> getDogById (@RequestParam Long id){
        Dog dog = dogService.getById(id);
        return ResponseEntity.ok(dog);
    }

    @PutMapping
    public ResponseEntity<Dog> update (@RequestParam(required = false) String name,
                                       @RequestParam(required = false) Integer age){
        Dog dog = dogService.update(new Dog(name, age));
        return ResponseEntity.ok(dog);
    }
}
