package com.springmvc.api.controller;


import java.util.List;

import com.springmvc.api.dao.CustomerDAO;
import com.springmvc.api.model.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	@Autowired
	private CustomerDAO customerDAO;

	@GetMapping(CustomerRestURIConstants.GET_ALL_CUSTOMERS)
	public List<Customer> getCustomers() {
		return customerDAO.list();
	}

	@GetMapping(CustomerRestURIConstants.GET_CUSTOMER)
	public ResponseEntity getCustomer(@PathVariable Long id) {
		Customer customer = customerDAO.get(id);
		if (customer == null) {
			return new ResponseEntity(CustomerRestURIConstants.ERROR_MESSAGE + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(customer, HttpStatus.OK);
	}

	@PostMapping(CustomerRestURIConstants.POST_CUSTOMER)
	public ResponseEntity createCustomer(@RequestBody Customer customer) {
		customerDAO.create(customer);
		return new ResponseEntity(customer, HttpStatus.OK);
	}

	@DeleteMapping(CustomerRestURIConstants.DELETE_CUSTOMER)
	public ResponseEntity deleteCustomer(@PathVariable Long id) {
		if (null == customerDAO.delete(id)) {
			return new ResponseEntity(CustomerRestURIConstants.ERROR_MESSAGE + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(id, HttpStatus.OK);
	}

	@PutMapping(CustomerRestURIConstants.PUT_CUSTOMER)
	public ResponseEntity updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
		customer = customerDAO.update(id, customer);
		if (null == customer) {
			return new ResponseEntity(CustomerRestURIConstants.ERROR_MESSAGE + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(customer, HttpStatus.OK);
	}
}
