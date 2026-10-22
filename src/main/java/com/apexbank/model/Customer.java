package com.apexbank.model;

import java.sql.Date;

/**
 * Customer entity extending abstract User.
 * Demonstrates OOP Inheritance, Encapsulation, Constructor Overloading, and Method Overriding.
 */
public class Customer extends User {
    private static final long serialVersionUID = 1L;

    private int customerId;
    private String firstName;
    private String lastName;
    private Date dob;
    private String gender;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String idProofType;
    private String idProofNumber;

    // Default constructor
    public Customer() {
        super();
    }

    // Constructor Overloading (1): Minimal registration
    public Customer(String firstName, String lastName, String email, String phone) {
        super(email, phone);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Constructor Overloading (2): Full profile creation
    public Customer(int customerId, String firstName, String lastName, String email, String phone,
                    Date dob, String gender, String address, String city, String state, 
                    String pincode, String idProofType, String idProofNumber) {
        super(email, phone);
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.idProofType = idProofType;
        this.idProofNumber = idProofNumber;
    }

    // Method Overriding from User
    @Override
    public String getDisplayName() {
        return getFullName();
    }

    @Override
    public String getRoleName() {
        return "CUSTOMER";
    }

    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "").trim();
    }

    // Getters and Setters (Encapsulation)
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getIdProofType() {
        return idProofType;
    }

    public void setIdProofType(String idProofType) {
        this.idProofType = idProofType;
    }

    public String getIdProofNumber() {
        return idProofNumber;
    }

    public void setIdProofNumber(String idProofNumber) {
        this.idProofNumber = idProofNumber;
    }
}
