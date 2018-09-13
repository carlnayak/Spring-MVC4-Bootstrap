package com.springmvc.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springmvc.api.controller.CustomerRestURIConstants;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable {
    private static final long serialVersionUID = -7788619177798333712L;

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private Date dateOfBirth;

    public Customer() { }

    public Customer(long id, String firstName, String lastName, String email, String mobile, Date dob) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setMobile(mobile);
        this.setDateOfBirth(dob);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CustomerRestURIConstants.DATE_FORMAT)
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id : " + Long.toString(getId()));
        sb.append("\n First Name : " + getFirstName());
        sb.append("\n Last Name : " + getLastName());
        sb.append("\n Email : " + getEmail());
        sb.append("\n Mobile : " + getMobile());
        sb.append("\n Date Of Birth : " + getDateOfBirth().toString());
        sb.append("\n");
        return sb.toString();
    }
}