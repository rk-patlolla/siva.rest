package com.rohtek.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rohtek.entities.Employee;
import com.rohtek.repository.EmployeeRepository;
import com.rohtek.util.ResourceNotFoundException;

@Service
public class EmployeeImplementaion implements EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public List<Employee> employeeList() {
		return employeeRepository.findAll();

	}

	@Override
	public Employee saveEmployee(Employee employee) {

		//Timestamp createDate = new Timestamp(System.currentTimeMillis());
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
		/*employee.setCreateDate(createDate);
		employee.setUpdateDate(createDate);*/

		return employeeRepository.save(employee);
	}

	@Override
	public boolean delteEmployee(Long eid) {
		try {

			employeeRepository.deleteById(eid);
			return true;
		} catch (Exception e) {

			return false;
		}

	}

	public UserDetails loadUserByUsername(String ename) throws UsernameNotFoundException {
		Employee emp = employeeRepository.findByename(ename);

		GrantedAuthority authority = new SimpleGrantedAuthority(emp.getRole());

		UserDetails userDetails = (UserDetails) new User(emp.getEname(), emp.getPassword(), Arrays.asList(authority));
		return userDetails;
	}

	@Override
	public Employee updateEmployee(Employee employee) {

		/*Timestamp updateDate = new Timestamp(System.currentTimeMillis());
		employee.setUpdateDate(updateDate);*/
		return employeeRepository.save(employee);

	}

	@Override
	public Employee getEmployee(Long eid) {
		Employee employeeFound = employeeRepository.findById(eid)
				.orElseThrow(() -> new ResourceNotFoundException("eid", "eid", eid));
		return employeeFound;
	}

}
