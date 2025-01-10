package main;

import controller.DepotController;
import view.DepotView;
import model.ParcelMap;
import model.QueueOfCustomers;

/**
 *
 * @author dania
 */
public class Manager {

    public static void main(String[] args) {
     QueueOfCustomers customerQueue = new QueueOfCustomers();
    ParcelMap parcelMap = new ParcelMap();
    DepotView view = new DepotView();
    DepotController controller = new DepotController(customerQueue, parcelMap, view);

    controller.loadCustomersFromFile("src/data/Custs.csv");
    controller.loadParcelsFromFile("src/data/Parcels.csv");

}}