package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.servicecatalog.InitialList;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InitialListService extends ServiceCatalogService{

    private static InitialListService INSTANCE;

    private List<OfferedService> offeredServices;
    private List<ServiceCategory> serviceCategories;
    private int nextServiceId = 0;
    private int nextCategoryId = 0;

    public InitialListService() {
        serviceCategories = InitialList.createCategories();
        offeredServices = InitialList.createServices(serviceCategories);
        nextServiceId = offeredServices.size() + 1;
        nextCategoryId = serviceCategories.size()+1;
    }

    public synchronized static ServiceCatalogService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InitialListService();
        }
        return INSTANCE;
    }

    @Override
    public Collection<OfferedService> getAllOfferedServices() {
        return Collections.unmodifiableList(offeredServices);
    }

    @Override
    public Collection<ServiceCategory> getAllCategories() {
        return Collections.unmodifiableList(serviceCategories);
    }

    /*
    TODO -- MUST FINISH COPYING AND MODIFYING FROM BOOKSTORE - MockDataService class
     */


    @Override
    public void updateOfferedService(OfferedService offeredService) {

    }

    @Override
    public void deleteOfferedService(int offeredServiceId) {

    }

    @Override
    public OfferedService getOfferedServiceById(int offeredServiceId) {
        return null;
    }

    @Override
    public void updateServiceCategory(ServiceCategory serviceCategory) {

    }

    @Override
    public void deleteServiceCategory(int categoryId) {

    }
}
