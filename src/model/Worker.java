package model;

/**
 *
 * @author dania
 */
public class Worker {
    public void processParcel(Customer customer) {
        if (customer != null && !customer.getParcels().isEmpty()) {
            Parcel parcel = customer.getParcels().remove(0); 
            double fee = calculateFee(parcel);
            parcel.markAsProcessed();
            Log.getInstance().logEvent("Processed parcel " + parcel.getId() + " for customer " + customer.getName() + ". Fee: $" + fee);
        }
    }

   public double calculateFee(Parcel parcel) {
    double baseFee = parcel.getWeight() * 2.0; 
    double storageFee = parcel.getDaysInDepot() * 1.5; 
    return baseFee + storageFee;
}

}