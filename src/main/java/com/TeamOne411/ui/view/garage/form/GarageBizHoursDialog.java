package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class GarageBizHoursDialog extends Dialog {
    private final GarageCategoryForm garageCategoryForm;

    /**
     * This constructor is for the dialog
     * @param serviceCatalogService the service class for the serviceCategory repository
     * @param garage    the service class for the garage repository
     */
    public GarageCategoryEditorDialog(ServiceCatalogService serviceCatalogService, Garage garage) {

        garageCategoryForm = new GarageCategoryForm(serviceCatalogService, garage);
        garageCategoryForm.addListener(GarageCategoryForm.ExitEvent.class, this::onExitClick);

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        setResizable(true);

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);

        container.add(new H3("Edit Service Categories"), garageCategoryForm);
        add(container);
    }

    /**
     * Event to emit when the GarageCategoryEditorForm has been exited.
     */
    public static class EditCategoriesExitEvent extends ComponentEvent<GarageCategoryEditorDialog> {
        EditCategoriesExitEvent(GarageCategoryEditorDialog source) {
            super(source, false);
        }
    }

    /**
     * Fired when exit button in child form is clicked.
     */
    private void onExitClick(ComponentEvent event) {
        fireEvent(new GarageCategoryEditorDialog.EditCategoriesExitEvent(this));
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}