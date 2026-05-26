package com.sinquinto.serviclick.Rating.Infrastructure.Repository;

import com.sinquinto.serviclick.Rating.Domain.Rating;
import com.sinquinto.serviclick.Rating.Domain.RatingRepository;
import com.sinquinto.serviclick.Rating.Infrastructure.Mapper.RatingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RatingRepositoryImpl implements RatingRepository {

    private final RatingJpaRepository jpaRepository;
    private final RatingMapper mapper;

    @Override
    public Rating save(Rating rating) {
        return mapper.ratingEntityToRating(
                jpaRepository.save(mapper.ratingToEntity(rating))
        );
    }

    @Override
    public Rating findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::ratingEntityToRating)
                .orElse(null);
    }

    @Override
    public List<Rating> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::ratingEntityToRating)
                .toList();
    }

    @Override
    public List<Rating> findByServiceOfferId(Long serviceOfferId) {
        return jpaRepository.findByServiceOfferId(serviceOfferId)
                .stream()
                .map(mapper::ratingEntityToRating)
                .toList();
    }

    @Override
    public List<Rating> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId)
                .stream()
                .map(mapper::ratingEntityToRating)
                .toList();
    }

    @Override
    public boolean existsByAppointmentId(Long appointmentId) {
        return jpaRepository.existsByAppointmentId(appointmentId);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Long countRatings() {
        return jpaRepository.count();
    }
}