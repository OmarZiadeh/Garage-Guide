package com.TeamOne411.ui.view.sandbox.form;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.value.ValueChangeMode;

public class ServiceCatalogEditorForm extends Div {

    private final VerticalLayout content;

    private TextField serviceName;
    private TextField serviceDescription;
    private TextField price;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final Binder<OfferedService> binder;
    private OfferedService currentService;

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

    public ServiceCatalogEditorForm() {
        setClassName("service-catalog-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("service-catalog-form-content");
        add(content);

        serviceName = new TextField("Service name");
        serviceName.setWidth("100%");
        serviceName.setRequired(true);
        serviceName.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(serviceName);

        serviceDescription = new TextField("Service description");
        serviceDescription.setWidth("100%");
        serviceDescription.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(serviceDescription);

        price = new TextField("Price");
        price.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        price.setValueChangeMode(ValueChangeMode.EAGER);

        binder = new BeanValidationBinder<>(OfferedService.class);
        binder.forField(price).withConverter(new PriceConverter())
                .bind("price");
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            final boolean isValid = !event.hasValidationErrors();
            final boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save = new Button("Save");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
     /*   save.addClickListener(event -> {
            if (currentProduct != null
                    && binder.writeBeanIfValid(currentProduct)) {
                viewLogic.saveProduct(currentProduct);
            }
        });  */
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
      /*  discard.addClickListener(
                event -> viewLogic.editProduct(currentProduct));
*/
        cancel = new Button("Cancel");
        cancel.setWidth("100%");
  /*      cancel.addClickListener(event -> viewLogic.cancelProduct()); */
        cancel.addClickShortcut(Key.ESCAPE);
     /*   getElement()
                .addEventListener("keydown", event -> viewLogic.cancelProduct())
                .setFilter("event.key == 'Escape'");
*/
        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
  /*      delete.addClickListener(event -> {
            if (currentProduct != null) {
                viewLogic.deleteProduct(currentProduct);
            }
        });
*/
        content.add(save, discard, delete, cancel);
    }

    public void editProduct(OfferedService offeredService) {
        if (offeredService == null) {
            offeredService = new OfferedService();
        }
      /*  delete.setVisible(!offeredService.isNewProduct());
        currentProduct = product;
        binder.readBean(product);

       */
    }
}
