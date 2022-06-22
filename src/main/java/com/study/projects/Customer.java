package com.study.projects;


public class Customer {

    private String customerName;
    private int customerId;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "{" +
                "customerName='" + customerName + '\'' +
                ", customerId=" + customerId +
                '}';
    }
}
