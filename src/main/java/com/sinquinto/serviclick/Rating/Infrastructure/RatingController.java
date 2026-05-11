package com.sinquinto.serviclick.Rating.Infrastructure;

import com.sinquinto.serviclick.Rating.Application.RatingService;
import com.sinquinto.serviclick.Rating.Domain.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService service;

    @PostMapping
    public ResponseEntity<Rating> save(@RequestBody Rating rating) {
        return new ResponseEntity<>(service.saveRating(rating), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Rating>> getAll() {
        return new ResponseEntity<>(service.findAllRatings(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findRatingById(id), HttpStatus.OK);
    }

    @GetMapping("/service-offer/{serviceOfferId}")
    public ResponseEntity<List<Rating>> findByServiceOffer(@PathVariable Long serviceOfferId) {
        return new ResponseEntity<>(service.findRatingsByServiceOffer(serviceOfferId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rating>> findByUser(@PathVariable Long userId) {
        return new ResponseEntity<>(service.findRatingsByUser(userId), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(service.countRatings(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rating> update(@PathVariable Long id, @RequestBody Rating rating) {
        return new ResponseEntity<>(service.updateRating(id, rating), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteRatingById(id);
    }
}