package model;

import java.util.*;

/**
 *
 * @author dania
 */
public class QueueOfCustomers {
    private LinkedList<Customer> queue;

    public QueueOfCustomers() {
        queue = new LinkedList<>();
    }

    public void enqueueCustomer(Customer customer) {
        queue.addLast(customer);
    }

    public Customer dequeueCustomer() {
        return queue.pollFirst();
    }

    public Customer peekCustomer() {
        return queue.peekFirst();
    }

    @Override
    public String toString() {
        return "Queue: " + queue;
    }

    public Iterable<Customer> getAllCustomers() {
        return queue;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}