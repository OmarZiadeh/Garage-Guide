package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.schedule.BusinessHours;
import com.TeamOne411.backend.service.BusinessHoursService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

@SuppressWarnings("rawtypes")
/**
 * This dialog class manages opening and closing of the GarageBizHoursForm
 */
public class GarageBizHoursDialog extends Dialog {
    private final GarageBizHoursForm garageBizHoursForm;
    private final BusinessHoursService businessHoursService;
    private BusinessHours businessHours;

    public GarageBizHoursDialog(BusinessHoursService businessHoursService, BusinessHours businessHours) {
        this.businessHours = businessHours;
        this.businessHoursService = businessHoursService;

        garageBizHoursForm = new GarageBizHoursForm();
        garageBizHoursForm.addListener(GarageBizHoursForm.CancelEvent.class, this::onCancelClick);
        garageBizHoursForm.addListener(GarageBizHoursForm.SaveEvent.class, this::onSaveClick);
        garageBizHoursForm.prefillForm(businessHours);

        // only way to exit is to hit cancel or complete the form
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        setResizable(true);

        VerticalLayout container = new VerticalLayout();
        container.setAlignItems(FlexComponent.Alignment.CENTER);

        container.add(new H3("Edit Business Hours"), garageBizHoursForm);
        add(container);
    }

    /**
     * Fired when cancel button in child form is clicked. Closes the dialog.
     *
     * @param event event that occurred
     */
    private void onCancelClick(ComponentEvent event) {
        close();
    }


    /**
     * Fired when save button in child form is clicked. Propagates another event depending on Add or Edit mode.
     *
     * @param event event that occurred
     */
    private void onSaveClick(ComponentEvent event) {
        // get the offeredService
        businessHours = garageBizHoursForm.getBusinessHours();
        businessHoursService.saveBusinessHours(businessHours);

        fireEvent(new GarageBizHoursDialog.SaveSuccessEvent(this));
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Event to emit when business hours have been updated.
     */
    public static class SaveSuccessEvent extends ComponentEvent<GarageBizHoursDialog> {
        SaveSuccessEvent(GarageBizHoursDialog source) {
            super(source, false);
        }
    }
}
