package com.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.demo.Exceptions.InvalidEmployeeException;

import com.demo.model.Employee;
import com.demo.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @Operation(summary = "Add a new employee", description = "Stores employee details and validates the input data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid employee data")
    })
    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee) {

        Employee savedEmployee = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @Operation(summary = "Tax Calculator", description = "It calculates the tax of employee.")
    @GetMapping("employees/{employeeId}/tax-deductions")
    public ResponseEntity<?> getEmployeeTax(@PathVariable String employeeId) {
        try {

            if(employeeId == null) {
                throw new InvalidEmployeeException("Employee ID should not be null");
            }else if(employeeId.isBlank()){
                throw new InvalidEmployeeException("Employee ID should not be null");
            }

            Employee employee = employeeService.getEmployee(employeeId);
            double tax = employeeService.calculateTax(employee);

            Map<String, Object> response = new HashMap<>();
            response.put("employeeCode", employee.getEmployeeId());
            response.put("firstName", employee.getFirstName());
            response.put("lastName", employee.getLastName());
            response.put("yearlySalary", employee.getSalary() * 12);
            response.put("taxAmount", tax);
            response.put("cessAmount", (employee.getSalary() * 12) > 2500000 ? ((employee.getSalary() * 12) - 2500000) * 0.02 : 0);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}