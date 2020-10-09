package com.TeamOne411.backend.entity.servicecatalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class InitialList {

    private static int nextCategoryId = 1;
    private static int nextServiceId = 1;
    private static final Random random = new Random(1);
    /*
    TODO Rewrite class to create an OfferedService with a specific ServiceCategory - get rid of the bookstore demo randomizer
     */

    private static final String[] categoryNames = new String[] {
            "Children's books", "Best sellers", "Romance", "Mystery",
            "Thriller", "Sci-fi", "Non-fiction", "Cookbooks" };

    private static String[] word1 = new String[] { "The art of", "Mastering",
            "The secrets of", "Avoiding", "For fun and profit: ",
            "How to fail at", "10 important facts about",
            "The ultimate guide to", "Book of", "Surviving", "Encyclopedia of",
            "Very much", "Learning the basics of", "The cheap way to",
            "Being awesome at", "The life changer:", "The Vaadin way:",
            "Becoming one with", "Beginners guide to",
            "The complete visual guide to", "The mother of all references:" };

    private static String[] word2 = new String[] { "gardening",
            "living a healthy life", "designing tree houses", "home security",
            "intergalaxy travel", "meditation", "ice hockey",
            "children's education", "computer programming", "Vaadin TreeTable",
            "winter bathing", "playing the cello", "dummies", "rubber bands",
            "feeling down", "debugging", "running barefoot",
            "speaking to a big audience", "creating software", "giant needles",
            "elephants", "keeping your wife happy" };

    public static List<ServiceCategory> createCategories() {
        List<ServiceCategory> serviceCategories = new ArrayList<ServiceCategory>();
        for (String name : categoryNames) {
            ServiceCategory serviceCategory = createCategory(name);
            serviceCategories.add(serviceCategory);
        }
        return serviceCategories;

    }

    public static List<OfferedService> createServices(List<ServiceCategory> serviceCategories) {
        List<OfferedService> offeredServices = new ArrayList<OfferedService>();
        for (int i = 0; i < 100; i++) {
            OfferedService offeredService = createOfferedService(serviceCategories);
            offeredServices.add(offeredService);
        }
        return offeredServices;
    }

    private static ServiceCategory createCategory(String name) {
        ServiceCategory c = new ServiceCategory();
        c.setId(nextCategoryId++);
        c.setName(name);
        return c;
    }

    private static OfferedService createOfferedService(List<ServiceCategory> categories) {
        OfferedService offeredService = new OfferedService();
        offeredService.setId(nextServiceId++);
        offeredService.setServiceName(generateName());
        offeredService.setPrice(new BigDecimal((random.nextInt(250) + 50) / 10.0));
        offeredService.setCategory(getCategory(categories, 1, 2));
        return offeredService;
    }

    private static Set<ServiceCategory> getCategory(List<ServiceCategory> categories,
                                             int min, int max) {
        int nr = random.nextInt(max) + min;
        HashSet<ServiceCategory> serviceCategories = new HashSet<ServiceCategory>();
        for (int i = 0; i < nr; i++) {
            serviceCategories.add(categories.get(random.nextInt(categories
                    .size())));
        }

        return serviceCategories;
    }

    private static String generateName() {
        return word1[random.nextInt(word1.length)] + " "
                + word2[random.nextInt(word2.length)];
    }

}
