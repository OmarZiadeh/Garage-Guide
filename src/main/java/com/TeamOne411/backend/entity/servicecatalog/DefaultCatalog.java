package com.TeamOne411.backend.entity.servicecatalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
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

    /*
    TODO Rewrite class to create an OfferedService with a specific ServiceCategory - get rid of the bookstore demo randomizer
     */


    //TODO fill in rest of the default categories
    private static String[] defaultCategories = new String[] {
            "Routine Maintenance", "Tires", "Brakes" };


    //TODO finish list of services....but need to figure out how the defaulting will work first.
    private static String[] defaultServices = new String[] {"Oil Change", "Vehicle Health Check"};


    /**
     * This method creates the default categories from the defaultCategories array list
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
    /*

    public static List<OfferedService> createServices() {

            OfferedService offeredService =
                    new OfferedService("Oil Change", "When you buy a premium oil change, you’ll also get a free tire rotation!",ServiceCategory.get, nextServiceId++);
            offeredServices.add(offeredService);
        return offeredServices;
    }
/
    /*  Commented out temporarily until these methods can be revised as part of the class rewrite

        private static OfferedService createOfferedService(List<ServiceCategory> categories) {
            OfferedService offeredService = new OfferedService();
            offeredService.setId(nextServiceId++);
            offeredService.setServiceName(generateName());
            offeredService.setPrice(new BigDecimal((random.nextInt(250) + 50) / 10.0));
            offeredService.setCategory(getCategory(categories, 1, 2));
            return offeredService;
        }

    }
 */
}
