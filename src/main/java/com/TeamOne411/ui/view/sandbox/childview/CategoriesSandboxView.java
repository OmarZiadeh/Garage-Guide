package com.TeamOne411.ui.view.sandbox.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.view.sandbox.form.CategoryEditorForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


/**
 * The CategoriesSandboxView is a God-mode ServiceCatalog editor for testing purposes.
 * This should either be deleted as the application evolves or turned into an Admin screen meant only for technical support, well hidden behind authentication.
 */
public class CategoriesSandboxView extends VerticalLayout{
    private Grid<ServiceCategory> grid = new Grid<>(ServiceCategory.class);
    private ServiceCatalogService serviceCatalogService;
    private GarageService garageService;
    private CategoryEditorForm categoryEditorForm = new CategoryEditorForm();
    private Button addCategoryButton = new Button("Add Category");

    /**
     * The constructor for the sandbox view. Does initial layout setup, grid configuration, and event listener attachment
     * @param serviceCatalogService the ServiceCatalogService to broker the repository calls for garage employees
     * @param garageService the GarageService to broker the repository calls for garages
     */
    public CategoriesSandboxView(ServiceCatalogService serviceCatalogService, GarageService garageService) {
        //initial layout setup
        this.serviceCatalogService = serviceCatalogService;
        this.garageService = garageService;
        addClassName("list-view");
        setSizeFull();

        //configure the categories-grid
        grid.addClassName("categories-grid");
        grid.setColumns("categoryName");

        //add garage
        grid.addColumn(serviceCategory -> {
            Garage garage = serviceCategory.getGarage();
            return garage.getCompanyName();
        }).setSortable(true).setHeader("Garage");

        // set column width
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // attach event listener on grid item select
        grid.asSingleSelect().addValueChangeListener(event -> editServiceCategory(event.getValue()));

        // connect the handlers to the form events
        categoryEditorForm.addListener(CategoryEditorForm.SaveEvent.class, this::saveServiceCategory);
        categoryEditorForm.addListener(CategoryEditorForm.DeleteEvent.class, this::deleteServiceCategory);
        categoryEditorForm.addListener(CategoryEditorForm.CloseEvent.class, this::closeCategoryFormHandler);

        // set a click lister to the add button to show the category form and then hide the add button
        addCategoryButton.addClickListener(event -> {
            categoryEditorForm.setVisible(true);
            addCategoryButton.setVisible(false);
        });

        // set the forms' default visibility to false
        categoryEditorForm.setVisible(false);

        // build a div element with title, serviceCatalog grid, and editor forms
        Div serviceCategoriesContent = new Div(
                grid,
                categoryEditorForm
        );

        serviceCategoriesContent.addClassName("serviceCategoriesContent");
        serviceCategoriesContent.setSizeFull();

        // add the components to this layout
        add(addCategoryButton, serviceCategoriesContent);

        // fetch the list for the grid
        updateCategoriesList();

        // pass down garages to the form for the garage combobox
        updateGarageCombobox();

    }

    private void updateGarageCombobox() {
        categoryEditorForm.setGarages(garageService.findAll());
    }

    /**
     * Refreshes the grid list from the database
     */
    private void updateCategoriesList() {
        grid.setItems(serviceCatalogService.findAllServiceCategories());
    }

    /**
     * Saves the given serviceCategory to the database
     * @param event the SaveEvent from the serviceCategory editor form
     */
    private void saveServiceCategory (CategoryEditorForm.SaveEvent event){
        serviceCatalogService.saveServiceCategory(event.getServiceCategory());
        updateCategoriesList();
        closeCategoryEditorForm();
        // TODO add toast to confirm add
    }

    /**
     * Deletes the given serviceCategory from the database.
     * @param event the DeleteEvent from the serviceCategory editor form
     */
    private void deleteServiceCategory (CategoryEditorForm.DeleteEvent event){
        if (event.getServiceCategory() != null) {
            // TODO make this run only after confirm delete dialog
            serviceCatalogService.deleteServiceCategory(event.getServiceCategory());
            updateCategoriesList();
        }
        closeCategoryEditorForm();
    }

    /**
     * Toggles the form visibility and sets initializes form fields if passed a ServiceCategory instance
     * @param serviceCategory the ServiceCategory instance to edit, or null if none is selected
     */
    private void editServiceCategory(ServiceCategory serviceCategory){
        if (serviceCategory == null) {
            closeCategoryEditorForm();
        } else {
            categoryEditorForm.setServiceCategory(serviceCategory);
            categoryEditorForm.setVisible(true);
            addClassName("editing-Category");
        }
    }

    /**
     * Clears and hides the editor form
     */
    private void closeCategoryEditorForm () {
        categoryEditorForm.setServiceCategory(new ServiceCategory());
        categoryEditorForm.setVisible(false);
        removeClassName("editing-Category");
        addCategoryButton.setVisible(true);
    }


    /**
     * Clears and hides the editor form
     */
    private void closeCategoryFormHandler (CategoryEditorForm.CloseEvent event){
        if (event.getServiceCategory() != null) {
            // TODO add confirm dialog
        }
        closeCategoryEditorForm();
    }
}