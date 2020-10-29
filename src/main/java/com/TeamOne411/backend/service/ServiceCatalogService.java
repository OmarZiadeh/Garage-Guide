package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.repository.OfferedServiceRepository;
import com.TeamOne411.backend.repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Back-end service interface for retrieving and updating service catalog data - offered services & service categories.
 */
@Service
public class ServiceCatalogService {

    private OfferedServiceRepository offeredServiceRepository;
    private ServiceCategoryRepository serviceCategoryRepository;

    public ServiceCatalogService(OfferedServiceRepository offeredServiceRepository, ServiceCategoryRepository serviceCategoryRepository) {
        this.offeredServiceRepository = offeredServiceRepository;
        this.serviceCategoryRepository = serviceCategoryRepository;
    }

    public List<OfferedService> findAllOfferedServices() {
        return (offeredServiceRepository.findAll());
    }

    /*
    public List<OfferedService> findServiceByGarage(Garage garage) {
        return offeredServiceRepository.findServiceByGarage(garage);
    }
     */

    public OfferedService findServiceByName(String serviceName) {
        return offeredServiceRepository.findByServiceName(serviceName);
    }

    public ServiceCategory findByCategoryName(String name) {
        return serviceCategoryRepository.findByCategoryName(name);
    }

    public void saveOfferedService(OfferedService offeredService) {
        offeredServiceRepository.save(offeredService);
    }

    public void deleteOfferedService(OfferedService offeredService) {
        offeredServiceRepository.delete(offeredService);
    }

    public List<ServiceCategory> findAllServiceCategories() {
        return (serviceCategoryRepository.findAll());
    }
/*
    public List<ServiceCategory> findCategoriesByGarage(Garage garage) {
        return serviceCategoryRepository.findCategoriesByGarage(garage);
    }
*/
    public void saveServiceCategory(ServiceCategory serviceCategory) {
        serviceCategoryRepository.save(serviceCategory);
    }

    public void deleteServiceCategory(ServiceCategory serviceCategory) {
        serviceCategoryRepository.delete(serviceCategory);
    }
}