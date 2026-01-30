package net.in.skylink.attendance.model;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee_leave")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Zoho Leave ID (important for sync)
    @Column(name = "zoho_leave_id", unique = true)
    private String zohoLeaveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "leave_type")
    private String leaveType;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "days")
    private Double numberOfDays;

    @Column(name = "status")
    private String status;

    @Column(name = "reason", length = 1000)
    private String reason;

    @Column(name = "source")
    private String source; // ZOHO / MANUAL

	public String getZohoLeaveId() {
		return zohoLeaveId;
	}

	public void setZohoLeaveId(String zohoLeaveId) {
		this.zohoLeaveId = zohoLeaveId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public Double getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Double numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getId() {
		return id;
	}
    
    
    
}
