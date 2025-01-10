package controller;

import view.DepotView;
import model.Customer;
import model.Log;
import model.Parcel;
import model.ParcelMap;
import model.QueueOfCustomers;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

/**
 *
 * @author dania
 */
public class DepotController {
    private QueueOfCustomers customerQueue;
    private ParcelMap parcelMap;
    private DepotView view;

    public DepotController(QueueOfCustomers customerQueue, ParcelMap parcelMap, DepotView view) {
        this.customerQueue = customerQueue;
        this.parcelMap = parcelMap;
        this.view = view;
        initActions();
    }

   public void loadCustomersFromFile(String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        br.readLine(); 
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");

            String name = values[0].trim();
            String id = values[1].trim();
            

            Customer customer = new Customer(id, name); 
            customerQueue.enqueueCustomer(customer);
            view.getCustomerArea().append(customer + "\n");
        }
    } catch (IOException e) {
        System.out.println("Error loading customers: " + e.getMessage());
    }
}

    public void loadParcelsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Parcel parcel = new Parcel(values[0], Double.parseDouble(values[1]), values[2], Integer.parseInt(values[3]));
                parcelMap.addParcel(parcel); 
                view.getParcelListModel().addElement(parcel.toString()); 
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Error loading parcels: " + e.getMessage());
        }
    }

    private void initActions() {
        view.getProcessButton().addActionListener(e -> processNextParcel());
        view.getAddCustomerButton().addActionListener(e -> addCustomer());
        view.getAddParcelButton().addActionListener(e -> addParcel());
        view.getClearProcessedButton().addActionListener(e -> clearProcessedParcels());
        view.getGenerateReportButton().addActionListener(e -> generateReport());
        view.getCalculateFeeButton().addActionListener(e -> calculateFee());
    }

    private void processNextParcel() {
    if (customerQueue.isEmpty()) {
        JOptionPane.showMessageDialog(view, "No customers in the queue.");
        return;
    }

    Customer customer = customerQueue.peekCustomer();

    if (customer.getParcels().isEmpty()) {
        JOptionPane.showMessageDialog(view, "No parcels to process for customer: " + customer.getId());
        customerQueue.dequeueCustomer(); 
        updateCustomerArea();
        return;
    }

    String parcelId = JOptionPane.showInputDialog(view, "Enter Parcel ID to process:");
    if (parcelId == null || parcelId.trim().isEmpty()) {
        JOptionPane.showMessageDialog(view, "Parcel ID cannot be empty.");
        return;
    }

    Parcel selectedParcel = null;
    for (Parcel parcel : customer.getParcels()) {
        if (parcel.getId().equalsIgnoreCase(parcelId)) {
            selectedParcel = parcel;
            break;
        }
    }

    if (selectedParcel == null) {
        JOptionPane.showMessageDialog(view, "Parcel ID not found for customer: " + customer.getId());
        return;
    }

    double fee = calculateParcelFee(selectedParcel);
    selectedParcel.markAsProcessed(); 
    customer.getParcels().remove(selectedParcel);

    view.getProcessingArea().setText("Processing Parcel:\n" + selectedParcel + "\nFee: $" + fee);

    Log.getInstance().logEvent("Processed parcel " + selectedParcel.getId() + " for customer " + customer.getName() + ". Fee: $" + fee);

    if (customer.getParcels().isEmpty()) {
        customerQueue.dequeueCustomer();
    }

    updateCustomerArea();
    updateParcelArea();
}

    private void addCustomer() {
        String customerName = JOptionPane.showInputDialog(view, "Enter Customer Name:");
        if (customerName != null && !customerName.trim().isEmpty()) {
            Customer customer = new Customer("C" + (customerQueue.size() + 1), customerName);
            customerQueue.enqueueCustomer(customer);
            view.getCustomerArea().append(customer + "\n");
        }
    }

    private void addParcel() {
        String parcelId = JOptionPane.showInputDialog(view, "Enter Parcel ID:");
        if (parcelId != null && !parcelId.trim().isEmpty()) {
            Parcel parcel = new Parcel(parcelId, 2.5, "10x10x10", 3); 
            parcelMap.addParcel(parcel); 
            view.getParcelListModel().addElement(parcel.toString()); 
            JOptionPane.showMessageDialog(view, "Parcel added successfully!");
        } else {
            JOptionPane.showMessageDialog(view, "Parcel ID cannot be empty.");
        }
    }

    private void clearProcessedParcels() {
        view.getParcelListModel().clear(); 
        JOptionPane.showMessageDialog(view, "All processed parcels have been cleared.");
    }

    private void generateReport() {
        try {
            String filePath = "DepotReport.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Write customer data
                writer.write("Depot Management System - Processed Parcels Report\n");

                writer.write("Customers:\n");
                for (Customer customer : customerQueue.getAllCustomers()) {
                    writer.write(customer.toString() + "\n");
                }

                // Write parcel data
                writer.write("\nParcels:\n");
                for (Parcel parcel : parcelMap.getAllParcels()) {
                    writer.write(parcel.toString() + "\n");
                }

                // Write logs
                writer.write("\nActivity Logs:\n");
                writer.write(Log.getInstance().getLog());
            }

            JOptionPane.showMessageDialog(view, "Report generated successfully!\nSaved to: " + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "Error generating report: " + e.getMessage());
        }
    }


    private void calculateFee() {
        if (view.getParcelListModel().isEmpty()) {
            JOptionPane.showMessageDialog(view, "No parcels available for fee calculation.");
            return;
        }

        String selectedParcel = view.getParcelList().getSelectedValue(); 
        if (selectedParcel != null) {
            String parcelId = selectedParcel.split(",")[0].split(":")[1].trim(); 
            Parcel parcel = parcelMap.getParcel(parcelId); 
            if (parcel != null) {
                double fee = calculateParcelFee(parcel);
                JOptionPane.showMessageDialog(view, "Fee for parcel " + parcel.getId() + ": $" + fee);
            } else {
                JOptionPane.showMessageDialog(view, "Parcel not found in the system.");
            }
        } else {
            JOptionPane.showMessageDialog(view, "No parcel selected for fee calculation.");
        }
    }

    private double calculateParcelFee(Parcel parcel) {
        double baseFee = parcel.getWeight() * 2.0; 
        double storageFee = parcel.getDaysInDepot() * 1.5; 
        return baseFee + storageFee;
    }

    private void updateCustomerArea() {
        view.getCustomerArea().setText(""); 
        for (Customer customer : customerQueue.getAllCustomers()) {
            view.getCustomerArea().append(customer + "\n");
        }
    }

    private void updateParcelArea() {
        view.getParcelListModel().clear(); 
        for (Parcel parcel : parcelMap.getAllParcels()) {
            view.getParcelListModel().addElement(parcel.toString());
        }
    }
}

