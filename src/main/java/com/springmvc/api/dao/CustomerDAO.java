package com.springmvc.api.dao;

import com.springmvc.api.model.Customer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class CustomerDAO {

    // Dummy database. Initialize with some dummy values.
    private static List<Customer> customers = new ArrayList<Customer>() {
        { add(new Customer(101, "John", "Doe", "djohn@gmail.com", "121-232-3435", new GregorianCalendar(1990, Calendar.DECEMBER, 15).getTime())); }
        { add(new Customer(201, "Russ", "Smith", "sruss@gmail.com", "343-545-2345", new GregorianCalendar(2000, Calendar.FEBRUARY, 27).getTime())); }
        { add(new Customer(301, "Kate", "Williams", "kwilliams@gmail.com", "876-237-2987", new GregorianCalendar(2010, Calendar.JULY, 30).getTime())); }
    };

    /**
     * Returns list of customers from dummy database.
     *
     * @return list of customers
     */
    public List<Customer> list() {
        return customers;
    }

    /**
     * Return customer object for given id from dummy database. If customer is
     * not found for id, returns null.
     *
     * @param id
     *            customer id
     * @return customer object for given id
     */
    public Customer get(Long id) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Create new customer in dummy database.
     *
     * @param customer
     *            Customer object
     * @return customer object
     */
    public Customer create(Customer customer) {
        customers.add(customer);
        return customer;
    }

    /**
     * Delete the customer object from dummy database. If customer not found for
     * given id, returns null.
     *
     * @param id
     *            the customer id
     * @return id of deleted customer object
     */
    public Long delete(Long id) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) {
                customers.remove(c);
                return id;
            }
        }
        return null;
    }

    /**
     * Update the customer object for given id in dummy database. If customer
     * not exists, returns null
     *
     * @param id
     * @param customer
     * @return customer object with id
     */
    public Customer update(Long id, Customer customer) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) {
                customer.setId(c.getId());
                customers.remove(c);
                customers.add(customer);
                return customer;
            }
        }
        return null;
    }
}
