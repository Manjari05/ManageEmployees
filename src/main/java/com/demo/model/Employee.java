package com.demo.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Entity
public class Employee {

	@Id
	@NotBlank(message = "Employee ID is mandatory")
    private String employeeId;

	@NotBlank(message = "First name is mandatory")
    private String firstName;

	@NotBlank(message = "Last name is mandatory")
    private String lastName;

	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is mandatory")
    private String email;

	@NotNull(message = "Phone numbers are mandatory")
	@Size(min = 1, message = "At least one phone number is required")
	private List<@Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits") String> phoneNumbers;

	@NotNull(message = "Date of joining is mandatory")
    private LocalDate doj;

	@Min(value = 0, message = "Salary must be a positive number")
    private Double salary;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public LocalDate getDoj() {
		return doj;
	}

	public void setDoj(LocalDate doj) {
		this.doj = doj;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee{" +
				"employeeId='" + employeeId + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", phoneNumbers=" + phoneNumbers +
				", doj=" + doj +
				", salary=" + salary +
				'}';
	}
}