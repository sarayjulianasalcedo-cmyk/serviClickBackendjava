package com.sinquinto.serviclick.Rating.Infrastructure.Mapper;

import com.sinquinto.serviclick.Rating.Domain.Rating;
import com.sinquinto.serviclick.Rating.Infrastructure.Entity.RatingEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    Rating ratingEntityToRating(RatingEntity entity);
    RatingEntity ratingToEntity(Rating rating);
}