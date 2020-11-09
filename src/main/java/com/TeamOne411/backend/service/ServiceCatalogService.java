package com.TeamOne411.backend.service;

import com.TeamOne411.backend.entity.Garage;
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
        return offeredServiceRepository.findAll();
    }

    public List<OfferedService> findByServiceCategory_Garage(Garage garage) {
        return offeredServiceRepository.findByServiceCategory_Garage(garage);
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

    public List<ServiceCategory> findCategoriesByGarage(Garage garage) {
        return serviceCategoryRepository.findCategoriesByGarage(garage);
    }

    public void saveServiceCategory(ServiceCategory serviceCategory) {
        serviceCategoryRepository.save(serviceCategory);
    }

    public void deleteServiceCategory(ServiceCategory serviceCategory) {
        serviceCategoryRepository.delete(serviceCategory);
    }

    /**
     * This class assigns the default categories and services to a garage when a garage is first created
     *
     * @param garage The new garage that default categories and services should be assigned to
     */
    public void initializeDefaultServices(Garage garage) {

        //These are the default Categories that are enabled for a garage
        ServiceCategory catRoutineMaintenance = createDefaultServiceCategory("Routine Maintenance", garage);
        ServiceCategory catTireServices = createDefaultServiceCategory("Tire Services", garage);
        ServiceCategory catRepairServices = createDefaultServiceCategory("Repair Services", garage);
        ServiceCategory catOther = createDefaultServiceCategory("Other", garage);

        //These are the default Services that are enabled for a garage

        //Routine Maintenance
        createDefaultOfferedService("Oil Change", catRoutineMaintenance);
        createDefaultOfferedService("Filter Replacement", catRoutineMaintenance);
        createDefaultOfferedService("Brake Replacement", catRoutineMaintenance);
        createDefaultOfferedService("Headlight Replacement", catRoutineMaintenance);
        createDefaultOfferedService("Wiper Blade Replacement", catRoutineMaintenance);
        createDefaultOfferedService("Battery Replacement", catRoutineMaintenance);
        createDefaultOfferedService("Fluid Change", catRoutineMaintenance);

        //Tire Services
        createDefaultOfferedService("New Tires", catTireServices);
        createDefaultOfferedService("Tire Rotation", catTireServices);
        createDefaultOfferedService("Alignment", catTireServices);
        createDefaultOfferedService("Flat Tire Repair", catTireServices);
        createDefaultOfferedService("Tire Pressure Monitoring System", catTireServices);
        createDefaultOfferedService("Wheel Balance", catTireServices);

        //Repair Services
        createDefaultOfferedService("Heating and Cooling", catRepairServices);
        createDefaultOfferedService("Belts and Hoses", catRepairServices);
        createDefaultOfferedService("Steering and Suspension", catRepairServices);

        //Other/Not Sure - no services to include, but does need to a descriptor for car owner clarity
        createDefaultOfferedService("Other/Not Sure", catOther);
    }

    /**
     * This class creates the default categories
     *
     * @param name   The category name
     * @param garage The garage the category will be assigned to
     * @return ServiceCategory object that has been created
     */
    ServiceCategory createDefaultServiceCategory(String name, Garage garage) {
        ServiceCategory category = new ServiceCategory();
        category.setCategoryName(name);
        category.setGarage(garage);
        serviceCategoryRepository.save(category);
        return category;
    }

    /**
     * This class creates the default offered services
     *
     * @param name     The service name
     * @param category The ServiceCategory that the service should be associated to
     */
    private void createDefaultOfferedService(String name, ServiceCategory category) {
        OfferedService service = new OfferedService();
        service.setServiceName(name);
        service.setServiceCategory(category);
        offeredServiceRepository.save(service);
    }
}