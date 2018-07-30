package com.rohtek.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "department")
@EntityListeners(AuditingEntityListener.class)
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "did")
	private Long did;
	@NotNull
	//@Size(min = 2, message = "Name should have atleast 2 characters")
	@Size(min = 2, message = "{department.dname.size}")
	@Pattern(regexp = "[A-Za-z]+",message="Enter alphabets")
	@Column(name = "dname", unique = true)
	private String dname;
	@Column(name = "createDate", updatable = false)
	@CreatedDate
	private Timestamp createDate;

	@Column(name = "updateDate")
	@LastModifiedDate
	private Timestamp updateDate;
	@Column(name = "active")
	private boolean active = true;

	public String getDname() {
		return dname;
	}

	public Long getDid() {
		return did;
	}

	public void setDid(Long did) {
		this.did = did;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
