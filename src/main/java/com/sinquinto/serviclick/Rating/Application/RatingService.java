package com.sinquinto.serviclick.Rating.Application;

import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.Rating.Application.UseCases.RatingCountUseCase;
import com.sinquinto.serviclick.Rating.Application.UseCases.RatingCrudUseCase;
import com.sinquinto.serviclick.Rating.Domain.Rating;
import com.sinquinto.serviclick.Rating.Domain.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RatingService implements RatingCrudUseCase, RatingCountUseCase {

    private final RatingRepository repository;

    @Override
    public Rating saveRating(Rating rating) {
        rating.setDate(LocalDateTime.now());
        return repository.save(rating);
    }

    @Override
    public Rating findRatingById(Long id) {
        Rating rating = repository.findById(id);
        if (rating == null) {
            throw new ResourceNotFoundException("Rating with id " + id + " not found");
        }
        return rating;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Rating> findAllRatings() {
        return repository.findAll();
    }

    @Override
    public List<Rating> findRatingsByServiceOffer(Long serviceOfferId) {
        return repository.findByServiceOfferId(serviceOfferId);
    }

    @Override
    public List<Rating> findRatingsByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public Rating updateRating(Long id, Rating rating) {
        Rating ratingDB = repository.findById(id);
        if (ratingDB == null) {
            throw new ResourceNotFoundException("Rating with id " + id + " not found");
        }
        ratingDB.setScore(rating.getScore());
        ratingDB.setComment(rating.getComment());
        return repository.save(ratingDB);
    }

    @Override
    public void deleteRatingById(Long id) {
        Rating ratingDB = repository.findById(id);
        if (ratingDB == null) {
            throw new ResourceNotFoundException("Rating with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public Long countRatings() {
        return repository.countRatings();
    }
}