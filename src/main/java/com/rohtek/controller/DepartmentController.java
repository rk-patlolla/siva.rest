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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rohtek.entities.Department;
import com.rohtek.exceptions.Response;
import com.rohtek.services.DepartmentService;

@RestController
@RequestMapping("department")
public class DepartmentController {

	public static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private DepartmentService departmentService;

	@RequestMapping(value = "/departmentList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Department> getEmployeeDetails() {
		List<Department> DepartmentList = departmentService.getDepartments();
		return DepartmentList;

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/insertDepartment", produces = MediaType.APPLICATION_JSON_VALUE)
	public Department insertDepartment(@RequestBody Department department, HttpServletResponse response) {

		departmentService.saveDepartment(department);

		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		return department;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/delete/{did}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> deleteDepartment(@PathVariable Long did, HttpServletResponse response) {
		Response res = new Response();
		try {
			boolean delteDepartment = departmentService.deleteDepartment(did);
			if (delteDepartment) {

				res.setMessage(HttpStatus.OK.getReasonPhrase());
				res.setErrorCode(200);
				return new ResponseEntity<Response>(res, HttpStatus.ACCEPTED);

			} else {

				res.setMessage("Department record not found with " + did);
				res.setErrorCode(417);

				return new ResponseEntity<Response>(res, HttpStatus.BAD_REQUEST);

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			String message = "First delete record from child table";
			res.setMessage(message);
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			res.setErrorCode(HttpServletResponse.SC_CONFLICT);
			return new ResponseEntity<Response>(res, HttpStatus.BAD_REQUEST);
		}

	}

}
