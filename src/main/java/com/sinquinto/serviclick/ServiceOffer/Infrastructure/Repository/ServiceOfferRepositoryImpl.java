package com.sinquinto.serviclick.ServiceOffer.Infrastructure.Repository;

import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOffer;
import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOfferRepository;
import com.sinquinto.serviclick.ServiceOffer.Infrastructure.Mapper.ServiceOfferMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ServiceOfferRepositoryImpl implements ServiceOfferRepository {

    private final ServiceOfferJpaRepository jpaRepository;
    private final ServiceOfferMapper mapper;

    @Override
    public ServiceOffer save(ServiceOffer serviceOffer) {
        return mapper.serviceOfferEntityToServiceOffer(
                jpaRepository.save(mapper.serviceOfferToEntity(serviceOffer))
        );
    }

    @Override
    public ServiceOffer findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::serviceOfferEntityToServiceOffer)
                .orElse(null);
    }

    @Override
    public List<ServiceOffer> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::serviceOfferEntityToServiceOffer)
                .toList();
    }

    @Override
    public List<ServiceOffer> findBySellerId(Long sellerId) {
        return jpaRepository.findBySellerId(sellerId)
                .stream()
                .map(mapper::serviceOfferEntityToServiceOffer)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Long countServiceOffers() {
        return jpaRepository.count();
    }
}