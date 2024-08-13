package com.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.model.Employee;
import com.demo.repository.EmployeeRepository;
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {

        if (employee.getEmployeeId() == null || employee.getEmployeeId().isEmpty()) {
            String maxId = employeeRepository.findMaxEmployeeId();
            String newId = generateNewEmployeeId(maxId);
            employee.setEmployeeId(newId);
        }

        return employeeRepository.save(employee);
    }

    private String generateNewEmployeeId(String maxId) {
        if (maxId == null) {
            return "E001";  // Start with E001 if no employees exist
        }
        int num = Integer.parseInt(maxId.substring(1)) + 1;
        return "E" + String.format("%03d", num);  // Ensure the number is always 3 digits
    }

    public Employee getEmployee(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }


    public double calculateTax(Employee employee) {

        LocalDate now = LocalDate.now();
        LocalDate financialYearStart = now.getMonthValue() >= 4 ? LocalDate.of(now.getYear(), 4, 1) : LocalDate.of(now.getYear() - 1, 4, 1);
        LocalDate doj = employee.getDoj();
        LocalDate finalDate =  doj.isBefore(financialYearStart) ? financialYearStart : doj;
        int monthsWorked =(int) ChronoUnit.MONTHS.between(finalDate,now) + 1;
   	    double yearlySalary =0;
	    yearlySalary =calculateLopAndTotalSalary(finalDate, employee.getSalary())+ (employee.getSalary() * (monthsWorked-1));
	
        
       //  yearlySalary = employee.getSalary() * monthsWorked;
        double tax = 0.0;

        if (yearlySalary <= 250000) {
            tax = 0.0;
        } else if (yearlySalary <= 500000) {
            tax = (yearlySalary - 250000) * 0.05;
        } else if (yearlySalary <= 1000000) {
            tax = 250000 * 0.05 + (yearlySalary - 500000) * 0.1;
        } else {
            tax = 250000 * 0.05 + 500000 * 0.1 + (yearlySalary - 1000000) * 0.2;
        }

        if (yearlySalary > 2500000) {
            tax += (yearlySalary - 2500000) * 0.02;
        }

        return tax;
    }
    public static double calculateLopAndTotalSalary(LocalDate dateOfJoining, double salary) {

        YearMonth yearMonth = YearMonth.from(dateOfJoining);
        int totalDaysInMonth = yearMonth.lengthOfMonth();

        int workedDays = totalDaysInMonth - dateOfJoining.getDayOfMonth() + 1;
       

        double dailySalary = salary / 30;
        
        // Calculate LOP amount
        int lopDays = totalDaysInMonth - workedDays;
        double lopAmount = lopDays * dailySalary;
        
        // Calculate the total salary for the month
        double totalSalary = workedDays * dailySalary;

        return totalSalary;
    }
}