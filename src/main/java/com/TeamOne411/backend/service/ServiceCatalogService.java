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

    public List<OfferedService> findServicesByServiceCategory(ServiceCategory serviceCategory) {
        return offeredServiceRepository.findServicesByServiceCategory(serviceCategory);
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
        ServiceCategory catTires = createDefaultServiceCategory("Tires", garage);
        ServiceCategory catBatteries = createDefaultServiceCategory("Batteries", garage);
        ServiceCategory catShocksStruts = createDefaultServiceCategory("Shocks & Struts", garage);
        ServiceCategory catOther = createDefaultServiceCategory("Other/Not Sure", garage);

        //These are the default Services that are enabled for a garage
        //Routine Maintenance
        createDefaultOfferedService("Oil Change", catRoutineMaintenance);
        createDefaultOfferedService("Brake Replacement", catRoutineMaintenance);
        createDefaultOfferedService("Headlight Replacement", catRoutineMaintenance);
        createDefaultOfferedService("Wiper Blade Replacement", catRoutineMaintenance);
        createDefaultOfferedService("Power Steering and Suspension", catRoutineMaintenance);
        createDefaultOfferedService("Check: Fluids", catRoutineMaintenance);
        createDefaultOfferedService("Check: Brakes", catRoutineMaintenance);
        createDefaultOfferedService("Check: Belts & Hoses", catRoutineMaintenance);
        createDefaultOfferedService("Check: Vehicle Health", catRoutineMaintenance);
        createDefaultOfferedService("Check: Air & Cabin Filters", catRoutineMaintenance);
        createDefaultOfferedService("Check: Alternators & Starters", catRoutineMaintenance);

        //Tires
        createDefaultOfferedService("New Tires", catTires);
        createDefaultOfferedService("Alignment", catTires);
        createDefaultOfferedService("Flat Repair", catTires);
        createDefaultOfferedService("TPMS Service", catTires);
        createDefaultOfferedService("Wheel Balance", catTires);
        createDefaultOfferedService("Tire Rotation", catTires);
        createDefaultOfferedService("Seasonal Changeover", catTires);
        createDefaultOfferedService("Check: Pre-Trip Safety", catTires);

        //Batteries
        createDefaultOfferedService("Battery Check", catBatteries);
        createDefaultOfferedService("Battery Installation", catBatteries);

        //Shocks & Struts
        createDefaultOfferedService("Struts & Shocks Consultation", catShocksStruts);

        //consultation differs from check in restoration/upgrade preference vs current safety status - Hope
        createDefaultOfferedService("Check: Struts & Suspension", catShocksStruts);

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