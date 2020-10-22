package com.TeamOne411.ui.view.sandbox.childview;

import com.TeamOne411.backend.entity.servicecatalog.OfferedService;
import com.TeamOne411.backend.service.ServiceCatalogService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;

import java.text.DecimalFormat;
import java.util.Comparator;

public class ServiceCatalogSandboxView extends Grid<OfferedService> {
    private ServiceCatalogService service;

    public ServiceCatalogSandboxView(ServiceCatalogService serviceCatalogService) {

        this.service = serviceCatalogService;
        setSizeFull();

        addColumn(OfferedService::getServiceName).setHeader("Service name")
                .setFlexGrow(20).setSortable(true).setKey("servicename");

        // Format and add " $" to price
        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        addColumn(offeredService -> decimalFormat.format(offeredService.getPrice()) + " $")
                .setHeader("Price").setTextAlign(ColumnTextAlign.END)
                .setComparator(Comparator.comparing(OfferedService::getPrice))
                .setFlexGrow(3).setKey("price");

        // Show all categories the product is in, separated by commas


        //TODO need to fix this
/*        addColumn(offeredService -> formatCategories).setHeader("Category").setFlexGrow(12)
                .setKey("category");
*/
        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(
                e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("productname").setVisible(true);
            getColumnByKey("price").setVisible(true);
 //           getColumnByKey("category").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("productname").setVisible(true);
            getColumnByKey("price").setVisible(true);
  //          getColumnByKey("category").setVisible(true);
        } else {
            getColumnByKey("productname").setVisible(true);
            getColumnByKey("price").setVisible(true);
  //          getColumnByKey("category").setVisible(false);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // fetch browser width
        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> {
            setColumnVisibility(e.getBodyClientWidth());
        });
    }

    public OfferedService getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(OfferedService offeredService) {
        getDataCommunicator().refresh(offeredService);
    }

    private String formatCategories(OfferedService offeredService) {
        if (offeredService.getServiceCategory() == null) {
            return "";
        }
        return offeredService.getServiceCategory().toString();
    }
}