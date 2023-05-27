package com.example.demo;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PetController {

  private final PetRepository petRepository;

  PetController(PetRepository petRepository) {
    this.petRepository = petRepository;
  }


  // Aggregate root
  // tag::get-aggregate-root[]
  @GetMapping("/pets")
  List<Pet> all() {
    return petRepository.findAll();
  }
  // end::get-aggregate-root[]

  @PostMapping("/pets")
  Pet newPet(@RequestBody Pet newPet) {
    return petRepository.save(newPet);
  }

  // Single item
  
  @GetMapping("/pet/{id}")
  Pet one(@PathVariable Long id) {
    
    return petRepository.findById(id)
      .orElseThrow(() -> new PetNotFoundException(id));
  }

  @PutMapping("/pet/{id}")
  Pet replacePet(@RequestBody Pet newPet, @PathVariable Long id) {
    
    return petRepository.findById(id)
      .map(pet -> {
        pet.setName(newPet.getName());
        pet.setAge(newPet.getAge());
        return petRepository.save(pet);
      })
      .orElseGet(() -> {
        newPet.setId(id);
        return petRepository.save(newPet);
      });
  }

  @DeleteMapping("/employees/{id}")
  void deletePet(@PathVariable Long id) {
    petRepository.deleteById(id);
  }
}