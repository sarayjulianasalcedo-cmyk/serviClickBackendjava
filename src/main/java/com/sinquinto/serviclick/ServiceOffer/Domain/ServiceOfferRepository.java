package com.sinquinto.serviclick.ServiceOffer.Domain;

import java.util.List;

public interface ServiceOfferRepository {
    ServiceOffer save(ServiceOffer serviceOffer);
    ServiceOffer findById(Long id);
    List<ServiceOffer> findAll();
    List<ServiceOffer> findBySellerId(Long sellerId);
    void deleteById(Long id);
    Long countServiceOffers();
}