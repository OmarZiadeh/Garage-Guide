package com.TeamOne411.ui.view.garage.form;

import com.TeamOne411.backend.entity.Garage;
import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.entity.servicecatalog.ServiceCategory;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

/**
 * This class is a VerticalLayout for add/editing offered services for a garage
 */
public class GarageServiceForm extends VerticalLayout {
    private TextField serviceName = new TextField("Service name");
    private TextField price = new TextField("$ Price");
    private ComboBox<Duration> duration = new ComboBox<>("Duration");
    private ComboBox<ServiceCategory> serviceCategory = new ComboBox<>("Category");
    private Garage garage;
    private ServiceCatalogService serviceCatalogService;

    Binder<OfferedService> binder = new BeanValidationBinder<>(OfferedService.class);
    private OfferedService offeredService = new OfferedService();

    private static class PriceConverter extends StringToBigDecimalConverter {

        public PriceConverter() {
            super(BigDecimal.ZERO, "Cannot convert value to a number.");
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            // Always display currency with two decimals
            final NumberFormat format = super.getFormat(locale);
            if (format instanceof DecimalFormat) {
                format.setMaximumFractionDigits(2);
                format.setMinimumFractionDigits(2);
            }
            return format;
        }
    }

    public GarageServiceForm(Garage garage, ServiceCatalogService serviceCatalogService){
        this.garage = garage;
        this.serviceCatalogService = serviceCatalogService;

        // initial view setup
        addClassName("garage-service-form");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        binder.forField(price).withConverter(new PriceConverter()).bind("price");
        binder.bindInstanceFields(this);

        serviceCategory.setItems(serviceCatalogService.findCategoriesByGarage(garage));
        duration.setItems((Duration.ofMinutes(0)), Duration.ofMinutes(30), Duration.ofMinutes(60), Duration.ofMinutes(90), Duration.ofMinutes(120),
                Duration.ofMinutes(150), Duration.ofMinutes(180));
        add(serviceName,
                price,
                duration,
                serviceCategory);
        }
}
