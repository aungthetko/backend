package com.demo.springjwt.service;

import com.demo.springjwt.exception.UserNotFoundException;
import com.demo.springjwt.modal.Employee;
import com.demo.springjwt.repo.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    public Employee addEmployee(Employee employee){
        employee.setEmployeeCode(UUID.randomUUID().toString());
        return employeeRepo.save(employee);
    }

    public List<Employee> findAllEmployee(){
        return employeeRepo.findAll();
    }

    public Employee updateEmployee(Employee employee){
        return employeeRepo.save(employee);
    }

    public Employee getEmployeeById (Long id){
        return employeeRepo.findEmployeeById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id : " + id + " not found"));
    }

    public void deleteEmployee(Long id){
        employeeRepo.deleteById(id);
    }

    public void generateEmployee(){
        Employee employee = new Employee();
        String firstName = RandomStringUtils.randomAlphabetic(4);
        employee.setFirstName(firstName.substring(0, 1).toUpperCase() + firstName.substring(1));
        String lastName = RandomStringUtils.randomAlphabetic(4);
        employee.setLastName(lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
        employee.setEmail(RandomStringUtils.randomAlphabetic(6) + "@gmail.com");
        employee.setJobTitle("Generated Job");
        employee.setPhone(RandomStringUtils.randomNumeric(11));
        employee.setImageUrl(null);
        employee.setEmployeeCode(UUID.randomUUID().toString());
        employeeRepo.save(employee);
    }

    public void deleteAllEmployee(){
        employeeRepo.deleteAll();
    }

    public List<Employee> getLastEmployee(){
        return employeeRepo.getLastEmployee();
    }
}
