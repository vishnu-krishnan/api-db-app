package com.study.projects;

import org.apache.camel.Body;
import org.apache.camel.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMapper {

    public Map<String, Object> putCustomerDetails(@Body Map<String, Object> body) {

        Map<String, Object> customerDetails = new HashMap<String, Object>();
        Customer customer = new Customer();

        customer.setCustomerId((Integer) body.get("customerId"));
        customer.setCustomerName((String) body.get("customerName"));

        customerDetails.put("customerId", customer.getCustomerId());
        customerDetails.put("customerName", customer.getCustomerName());
        return customerDetails;
    }

    public List<Customer> getCustomerDetails(List<Map<String,Object>> getList){

        List<Customer> customerList = new ArrayList<Customer>();

        for (Map<String,Object> data: getList) {
            Customer customer = new Customer();
            customer.setCustomerId((Integer) data.get("id"));
            customer.setCustomerName((String) data.get("name"));
            customerList.add(customer);
        }
        return customerList;
    }

    public Map<String, Object> updateCustomerDetails(@Body Map<String, Object> body) {

        Map<String, Object> customerDetails = new HashMap<String, Object>();
        Customer customer = new Customer();

        customer.setCustomerId((Integer) body.get("customerId"));
        customer.setCustomerName((String) body.get("customerName"));

        customerDetails.put("customerId", customer.getCustomerId());
        customerDetails.put("customerName", customer.getCustomerName());

        System.out.println(" customer details");
        return customerDetails;
    }
}
