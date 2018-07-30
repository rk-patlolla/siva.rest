package com.rohtek.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rohtek.entities.Department;


public interface DepartmentRepository extends JpaRepository<Department, Long>{

	/*
	@Query("SELECT d.dname FROM department d where d.dname = ?1")
    public String findByDName(String dname);*/
	
}
