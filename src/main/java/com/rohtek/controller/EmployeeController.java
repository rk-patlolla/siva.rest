package com.rohtek.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rohtek.entities.Employee;
import com.rohtek.exceptions.EmployeeException;
import com.rohtek.exceptions.Response;
import com.rohtek.jwt.JwtTokenUtil;
import com.rohtek.repository.EmployeeRepository;
import com.rohtek.services.EmployeeService;

@RestController
@RequestMapping("employee")
public class EmployeeController {

	public static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	EmployeeService employeeService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public ResponseEntity<?> register(@RequestBody Employee employee) throws AuthenticationException {

		final Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(employee.getEname(), employee.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Employee employeeDetails = employeeRepository.findByename(employee.getEname());

		final String token = jwtTokenUtil.generateToken(employeeDetails);

		return ResponseEntity.ok(token);

	}


	@RequestMapping(value = "/emplist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Employee> getEmployeeDetails() {
		List<Employee> employeeList = employeeService.employeeList();
		return employeeList;

	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@PostMapping(value = "/saveEmployee", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response>  storeEmployee(@RequestBody Employee employee, HttpServletResponse response) {
		Response response2 = new Response();
		
		try {
			employeeService.saveEmployee(employee);
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			response2.setMessage("Employee record inserted");
			 
		} catch (Exception e) {
			
			 e.printStackTrace();
			 String message = "could not execute statement: Employee name already exist";
			 response2.setMessage(message);
			 response.setStatus(HttpServletResponse.SC_CONFLICT);
			 response2.setErrorCode(HttpServletResponse.SC_CONFLICT);
		}
		return new ResponseEntity<Response>(response2, HttpStatus.BAD_REQUEST);    
		
		
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/update/{eid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> updateEmployee(@PathVariable Long eid, @RequestBody Employee employee,
			HttpServletResponse response) {
		Response response2 = new Response();
		Employee updateEmployee = employeeService.getEmployee(eid);

		if (updateEmployee == null) {
			logger.error("Unable to update. User with id {} not found.", eid);
			response2.setMessage("Employee record not found with "+eid);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {

			updateEmployee.setEname(employee.getEname());
			updateEmployee.setMobile(employee.getMobile());
			updateEmployee.setEaddress(employee.getEaddress());

			employeeService.updateEmployee(updateEmployee);
			response2.setMessage("Employee record updated successfully...........!");
			return new ResponseEntity<Response>(response2, HttpStatus.ACCEPTED); 
		}
		return new ResponseEntity<Response>(response2, HttpStatus.ACCEPTED); 
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/delete/{eid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> deleteEmployee(@PathVariable Long eid, HttpServletResponse response)throws EmployeeException {
		Response response2 = new Response();
		boolean delteEmployee = employeeService.delteEmployee(eid);
		
		
			if(delteEmployee) {
				 
				
			        response2.setMessage(HttpStatus.OK.getReasonPhrase());
			        response2.setErrorCode(200);
			        return new ResponseEntity<Response>(response2, HttpStatus.ACCEPTED);    
				 
				 
			} else
			{
			  
			    response2.setMessage("Employee record not found with "+eid);
			    response2.setErrorCode(417);
			    return new ResponseEntity<Response>(response2, HttpStatus.NOT_FOUND);    
			    
			}
		
		
	}

}
