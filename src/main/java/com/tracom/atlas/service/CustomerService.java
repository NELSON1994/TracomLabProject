package com.tracom.atlas.service;

import com.tracom.atlas.entity.Customers;
import com.tracom.atlas.repository.CustomersRepository;
import ke.axle.chassis.wrappers.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomersRepository customersRepository;

    public ResponseWrapper updateCustomers(Customers t, Long id) {
        Customers customers = new Customers();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        t = customersRepository.findById(id).get();
        customers.setContact_person(t.getContact_person());
        customers.setEmail(t.getEmail());
        customers.setPhone_number(t.getPhone_number());
        customers.setAddress(t.getAddress());
        customers.setName(t.getName());

        customersRepository.save(customers);
        responseWrapper.setCode(HttpStatus.OK.value());
        responseWrapper.setTimestamp(Calendar.getInstance().getTimeInMillis());
        responseWrapper.setData(t);
        return responseWrapper;
    }

    public Optional<List<Customers>> findStatus(String status){
         return customersRepository.findCustomersByActionStatus(status);
    }

    public Optional<List<Customers>> findIntrash(String intrash){
        return customersRepository.findCustomersByIntrash(intrash);
    }

    public Optional<List<Customers>> findCustomersBetweenDate(Date from , Date to){
        return customersRepository.findCustomersByCreationDateBetween(from , to);
    }

}
