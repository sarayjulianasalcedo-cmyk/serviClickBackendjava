package com.sinquinto.serviclick.ServiceOffer.Application;

import com.sinquinto.serviclick.Exception.Domain.ResourceNotFoundException;
import com.sinquinto.serviclick.ServiceOffer.Application.UseCases.ServiceOfferCountUseCase;
import com.sinquinto.serviclick.ServiceOffer.Application.UseCases.ServiceOfferCrudUseCase;
import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOffer;
import com.sinquinto.serviclick.ServiceOffer.Domain.ServiceOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ServiceOfferService implements ServiceOfferCrudUseCase, ServiceOfferCountUseCase {

    private final ServiceOfferRepository repository;

    @Override
    public ServiceOffer saveServiceOffer(ServiceOffer serviceOffer) {
        serviceOffer.setPublicationDate(LocalDateTime.now());
        return repository.save(serviceOffer);
    }

    @Override
    public ServiceOffer findServiceOfferById(Long id) {
        ServiceOffer serviceOffer = repository.findById(id);
        if (serviceOffer == null) {
            throw new ResourceNotFoundException("ServiceOffer with id " + id + " not found");
        }
        return serviceOffer;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ServiceOffer> findAllServiceOffers() {
        return repository.findAll();
    }

    @Override
    public List<ServiceOffer> findServiceOffersBySeller(Long sellerId) {
        return repository.findBySellerId(sellerId);
    }

    @Override
    public ServiceOffer updateServiceOffer(Long id, ServiceOffer serviceOffer) {
        ServiceOffer serviceOfferDB = repository.findById(id);
        if (serviceOfferDB == null) {
            throw new ResourceNotFoundException("ServiceOffer with id " + id + " not found");
        }
        serviceOfferDB.setTitle(serviceOffer.getTitle());
        serviceOfferDB.setDescription(serviceOffer.getDescription());
        serviceOfferDB.setPrice(serviceOffer.getPrice());
        serviceOfferDB.setEstimatedDuration(serviceOffer.getEstimatedDuration());
        serviceOfferDB.setCategory(serviceOffer.getCategory());
        serviceOfferDB.setPhoto(serviceOffer.getPhoto());
        return repository.save(serviceOfferDB);
    }

    @Override
    public void deleteServiceOfferById(Long id) {
        ServiceOffer serviceOfferDB = repository.findById(id);
        if (serviceOfferDB == null) {
            throw new ResourceNotFoundException("ServiceOffer with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public Long countServiceOffers() {
        return repository.countServiceOffers();
    }
}