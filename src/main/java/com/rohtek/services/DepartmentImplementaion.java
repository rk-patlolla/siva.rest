package com.rohtek.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohtek.entities.Department;
import com.rohtek.repository.DepartmentRepository;

@Service
public class DepartmentImplementaion implements DepartmentService{
	@Autowired
	private DepartmentRepository departmentRepository;

	public Department saveDepartment(Department department) {
		/*Timestamp createDate = new Timestamp(System.currentTimeMillis());
		department.setCreateDate(createDate);
		department.setUpdateDate(createDate);*/
		return departmentRepository.save(department);

	}

	public List<Department> getDepartments() {

		return departmentRepository.findAll();

	}

	public boolean deleteDepartment(Long did) {
		

			departmentRepository.deleteById(did);
			return true;

	}
}