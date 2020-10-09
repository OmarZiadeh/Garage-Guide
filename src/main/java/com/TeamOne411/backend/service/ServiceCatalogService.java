package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;

import java.io.Serializable;
import java.util.Collection;

/**
 * Back-end service interface for retrieving and updating service catalog data.
 */
public abstract class ServiceCatalogService implements Serializable {

    public abstract Collection<OfferedService> getAllOfferedServices();

    public abstract Collection<ServiceCategory> getAllCategories();

    public abstract void updateOfferedService(OfferedService offeredService);

    public abstract void deleteOfferedService(int offeredServiceId);

    public abstract OfferedService getOfferedServiceById(int offeredServiceId);

    public abstract void updateServiceCategory(ServiceCategory serviceCategory);

    public abstract void deleteServiceCategory(int categoryId);

    public static ServiceCatalogService get() {
        return InitialListService.getInstance();
    }

}
