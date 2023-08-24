package com.teamwork.telegrambotanimalshelter.controller;

import com.teamwork.telegrambotanimalshelter.exceptions.IncorrectArgumentException;
import com.teamwork.telegrambotanimalshelter.model.animals.Animal;
import com.teamwork.telegrambotanimalshelter.model.owners.Owner;
import com.teamwork.telegrambotanimalshelter.service.AnimalService;
import com.teamwork.telegrambotanimalshelter.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Запрос выполнен успешно"),
        @ApiResponse(responseCode = "400", description = "Ошибка в параметрах запроса"),
        @ApiResponse(responseCode = "404", description = "Несуществующий URL"),
        @ApiResponse(responseCode = "500", description = "Ошибка со стороны сервера")
})
public class OwnerController {
    private final OwnerService ownerService;
    private final AnimalService animalService;

    public OwnerController(OwnerService ownerService, AnimalService animalService) {
        this.ownerService = ownerService;
        this.animalService = animalService;
    }

    @PostMapping
    @Operation(summary = "Создание нового пользователя без привязки к животному")
    public Owner create(@RequestParam String name,
                        @RequestParam @Parameter(description = "89171230011") String phone) throws IncorrectArgumentException {
        return ownerService.create(new Owner(name, phone));
    }

    @PostMapping("/animal_owner")
    @Operation(summary = "Создание владельца с привязкой к животному")
    public Owner create(@RequestParam Long ownerId,
                       @RequestParam Long animalId) throws IncorrectArgumentException {
        return ownerService.create(ownerId, animalId);
    }

    @GetMapping("/animal")
    @Operation(summary = "Получить информацию о животных по ID владельца")
    public List<Animal> getAnimalByOwnerId(@RequestParam Long owner_id){
        return ownerService.getAnimalByOwnerId(owner_id);
    }

    @GetMapping("/owner_id")
    @Operation(summary = "Получить информацию о владельце по его ID")
    public ResponseEntity<Owner> getOwnerById(@RequestParam Long id){
        return ResponseEntity.ok(ownerService.getById(id));
    }

    @GetMapping("/get_all")
    @Operation(summary = "Получить список всех владельцев")
    public ResponseEntity<List<Owner>> getAll(){
        return ResponseEntity.ok(ownerService.getAll());
    }

    @DeleteMapping
    @Operation(summary = "Удалить владельца из БД по его ID")
    public void delete(@RequestParam Long id){
        ownerService.delete(id);
    }

    @PutMapping
    @Operation(summary = "Обновить информацию о владельце")
    public ResponseEntity<Owner> update (@RequestParam Long id,
                                         @RequestParam (required = false) Long chatId,
                                         @RequestParam (required = false) String name,
                                         @RequestParam (required = false) @Parameter(description = "89171230011") String phone,
                                         @RequestParam (required = false) Long animal_id) {
        Owner owner = ownerService.getById(id);
        if (chatId != null){
            owner.setChatId(chatId);
        }
        if (name != null) {
            owner.setName(name);
        }
        if (phone != null) {
            owner.setPhone(phone);
        }
        if (animal_id != null) {
            List<Animal> animals = owner.getAnimals();
            animals.add(animalService.getById(animal_id));
            owner.setAnimals(animals);
        }
        ownerService.update(owner);
        return ResponseEntity.ok(owner);
    }

}
