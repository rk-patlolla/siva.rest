package com.rohtek.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.rohtek.entities.Employee;

public interface EmployeeService extends UserDetailsService {

	public List<Employee> employeeList();

	public Employee saveEmployee(Employee employee);

	public Employee updateEmployee(Employee employee);
	
	public Employee getEmployee(Long eid);

	public boolean delteEmployee(Long eid);

}
