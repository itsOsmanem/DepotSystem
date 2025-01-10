package model;

import java.util.*;

/**
 *
 * @author dania
 */

public class ParcelMap {
    private Map<String, Parcel> parcels;

    public ParcelMap() {
        parcels = new HashMap<>();
    }

    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getId(), parcel);
    }

    public Parcel getParcel(String parcelId) {
        return parcels.get(parcelId);
    }

    public Iterable<Parcel> getAllParcels() {
        return parcels.values();
    }

    public boolean isEmpty() {
        return parcels.isEmpty();
    }
}