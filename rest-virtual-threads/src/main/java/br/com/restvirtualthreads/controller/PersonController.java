package br.com.restvirtualthreads.controller;

import br.com.restvirtualthreads.domain.response.PersonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("persons")
public class PersonController {

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> findById(@PathVariable("id") Long id) {
        log.info("M=findById, I=Find person with ID={}", id);
        try {
            Thread.sleep(2000);
            return ResponseEntity.ok(new PersonResponse(id, "John Doe"));
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to find person with id: " + id, e);
        }
    }

}
