package com.sinquinto.serviclick.Rating.Application;

import com.sinquinto.serviclick.Exception.Domain.ResourceDuplicateException;
import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.Rating.Application.UseCases.RatingCountUseCase;
import com.sinquinto.serviclick.Rating.Application.UseCases.RatingCrudUseCase;
import com.sinquinto.serviclick.Rating.Domain.Rating;
import com.sinquinto.serviclick.Rating.Domain.RatingRepository;
import com.sinquinto.serviclick.Rating.Infrastructure.DTO.RatingDTO;
import com.sinquinto.serviclick.User.Application.UserService;
import com.sinquinto.serviclick.User.Domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingService implements RatingCrudUseCase, RatingCountUseCase {

    private final RatingRepository repository;
    private final UserService userService;

    public RatingService(RatingRepository repository, @Lazy UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Rating saveRating(Rating rating) {
        if (rating.getAppointmentId() != null && repository.existsByAppointmentId(rating.getAppointmentId())) {
            throw new ResourceDuplicateException("Ya existe una reseña para esta cita");
        }
        rating.setDate(LocalDateTime.now());
        return repository.save(rating);
    }

    public boolean existsRatingForAppointment(Long appointmentId) {
        return repository.existsByAppointmentId(appointmentId);
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

    public List<RatingDTO> findRatingsDTOByServiceOffer(Long serviceOfferId) {
        return repository.findByServiceOfferId(serviceOfferId).stream()
                .map(r -> {
                    String clientName = "Cliente";
                    try {
                        User user = userService.findUserById(r.getUserId());
                        clientName = user.getName() + " " + user.getLastName();
                    } catch (Exception ignored) {}
                    return new RatingDTO(clientName, r.getScore(), r.getComment(), r.getDate());
                })
                .toList();
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