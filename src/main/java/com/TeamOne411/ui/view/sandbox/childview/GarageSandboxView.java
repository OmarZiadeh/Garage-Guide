package com.TeamOne411.ui.view.sandbox.childview;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.ui.view.sandbox.form.GarageEditorForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * The GarageSandboxView is a God-mode Garage editor for testing purposes.
 * This should either be deleted as the application evolves or turned into an Admin screen meant only for technical support, well hidden behind authentication.
 */
public class GarageSandboxView extends VerticalLayout {
    private Grid<Garage> grid = new Grid<>(Garage.class);
    private GarageService service;
    private GarageEditorForm form = new GarageEditorForm();
    private Button addButton = new Button("Add Garage");

    /**
     * The constructor for the sandbox view. Does initial layout setup, grid configuration, and event listener attachment
     * @param service the GarageService to broker the repository calls
     */
    public GarageSandboxView(GarageService service) {
        // initial layout setup
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        // configure the garage grid
        grid.addClassName("garage-grid");
        grid.setHeightByRows(true);
        grid.setMaxHeight("25vh");
        grid.setColumns("companyName", "phoneNumber", "address");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // attach event listener on grid item select
        grid.asSingleSelect().addValueChangeListener(event -> editGarage(event.getValue()));

        // connect the handlers to the form events
        form.addListener(GarageEditorForm.SaveEvent.class, this::saveGarage);
        form.addListener(GarageEditorForm.DeleteEvent.class, this::deleteGarage);
        form.addListener(GarageEditorForm.CloseEvent.class, this::closeGarageFormHandler);

        addButton.addClickListener(event -> {
            form.setVisible(true);
            addButton.setVisible(false);
        });

        // set the form's default visibility to false
        form.setVisible(false);

        // build a div element with title, garage grid, and editor form
        Div garageContent = new Div(
            grid,
            form
        );

        garageContent.addClassName("garageContent");
        garageContent.setSizeFull();

        // add the components to this layout
        add(new H1("Garages"), addButton, garageContent);

        // fetch the list for the grid
        updateGarageList();
    }

    /**
     * Refreshes the grid list from the database
     */
    private void updateGarageList() {
        grid.setItems(service.findAll());
    }

    /**
     * Saves the given garage to the database
     * @param event the SaveEvent from the garage editor form
     */
    private void saveGarage(GarageEditorForm.SaveEvent event) {
        service.save(event.getGarage());
        updateGarageList();
        closeGarageForm();
        // TODO add toast to confirm add
    }

    /**
     * Deletes the given garage from the database.
     * @param event the DeleteEvent from the garage editor form
     */
    private void deleteGarage(GarageEditorForm.DeleteEvent event) {
        if (event.getGarage() != null) {
            // TODO make this run only after confirm delete dialog
            service.delete(event.getGarage());
            updateGarageList();
        }
        closeGarageForm();
    }

    /**
     * Toggles the form visibility and sets initializes form fields if passed a Garage instance
     * @param garage the Garage instance to edit, or null if none is selected
     */
    private void editGarage(Garage garage) {
        if (garage == null) {
            closeGarageForm();
        } else {
            form.setGarage(garage);
            form.setVisible(true);
            addClassName("editing-garage");
        }
    }

    /**
     * Clears and hides the editor form
     */
    private void closeGarageForm() {
        form.setGarage(new Garage());
        form.setVisible(false);
        removeClassName("editing-garage");
        addButton.setVisible(true);
    }

    /**
     * Clears and hides the editor form
     */
    private void closeGarageFormHandler(GarageEditorForm.CloseEvent event) {
        if (event.getGarage() != null)
        {
            // TODO add confirm dialog
        }
        closeGarageForm();
    }
}
