package model;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton class for logging system events.
 *
 * @author dania
 */
public class Log {
    private static Log instance;
    private StringBuffer logBuffer;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Log() {
        logBuffer = new StringBuffer();
    }

    /**
     * Retrieves the singleton instance of the Log.
     *
     * @return Singleton instance of Log.
     */
    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    /**
     * Logs an event with a timestamp.
     *
     * @param event The event description.
     */
    public void logEvent(String event) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        logBuffer.append("[").append(timestamp).append("] ").append(event).append("\n");
    }

    /**
     * Logs customer data.
     *
     * @param customerId Customer's unique ID.
     * @param name       Customer's name.
     */
    public void logCustomer(String customerId, String name) {
        logEvent("Customer loaded: ID=" + customerId + ", Name=" + name);
    }

    /**
     * Logs parcel data.
     *
     * @param parcelId Parcel's unique ID.
     * @param weight   Parcel's weight.
     */
    public void logParcel(String parcelId, double weight) {
        logEvent("Parcel loaded: ID=" + parcelId + ", Weight=" + weight + "kg");
    }

    /**
     * Logs processing of a parcel.
     *
     * @param parcelId Parcel's unique ID.
     * @param fee      Fee charged for the parcel.
     */
    public void logParcelProcessing(String parcelId, double fee) {
        logEvent("Parcel processed: ID=" + parcelId + ", Fee= $" + fee);
    }

    /**
     * Retrieves the log data as a string.
     *
     * @return The log data.
     */
    public String getLog() {
        return logBuffer.toString();
    }

    /**
     * Saves the log data to a file.
     *
     * @param fileName The file name to save the log data to.
     */
    public void saveLogToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(logBuffer.toString());
        } catch (IOException e) {
            System.err.println("Error saving log to file: " + e.getMessage());
        }
    }
}
