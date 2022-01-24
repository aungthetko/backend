package com.demo.springjwt.resource;

import com.demo.springjwt.modal.Employee;
import com.demo.springjwt.modal.HttpResponse;
import com.demo.springjwt.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeResource {

    private final EmployeeService employeeService;

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployee(){
        List<Employee> employees = employeeService.findAllEmployee();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id){
        Employee employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){
        Employee newEmployee = employeeService.addEmployee(employee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/update/")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee){
        Employee updateEmployee = employeeService.updateEmployee(employee);
        return ResponseEntity.ok().body(updateEmployee);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteEmployee (@PathVariable("id") Long id){
        Employee employee = employeeService.getEmployeeById(id);
        if(employee != null){
            employeeService.deleteEmployee(id);
        }
        return response(HttpStatus.OK, employee.getFirstName() + " " + employee.getLastName() + " was deleted.");
    }

    @GetMapping("/generate")
    public ResponseEntity<HttpResponse> generateEmployee(){
        employeeService.generateEmployee();
        return response(HttpStatus.OK, "Generated employee added");
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<HttpResponse> deleteAllEmployee(){
        employeeService.deleteAllEmployee();
        return response(HttpStatus.OK, "All of employees are deleted.");
    }

    @GetMapping("/last")
    public ResponseEntity<List<Employee>> getLastEmployee(){
        List<Employee> employees = employeeService.getLastEmployee();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message){
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(),
                httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }
}
