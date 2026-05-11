package com.sinquinto.serviclick.Rating.Application.UseCases;

import com.sinquinto.serviclick.Rating.Domain.Rating;
import java.util.List;

public interface RatingCrudUseCase {
    Rating saveRating(Rating rating);
    Rating findRatingById(Long id);
    List<Rating> findAllRatings();
    List<Rating> findRatingsByServiceOffer(Long serviceOfferId);
    List<Rating> findRatingsByUser(Long userId);
    Rating updateRating(Long id, Rating rating);
    void deleteRatingById(Long id);
}