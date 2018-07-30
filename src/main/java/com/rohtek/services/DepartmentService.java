package com.rohtek.services;

import java.util.List;

import com.rohtek.entities.Department;

public interface DepartmentService  {

	public Department saveDepartment(Department department);

	public List<Department> getDepartments();

	public boolean deleteDepartment(Long did);
	/*public String searchByName(String dname);
*/
}
