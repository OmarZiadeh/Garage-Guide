package com.TeamOne411.backend.config;

import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.entity.users.Privilege;
import com.TeamOne411.backend.entity.users.Role;
import com.TeamOne411.backend.entity.users.User;
import com.TeamOne411.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OfferedServiceRepository offeredServiceRepository;

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_GG_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_CAR_OWNER", Arrays.asList(readPrivilege));
        createRoleIfNotFound("ROLE_GARAGE_EMPLOYEE", Arrays.asList(readPrivilege));
        createRoleIfNotFound("ROLE_GARAGE_ADMIN", Arrays.asList(readPrivilege));

        createTestUserIfNotFound();

        //initializeDefaultServiceCatalog();

        alreadySetup = true;
    }

    public void initializeDefaultServiceCatalog() {
        //These are the default Categories that are enabled for a garage
        ServiceCategory catRoutineMaintenance = createServiceCategoryIfNotFound("Routine Maintenance");
        ServiceCategory catTires = createServiceCategoryIfNotFound("Tires");
        ServiceCategory catBatteries = createServiceCategoryIfNotFound("Batteries");
        ServiceCategory catShocksStruts = createServiceCategoryIfNotFound("Shocks & Struts");
        ServiceCategory catOther = createServiceCategoryIfNotFound("Other/Not Sure");

        //Routine Maintenance
        createOfferedServiceIfNotFound("Oil Change", catRoutineMaintenance);
        createOfferedServiceIfNotFound("Brake Replacement", catRoutineMaintenance);
        createOfferedServiceIfNotFound("Headlight Replacement", catRoutineMaintenance);
        createOfferedServiceIfNotFound("Wiper Blade Replacement", catRoutineMaintenance);
        createOfferedServiceIfNotFound("Power Steering and Suspension", catRoutineMaintenance);
        createOfferedServiceIfNotFound("Check: Fluids", catRoutineMaintenance);
        createOfferedServiceIfNotFound("Check: Brakes", catRoutineMaintenance);
        createOfferedServiceIfNotFound("Check: Belts & Hoses", catRoutineMaintenance);
        createOfferedServiceIfNotFound("Check: Vehicle Health", catRoutineMaintenance);
        createOfferedServiceIfNotFound("Check: Air & Cabin Filters", catRoutineMaintenance);
        createOfferedServiceIfNotFound("Check: Alternators & Starters", catRoutineMaintenance);

        //Tires
        createOfferedServiceIfNotFound("New Tires", catTires);
        createOfferedServiceIfNotFound("Alignment", catTires);
        createOfferedServiceIfNotFound("Flat Repair", catTires);
        createOfferedServiceIfNotFound("TPMS Service", catTires);
        createOfferedServiceIfNotFound("Wheel Balance", catTires);
        createOfferedServiceIfNotFound("Tire Rotation", catTires);
        createOfferedServiceIfNotFound("Seasonal Changeover", catTires);
        createOfferedServiceIfNotFound("Check: Pre-Trip Safety", catTires);

        //Batteries
        createOfferedServiceIfNotFound("Battery Check", catBatteries);
        createOfferedServiceIfNotFound("Battery Installation", catBatteries);

        //Shocks & Struts
        createOfferedServiceIfNotFound("Struts & Shocks Consultation", catShocksStruts);
        //consultation differs from check in restoration/upgrade preference vs current safety status - Hope
        createOfferedServiceIfNotFound("Check: Struts & Suspension", catShocksStruts);

        //Other/Not Sure - no services to include, but does need to a descriptor for car owner clarity
        createOfferedServiceIfNotFound("Other/Not Sure", catOther);
    }

    @Transactional
    OfferedService createOfferedServiceIfNotFound(String name, ServiceCategory category) {
        OfferedService service = offeredServiceRepository.findByServiceName(name);

        if (service == null) {
            service = new OfferedService();
            service.setServiceName(name);
            service.setServiceCategory(category);
            offeredServiceRepository.save(service);
        }

        return service;
    }

    @Transactional
    ServiceCategory createServiceCategoryIfNotFound(String name) {
        ServiceCategory category = serviceCategoryRepository.findByCategoryName(name);

        if (category == null) {
            category = new ServiceCategory();
            category.setCategoryName(name);
            serviceCategoryRepository.save(category);
        }

        return category;
    }

    private void createTestUserIfNotFound() {
        if (userRepository.findByUsername("test") != null) return;

        Role adminRole = roleRepository.findByName("ROLE_GG_ADMIN");
        User user = new User();
        user.setUsername("test");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Arrays.asList(adminRole));
        user.setIsEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}