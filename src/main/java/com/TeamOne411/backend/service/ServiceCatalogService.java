package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.servicecatalog.DefaultCatalog;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Back-end service interface for retrieving and updating service catalog data.
 */
public class ServiceCatalogService implements Serializable {

    private static ServiceCatalogService INSTANCE;

    private List<OfferedService> offeredServices;
    private List<ServiceCategory> serviceCategories;
    private int nextServiceId = 0;
    private int nextCategoryId = 0;

    public ServiceCatalogService() {

        serviceCategories = DefaultCatalog.createCategories();
        offeredServices = DefaultCatalog.createServices();
        nextServiceId = offeredServices.size() + 1;
        nextCategoryId = serviceCategories.size() + 1;
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
        return Collections.unmodifiableList(offeredServices);
    }

    public synchronized List<ServiceCategory> getAllCategories() {
        return Collections.unmodifiableList(serviceCategories);
    }

    public synchronized void updateOfferedService(OfferedService offeredService) {

        if (offeredService.getId() < 0) {
            // New product
            offeredService.setId(nextServiceId++);
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
            serviceCategory.setId(nextCategoryId++);
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
