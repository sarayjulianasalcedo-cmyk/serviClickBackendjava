package com.sinquinto.serviclick.ServiceOffer.Infrastructure;

import com.sinquinto.serviclick.ServiceOffer.Application.ServiceOfferService;
import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOffer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/service-offers")
public class ServiceOfferController {

    private final ServiceOfferService service;

    @PostMapping
    public ResponseEntity<ServiceOffer> save(@RequestBody ServiceOffer serviceOffer) {
        return new ResponseEntity<>(service.saveServiceOffer(serviceOffer), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServiceOffer>> getAll() {
        return new ResponseEntity<>(service.findAllServiceOffers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOffer> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findServiceOfferById(id), HttpStatus.OK);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ServiceOffer>> findBySeller(@PathVariable Long sellerId) {
        return new ResponseEntity<>(service.findServiceOffersBySeller(sellerId), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(service.countServiceOffers(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOffer> update(@PathVariable Long id, @RequestBody ServiceOffer serviceOffer) {
        return new ResponseEntity<>(service.updateServiceOffer(id, serviceOffer), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteServiceOfferById(id);
    }
}