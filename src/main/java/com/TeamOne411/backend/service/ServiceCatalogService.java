package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.repository.OfferedServiceRepository;
import com.TeamOne411.backend.repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Back-end service interface for retrieving and updating service catalog data.
 */
@Service
public class ServiceCatalogService {
    private static final Logger LOGGER = Logger.getLogger(GarageEmployeeService.class.getName());
    private OfferedServiceRepository offeredServiceRepository;
    private ServiceCategoryRepository serviceCategoryRepository;

    public ServiceCatalogService(OfferedServiceRepository offeredServiceRepository, ServiceCategoryRepository serviceCategoryRepository) {
        this.offeredServiceRepository = offeredServiceRepository;
        this.serviceCategoryRepository = serviceCategoryRepository;
    }

    /*
    Offered Services Section
     */

    public List<OfferedService> findAllOfferedServices() {
        return offeredServiceRepository.findAll();
    }

    public void saveOfferedService(OfferedService offeredService) {
        offeredServiceRepository.save(offeredService);
    }

    public void deleteOfferedService(OfferedService offeredService) {
        offeredServiceRepository.delete(offeredService);
    }

    /*
    Service Category Section
     */

    public List<ServiceCategory> findAllServiceCategories() {
        return serviceCategoryRepository.findAll();
    }

    public void saveServiceCategory(ServiceCategory serviceCategory) {
        if (serviceCategory == null) {
            LOGGER.log(Level.SEVERE,
                    "GarageEmployee is null. Are you sure you have connected your form to the application?");
            return;
        }
        serviceCategoryRepository.save(serviceCategory);
    }

    public void deleteServiceCategory(ServiceCategory serviceCategory) {
        serviceCategoryRepository.delete(serviceCategory);
    }
}
