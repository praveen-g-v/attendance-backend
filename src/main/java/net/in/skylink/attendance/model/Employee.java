package net.in.skylink.attendance.model;

import java.util.Date;

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
	
	private String trackwickId;
	
	private String fullName;
	
	private long contactNo;
	
	private String department;
	
	private String destination;
	
	private long reportingToId;
	
	private String emailId;
	
	private Boolean zohoIdAvailable;

	private Boolean resigned;
	
	private Date resignedDate;
	
	public String getTrackwickId() {
		return trackwickId;
	}

	public void setTrackwickId(String trackwickid) {
		this.trackwickId = trackwickid;
	}
	
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

	public Boolean getResigned() {
		return resigned;
	}

	public void setResigned(Boolean resigned) {
		this.resigned = resigned;
	}

	public Date getResignedDate() {
		return resignedDate;
	}

	public void setResignedDate(Date resignedDate) {
		this.resignedDate = resignedDate;
	}

}
