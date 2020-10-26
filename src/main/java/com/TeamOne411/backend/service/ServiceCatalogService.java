package com.TeamOne411.backend.service;

import com.TeamOne411.backend.config.DefaultCatalog;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Back-end service interface for retrieving and updating service catalog data.
 */
@Service
public class ServiceCatalogService implements Serializable {

    private static ServiceCatalogService INSTANCE;

    private List<OfferedService> offeredServices;
    private List<ServiceCategory> serviceCategories;

    public ServiceCatalogService() {

        serviceCategories = DefaultCatalog.createCategories();
        offeredServices = DefaultCatalog.createServices();
    }

    /**
     * This checks if the service catalog already exists for the garage or not. (EAK - I think?)
     * If the service catalog doesn't exists, then a new service catalog is created.
     * @return Instance
     */
    public synchronized static ServiceCatalogService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceCatalogService();
        }
        return INSTANCE;
    }

    public synchronized List<OfferedService> getAllOfferedServices() {
        return (offeredServices);
    }

    public synchronized List<ServiceCategory> getAllCategories() {
        return (serviceCategories);
    }

    public synchronized void updateOfferedService(OfferedService offeredService) {

        if (offeredService.getId() < 0) {
            // New product
            offeredServices.add(offeredService);
            return;
        }
        for (int i = 0; i < offeredServices.size(); i++) {
            if (offeredServices.get(i).getId() == offeredService.getId()) {
                offeredServices.set(i, offeredService);
                return;
            }
        }

        throw new IllegalArgumentException("No service with id " + offeredService.getId()
                + " found");

    }

    public synchronized void deleteOfferedService(int offeredServiceId) {
        OfferedService offeredService = getOfferedServiceById(offeredServiceId);
        if (offeredService == null) {
            throw new IllegalArgumentException("Service with id " + offeredServiceId
                    + " not found");
        }
        offeredServices.remove(offeredService);
    }

    public synchronized OfferedService getOfferedServiceById(int offeredServiceId) {
        for (int i = 0; i < offeredServices.size(); i++) {
            if (offeredServices.get(i).getId() == offeredServiceId) {
                return offeredServices.get(i);
            }
        }
        return null;
    }

    public void updateServiceCategory(ServiceCategory serviceCategory) {
        if (serviceCategory.getId() < 0) {
            serviceCategories.add(serviceCategory);
        }
    }


    public void deleteServiceCategory(int categoryId) {
        /*  Commented out until method can be rewritten

        if (serviceCategories.removeIf(category -> category.getId() == categoryId)) {
            getAllOfferedServices().forEach(offeredService -> {
                offeredService.getServiceCategory().removeIf(category -> category.getId() == categoryId);
            });


        }
         */

    }
    /*
    TODO: This needs to be added to the GarageService (or whichever class performs the garage creation)
    / public static ServiceCatalogService get() {
        return ServiceCatalogService.getInstance();
    }
    */
}
