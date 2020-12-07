package com.TeamOne411.ui.view.carowner.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.schedule.Appointment;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.AppointmentService;
import com.TeamOne411.backend.service.GarageCalendarService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.TeamOne411.ui.utils.FormattingUtils;
import com.TeamOne411.ui.utils.LocalDateConverter;
import com.TeamOne411.ui.utils.LocalTimeConverter;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Set;


public class AppointmentForm extends VerticalLayout {
    private final ServiceCatalogService serviceCatalogService;
    private final GarageCalendarService garageCalendarService;
    private final AppointmentService appointmentService;
    private final Appointment appointment = new Appointment();
    private final Text confirmPrice = new Text("");
    private final TextArea carOwnerComments = new TextArea("Is there anything else you'd like us to know?");
    private final Button saveButton = new Button("Book Appointment");
    private final ComboBox<Garage> garage = new ComboBox<>("Select Garage");
    private final Grid<OfferedService> offeredServicesGrid = new Grid<>(OfferedService.class);
    private final ComboBox<LocalDate> appointmentDate = new ComboBox<>("Select Appointment Date");
    private final ComboBox<LocalTime> appointmentTime = new ComboBox<>("Select Appointment Time");
    private final Checkbox confirmationCheckbox = new Checkbox("Check Here If Everything Looks Good");
    private final Text confirmGarage = new Text("");
    private final Text confirmDateTime = new Text("");
    private final LocalTimeConverter localTimeConverter = new LocalTimeConverter();
    private final LocalDateConverter localDateConverter = new LocalDateConverter();
    private Duration estimatedDuration;
    private BigDecimal estimatedTotalPrice;

    public AppointmentForm(ServiceCatalogService serviceCatalogService, GarageCalendarService garageCalendarService,
                           AppointmentService appointmentService) {
        this.serviceCatalogService = serviceCatalogService;
        this.garageCalendarService = garageCalendarService;
        this.appointmentService = appointmentService;

        addClassName("appointment-form");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        Accordion accordion = new Accordion();
        accordion.setWidth("100%");
        saveButton.setEnabled(false);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        // VEHICLE AND GARAGE INFO
        FormLayout garageForm = new FormLayout();
        TextField vehicle = new TextField("Vehicle");
        vehicle.setPlaceholder("Placeholder");
        garageForm.add(vehicle, garage);
        garage.setItemLabelGenerator(Garage::getCompanyName);
        garage.setItems(garageCalendarService.findAllByGarageExists());
        setRequiredComboBoxValues(garage);
        accordion.add("Vehicle and Garage Information", garageForm);

        // OFFERED SERVICES
        offeredServicesGrid.setVisible(false);
        offeredServicesGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        accordion.add("Services Required", offeredServicesGrid);

        // APPOINTMENT DATE AND TIME
        FormLayout appointmentTimeForm = new FormLayout();
        appointmentTimeForm.add(appointmentDate, appointmentTime);
        setRequiredComboBoxValues(appointmentDate);
        setRequiredComboBoxValues(appointmentTime);
        appointmentDate.setEnabled(false);
        appointmentTime.setEnabled(false);
        accordion.add("Appointment Date and Time", appointmentTimeForm);

        // CONFIRM DETAILS
        VerticalLayout confirmForm = new VerticalLayout();
        confirmForm.setJustifyContentMode(JustifyContentMode.CENTER);
        confirmForm.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        confirmForm.setWidth("100%");
        confirmForm.add(
                new H4("Please Confirm Your Appointment Information"),
                new H5(confirmGarage),
                new H5(confirmPrice),
                new H5(confirmDateTime),
                confirmationCheckbox,
                carOwnerComments);
        carOwnerComments.setWidth("50%");
        carOwnerComments.setPlaceholder("Add Your Comments Here");
        accordion.add("Confirm Appointment", confirmForm);

        // CLICK LISTENERS
        garage.addValueChangeListener(e -> {
            setOfferedServicesGrid();
            setAppointmentDate();
            confirmGarage.setText("Selected Garage: " + garage.getValue().getCompanyName());
        });
        offeredServicesGrid.asMultiSelect().addValueChangeListener(e -> totalPrice());
        appointmentDate.addValueChangeListener(e -> setAppointmentTime());
        appointmentTime.addValueChangeListener(e ->
                confirmDateTime.setText("Appointment Date & Time: " + FormattingUtils.convertDate(appointmentDate.getValue()) +
                        "  " + FormattingUtils.convertTime(appointmentTime.getValue())));
        confirmationCheckbox.addValueChangeListener(e -> {
            if (confirmationCheckbox.getValue()) {
                checkFormCompletion();
            } else {
                saveButton.setEnabled(false);
            }
        });
        saveButton.addClickListener(e -> fireEvent(new AppointmentForm.SaveEvent(this)));
        cancelButton.addClickListener(e -> fireEvent(new AppointmentForm.CancelEvent(this)));

        // add fields to the form
        add(accordion, new HorizontalLayout(cancelButton, saveButton));
    }

    /**
     * Sets the required and required indicated values for a combobox
     */
    private void setRequiredComboBoxValues(ComboBox comboBox){
        comboBox.setRequired(true);
        comboBox.setRequiredIndicatorVisible(true);
    }

    /**
     * Sets the offeredServicesGrid contents once a garage has been selected
     */
    private void setOfferedServicesGrid() {
        offeredServicesGrid.setItems(serviceCatalogService.findByServiceCategory_Garage(garage.getValue()));
        offeredServicesGrid.setVisible(true);
        offeredServicesGrid.removeAllColumns();
        offeredServicesGrid.addColumn(offeredService -> {
            ServiceCategory serviceCategory = offeredService.getServiceCategory();
            return serviceCategory.getCategoryName();
        }).setSortable(true).setHeader("Category").setKey("serviceCategory");
        offeredServicesGrid.addColumn(OfferedService::getServiceName).setKey("serviceName").setHeader("Service");
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        offeredServicesGrid.addColumn(offeredService -> decimalFormat.format(offeredService.getPrice()))
                .setHeader("Price").setComparator(Comparator.comparing(OfferedService::getPrice))
                .setKey("price").setSortable(false);
    }

    /**
     * Totals the price for the owner to view on the confirm form
     */
    private void totalPrice() {
        Set<OfferedService> offeredServiceSet = offeredServicesGrid.asMultiSelect().getSelectedItems();
        BigDecimal calcTotal = new BigDecimal(0);
        Duration durationTotal = Duration.ZERO;
        for (OfferedService os : offeredServiceSet) {
            calcTotal = calcTotal.add(os.getPrice());
            durationTotal = durationTotal.plus(os.getDuration());
        }
        confirmPrice.setText("Estimated Total: $" + calcTotal.toString());
        estimatedTotalPrice = calcTotal;
        estimatedDuration = durationTotal;
    }

    /**
     * Gets the set of selected services
     *
     * @return Set of selected services
     */
    public Set<OfferedService> getSelectedServices() {
        return offeredServicesGrid.getSelectedItems();
    }

    /**
     * Checks that the appointment form is complete before enabling the save button
     * User receives a notification if the form is not complete
     */
    private void checkFormCompletion() {
        if (garage.getValue() != null && appointmentDate.getValue() != null && appointmentTime.getValue() != null
                && !offeredServicesGrid.asMultiSelect().isEmpty()) {
            saveButton.setEnabled(true);
        } else {
            String message = "";
            if (garage.getValue() == null)
                message = "a garage";
            else if (appointmentDate.getValue() == null)
                message = "an appointment Date and Time";
            else if (appointmentTime.getValue() == null)
                message = "an appointmentTime";
            else if (offeredServicesGrid.asMultiSelect().isEmpty()) {
                message = "a service required";
            }
            Notification notification = new Notification(
                    "Please select " + message + " to complete your appointment request",
                    3000, Notification.Position.TOP_CENTER);
            NativeButton buttonInside = new NativeButton("Close");
            buttonInside.addClickListener(event -> notification.close());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        }
    }

    /**
     * Sets the appointmentDate combobox once the user has selected a garage
     */
    private void setAppointmentDate() {
        appointmentDate.setEnabled(true);
        appointmentDate.setItems(garageCalendarService.findStartDatesByGarage(garage.getValue()));
        appointmentDate.setItemLabelGenerator(localDateConverter::encode);
    }

    /**
     * Sets the appointmentTime combobox once the user has selected an appointmentDate
     */
    private void setAppointmentTime() {
        // set the time slots once the user has picked a desired appointment date
        appointmentTime.setEnabled(true);
        appointmentTime.setItems(garageCalendarService
                .findStartTimesByGarageAndDate(garage.getValue(), appointmentDate.getValue()));
        appointmentTime.setItemLabelGenerator(localTimeConverter::encode);
    }

    /**
     * Saves the appointment and starts async background tasks to create appointment tasks and fill slots
     */
    public void completeAppointment() {
        //set appointment values
        appointment.setAppointmentDate(appointmentDate.getValue());
        appointment.setAppointmentTime(appointmentTime.getValue());
        appointment.setGarage(garage.getValue());
        appointment.setCarOwnerComments(carOwnerComments.getValue());
        appointment.setEstimatedDuration(estimatedDuration);
        appointment.setEstimatedTotalPrice(estimatedTotalPrice);

        // save the appointment to the db
        appointmentService.saveAppointment(appointment);

        // create the appointment tasks via async background process & fill time slots
        appointmentService.createAppointmentTasks(appointment, offeredServicesGrid.getSelectedItems());
        garageCalendarService.fillTimeSlots(appointment);
    }

    @Override
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    /**
     * Event to emit when save button is clicked
     */
    public static class SaveEvent extends ComponentEvent<AppointmentForm> {
        SaveEvent(AppointmentForm source) {
            super(source, false);
        }
    }

    /**
     * Event to emit when cancel button is clicked
     */
    public static class CancelEvent extends ComponentEvent<AppointmentForm> {
        CancelEvent(AppointmentForm source) {
            super(source, false);
        }
    }
}
