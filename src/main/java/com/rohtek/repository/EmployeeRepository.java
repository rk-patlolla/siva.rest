package com.rohtek.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rohtek.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	public Employee findByename(String ename);
}
