package net.in.skylink.attendance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Employee")
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String employeeId;
	
	private String fullName;
	
	private long contactNo;
	
	private String department;
	
	private String destination;
	
	private long reportingToId;
	
	private String emailId;
	
	private Boolean zohoIdAvailable;
	
	public long getId() {
		return id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public long getContactNo() {
		return contactNo;
	}

	public void setContactNo(long contactNo) {
		this.contactNo = contactNo;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public long getReportingToId() {
		return reportingToId;
	}

	public void setReportingToId(long reportingToId) {
		this.reportingToId = reportingToId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Boolean getZohoIdAvailable() {
		return zohoIdAvailable;
	}

	public void setZohoIdAvailable(Boolean isZohoIdAvailable) {
		this.zohoIdAvailable = isZohoIdAvailable;
	}
	
	

	

}
