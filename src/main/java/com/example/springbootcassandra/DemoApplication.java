package com.example.springbootcassandra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@RestController
@Slf4j
public class DemoApplication {

  @Autowired
  private PersonRepository personRepository;

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @GetMapping("/persons")
  public List<Person> persons(){
    personRepository.save(
        new Person(
            new PersonKey("joseph", LocalDateTime.now(), UUID.randomUUID()),
            "mbimbi-bene",
            32000.0));
    personRepository.save(
        new Person(
            new PersonKey("joseph", LocalDateTime.now(), UUID.randomUUID()),
            "mbimbi-bene",
            38000.0));
    personRepository.save(
        new Person(
            new PersonKey("julien", LocalDateTime.now(), UUID.randomUUID()),
            "mbimbi-bene",
            38000.0));
//    List<Person> persons = personRepository.findByLastName("mbimbi-bene");
    List<Person> persons = personRepository.findAll();
    return persons;
  }
}
