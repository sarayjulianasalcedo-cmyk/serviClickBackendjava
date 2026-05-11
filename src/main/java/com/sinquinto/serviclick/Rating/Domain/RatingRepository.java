package com.sinquinto.serviclick.Rating.Domain;

import java.util.List;

public interface RatingRepository {
    Rating save(Rating rating);
    Rating findById(Long id);
    List<Rating> findAll();
    List<Rating> findByServiceOfferId(Long serviceOfferId);
    List<Rating> findByUserId(Long userId);
    void deleteById(Long id);
    Long countRatings();
}