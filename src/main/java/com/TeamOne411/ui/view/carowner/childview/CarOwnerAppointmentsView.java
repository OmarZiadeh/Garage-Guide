package com.TeamOne411.ui.view.carowner.childview;

import com.TeamOne411.backend.service.AppointmentService;
import com.TeamOne411.backend.service.GarageCalendarService;
import com.TeamOne411.backend.service.GarageService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.view.carowner.form.AppointmentDialog;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CarOwnerAppointmentsView extends VerticalLayout {
    private final AppointmentService appointmentService;
    private final Button newAppointment = new Button ("Schedule New Appointment");
    private AppointmentDialog appointmentDialog;

    public CarOwnerAppointmentsView(AppointmentService appointmentService, GarageService garageService,
                                    ServiceCatalogService serviceCatalogService,
                                    GarageCalendarService garageCalendarService) {
        this.appointmentService = appointmentService;

        newAppointment.addClickListener(e -> showAppointmentDialog(garageService, serviceCatalogService,
                garageCalendarService));

        // create the layout for the upcoming appointments
        VerticalLayout upcomingAppointments = new VerticalLayout(
                new H4("Upcoming Appointments"),
                newAppointment
        );
        upcomingAppointments.setClassName("upcoming-appointments");
        setLayoutAttributes(upcomingAppointments);

        // create the layout for the past appointments
        VerticalLayout pastAppointments = new VerticalLayout(
                new H4("Past Appointments")
        );
        pastAppointments.setClassName("past-appointments");
        setLayoutAttributes(pastAppointments);

        HorizontalLayout combinedLayout = new HorizontalLayout(upcomingAppointments, pastAppointments);
        combinedLayout.setSizeFull();

        // add the upcoming and past appointments to the parent layout
        add(newAppointment, combinedLayout);
    }
    /**
     * Sets common attributes for the layouts
     */
    private void setLayoutAttributes(VerticalLayout verticalLayout){
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.getStyle().set("border", "1px solid #9E9E9E");
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setWidth("40%");
    }

    /**
     * Opens the appointment dialog
     */
    private void showAppointmentDialog(GarageService garageService, ServiceCatalogService serviceCatalogService,
                                       GarageCalendarService garageCalendarService) {
        appointmentDialog = new AppointmentDialog(appointmentService, garageService, serviceCatalogService,
                garageCalendarService);
        appointmentDialog.setWidth("50%");
        appointmentDialog.setHeightFull();
        appointmentDialog.addListener(AppointmentDialog.SaveSuccessEvent.class,
                this::onSave);
        appointmentDialog.open();
    }

    /**
     * Fired when the AppointmentForm has been exited.
     * @param event the event that fired this method
     */
    private void onSave(ComponentEvent<AppointmentDialog> event) {
        appointmentDialog.close();
        //updateBusinessHoursList();
    }
}
