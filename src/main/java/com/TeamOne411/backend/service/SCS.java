package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.servicecatalog.DefaultCatalog;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;

import java.util.Collections;
import java.util.List;


//TODO combine this with the ServiceCatalogService to eliminate the unnecessary abstract class.
public class SCS extends ServiceCatalogService{

    private static SCS INSTANCE;

    private List<OfferedService> offeredServices;
    private List<ServiceCategory> serviceCategories;
    private int nextServiceId = 0;
    private int nextCategoryId = 0;

    public SCS() {

        serviceCategories = DefaultCatalog.createCategories();
        offeredServices = DefaultCatalog.createServices();
        nextServiceId = offeredServices.size() + 1;
        nextCategoryId = serviceCategories.size() + 1;
    }

    public synchronized static ServiceCatalogService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SCS();
        }
        return INSTANCE;
    }

    @Override
    public synchronized List<OfferedService> getAllOfferedServices() {
        return Collections.unmodifiableList(offeredServices);
    }

    @Override
    public synchronized List<ServiceCategory> getAllCategories() {
        return Collections.unmodifiableList(serviceCategories);
    }

    @Override
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

    @Override
    public synchronized void deleteOfferedService(int offeredServiceId) {
        OfferedService offeredService = getOfferedServiceById(offeredServiceId);
        if (offeredService == null) {
            throw new IllegalArgumentException("Service with id " + offeredServiceId
                    + " not found");
        }
        offeredServices.remove(offeredService);
    }

    @Override
    public synchronized OfferedService getOfferedServiceById(int offeredServiceId) {
        for (int i = 0; i < offeredServices.size(); i++) {
            if (offeredServices.get(i).getId() == offeredServiceId) {
                return offeredServices.get(i);
            }
        }
        return null;
    }

    @Override
    public void updateServiceCategory(ServiceCategory serviceCategory) {
        if (serviceCategory.getId() < 0) {
            serviceCategory.setId(nextCategoryId++);
            serviceCategories.add(serviceCategory);
        }
    }

    @Override
    public void deleteServiceCategory(int categoryId) {
        /*  Commented out until method can be rewritten

        if (serviceCategories.removeIf(category -> category.getId() == categoryId)) {
            getAllOfferedServices().forEach(offeredService -> {
                offeredService.getServiceCategory().removeIf(category -> category.getId() == categoryId);
            });


        }
         */

    }
}