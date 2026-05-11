package com.sinquinto.serviclick.ServiceOffer.Infrastructure.Mapper;

import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOffer;
import com.sinquinto.serviclick.ServiceOffer.Infrastructure.Entity.ServiceOfferEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceOfferMapper {
    ServiceOffer serviceOfferEntityToServiceOffer(ServiceOfferEntity entity);
    ServiceOfferEntity serviceOfferToEntity(ServiceOffer serviceOffer);
}