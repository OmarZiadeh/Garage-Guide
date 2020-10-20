package com.TeamOne411.ui.view.sandbox.childview;

import com.TeamOne411.backend.entity.users.CarOwner;
import com.TeamOne411.backend.service.CarOwnerService;
import com.TeamOne411.ui.view.sandbox.form.CarOwnerEditorForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * The CarOwnerSandboxView is a God-mode CarOwner editor for testing purposes.
 * This should either be deleted as the application evolves or turned into an Admin screen meant only for technical support, well hidden behind authentication.
 */
public class CarOwnerSandboxView extends VerticalLayout {
    private Grid<CarOwner> grid = new Grid<>(CarOwner.class);
    private CarOwnerService carOwnerService;
    private CarOwnerEditorForm form = new CarOwnerEditorForm();
    private Button addButton = new Button("Add CarOwner");

    /**
     * The constructor for the sandbox view. Does initial layout setup, grid configuration, and event listener attachment
     * @param carOwnerService the CarOwnerService to broker the repository calls for garage employees
     */
    public CarOwnerSandboxView(CarOwnerService carOwnerService) {
        // initial layout setup
        this.carOwnerService = carOwnerService;
        addClassName("list-view");
        setSizeFull();

        // configure the carOwner grid
        grid.addClassName("car-owner-grid");
        grid.setHeightByRows(true);
        grid.setColumns("username", "firstName", "lastName", "email", "phoneNumber", "address");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // attach event listener on grid item select
        grid.asSingleSelect().addValueChangeListener(event -> editCarOwner(event.getValue()));

        // connect the handlers to the form events
        form.addListener(CarOwnerEditorForm.SaveEvent.class, this::saveCarOwner);
        form.addListener(CarOwnerEditorForm.DeleteEvent.class, this::deleteCarOwner);
        form.addListener(CarOwnerEditorForm.CloseEvent.class, this::closeCarOwnerFormHandler);

        // set a click lister to the add button to show the form and then hide the add button
        addButton.addClickListener(event -> {
            form.setVisible(true);
            addButton.setVisible(false);
        });

        // set the form's default visibility to false
        form.setVisible(false);

        // build a div element with title, carOwner grid, and editor form
        Div carOwnerContent = new Div(
            grid,
            form
        );

        carOwnerContent.addClassName("carOwnerContent");
        carOwnerContent.setSizeFull();

        // add the components to this layout
        add(new H1("Car Owners"), addButton, carOwnerContent);

        // fetch the list for the grid
        updateCarOwnerList();
    }

    /**
     * Refreshes the grid list from the database
     */
    private void updateCarOwnerList() {
        grid.setItems(carOwnerService.findAll());
    }

    /**
     * Saves the given carOwner to the database
     * @param event the SaveEvent from the carOwner editor form
     */
    private void saveCarOwner(CarOwnerEditorForm.SaveEvent event) {
        carOwnerService.save(event.getCarOwner());
        updateCarOwnerList();
        closeCarOwnerForm();
        // TODO add toast to confirm add
    }

    /**
     * Deletes the given carOwner from the database.
     * @param event the DeleteEvent from the carOwner editor form
     */
    private void deleteCarOwner(CarOwnerEditorForm.DeleteEvent event) {
        if (event.getCarOwner() != null) {
            // TODO make this run only after confirm delete dialog
            carOwnerService.delete(event.getCarOwner());
            updateCarOwnerList();
        }
        closeCarOwnerForm();
    }

    /**
     * Toggles the form visibility and sets initializes form fields if passed a CarOwner instance
     * @param carOwner the CarOwner instance to edit, or null if none is selected
     */
    private void editCarOwner(CarOwner carOwner) {
        if (carOwner == null) {
            closeCarOwnerForm();
        } else {
            form.setCarOwner(carOwner);
            form.setVisible(true);
            addClassName("editing-carOwner");
        }
    }

    /**
     * Clears and hides the editor form
     */
    private void closeCarOwnerForm() {
        form.setCarOwner(new CarOwner());
        form.setVisible(false);
        removeClassName("editing-carOwner");
        addButton.setVisible(true);
    }

    /**
     * Clears and hides the editor form
     */
    private void closeCarOwnerFormHandler(CarOwnerEditorForm.CloseEvent event) {
        if (event.getCarOwner() != null)
        {
            // TODO add confirm dialog
        }
        closeCarOwnerForm();
    }
}
