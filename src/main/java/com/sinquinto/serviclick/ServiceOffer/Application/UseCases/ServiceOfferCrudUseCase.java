package com.sinquinto.serviclick.ServiceOffer.Application.UseCases;

import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOffer;
import java.util.List;

public interface ServiceOfferCrudUseCase {
    ServiceOffer saveServiceOffer(ServiceOffer serviceOffer);
    ServiceOffer findServiceOfferById(Long id);
    List<ServiceOffer> findAllServiceOffers();
    List<ServiceOffer> findServiceOffersBySeller(Long sellerId);
    ServiceOffer updateServiceOffer(Long id, ServiceOffer serviceOffer);
    void deleteServiceOfferById(Long id);
}