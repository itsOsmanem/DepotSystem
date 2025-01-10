package model;

import java.util.*;


/**
 *
 * @author dania
 */
public class Customer {
    private String id;
    private String name;
    private List<Parcel> parcels;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
        this.parcels = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Parcel> getParcels() {
        return parcels;
    }

    public void addParcel(Parcel parcel) {
        parcels.add(parcel);
    }

    @Override
    public String toString() {
        return "Customer ID: " + id + ", Name: " + name;
    }
}