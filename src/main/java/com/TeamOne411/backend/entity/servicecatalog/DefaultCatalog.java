package com.TeamOne411.backend.entity.servicecatalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class generates a default service catalog for the garage
 */
public class DefaultCatalog {

    private static int nextCategoryId = 1;
    private static int nextServiceId = 1;

    //The serviceCategories ArrayList is needed by the InitialListService
    private static List<ServiceCategory> serviceCategories = new ArrayList<>();
    //The offeredServices ArrayList is needed by the InitialListService
    private static List<OfferedService> offeredServices = new ArrayList<>();

    //These are the default Categories that are enabled for a garage
    private static String[] defaultCategories = { "Routine Maintenance", "Tires", "Batteries", "Shocks & Struts", "Other/Not Sure"};


    /**
     * This method creates the default categories from the defaultCategories array
     * @return the default List of categories for a garage
     */
    public static List<ServiceCategory> createCategories() {

        for (String name : defaultCategories) {
            ServiceCategory serviceCategory = new ServiceCategory();
            serviceCategory.setName(name);
            serviceCategory.setId(nextCategoryId++);
            serviceCategories.add(serviceCategory);
        }
        return serviceCategories;
    }

    /**
     * This method returns the service category for use in the createServices() method
     * @param category A String representing the service category that should be returned
     * @return The service category found
     */
    private static ServiceCategory getDefaultCategory(String category){
        int index = serviceCategories.indexOf(category);
        return serviceCategories.get(index);
    }

    /*The HashMap stores the default services that are enabled for a garage
    See method createDefaultServices() for list of services and corresponding category
     */
    private static HashMap<String, String> defaultServices = new HashMap<>();

    /**
     * This method adds the default services to the HashMap.
     * Update this method when default services should be changed.
     * The first value is the service, the second value is the category.
     */
    private static void createDefaultServices(){
        //Routine Maintenance
        defaultServices.put("Oil Change", "Routine Maintenance");
        defaultServices.put("Brake Replacement", "Routine Maintenance");
        defaultServices.put("Headlight Replacement", "Routine Maintenance");
        defaultServices.put("Wiper Blade Replacement", "Routine Maintenance");
        defaultServices.put("Power Steering and Suspension", "Routine Maintenance");
        defaultServices.put("Check: Fluids", "Routine Maintenance");
        defaultServices.put("Check: Brakes", "Routine Maintenance");
        defaultServices.put("Check: Belts & Hoses", "Routine Maintenance");
        defaultServices.put("Check: Vehicle Health", "Routine Maintenance");
        defaultServices.put("Check: Air & Cabin Filters", "Routine Maintenance");
        defaultServices.put("Check: Alternators & Starters", "Routine Maintenance");

        //Tires
        defaultServices.put("New Tires", "Tires");
        defaultServices.put("Alignment", "Tires");
        defaultServices.put("Flat Repair", "Tires");
        defaultServices.put("TPMS Service", "Tires");
        defaultServices.put("Wheel Balance", "Tires");
        defaultServices.put("Tire Rotation", "Tires");
        defaultServices.put("Seasonal Changeover", "Tires");
        defaultServices.put("Check: Pre-Trip Safety", "Tires");

        //Batteries
        defaultServices.put("Battery Check", "Batteries");
        defaultServices.put("Battery Installation", "Batteries");

        //Shocks & Struts
        defaultServices.put("Struts & Shocks Consultation", "Shocks & Struts");
            //consultation differs from check in restoration/upgrade preference vs current safety status - Hope
        defaultServices.put("Check: Struts & Suspension", "Shocks & Struts");

        //Other/Not Sure - no services to include, but does need to a descriptor for car owner clarity
        defaultServices.put("Other/Not Sure", "Other/Not Sure");
    }

    /**
     * This method creates the default services from the defaultCategories array
     * @return the default List of offered services for a garage
     */
    public static List<OfferedService> createServices() {

        createDefaultServices();
        Set<Map.Entry<String,String>> set = defaultServices.entrySet();

        for(Map.Entry<String,String> me : set) {

            OfferedService offeredService = new OfferedService();
            offeredService.setId(nextServiceId++);
            offeredService.setServiceName(me.getKey());
            offeredService.setServiceCategory(getDefaultCategory(me.getValue()));
            offeredServices.add(offeredService);
        }
        return offeredServices;
    }
}