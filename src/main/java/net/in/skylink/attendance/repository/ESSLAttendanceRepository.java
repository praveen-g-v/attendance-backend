package net.in.skylink.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.in.skylink.attendance.model.ESSLAttendance;
import net.in.skylink.attendance.model.Employee;
public interface ESSLAttendanceRepository extends JpaRepository<ESSLAttendance, Long> {
	
	List<ESSLAttendance> findByEmployeeId(String employeeId);
	
	
	List<ESSLAttendance> findByAttendanceDate(LocalDate date);
	
	boolean existsByEmployeeAndAttendanceDate(Employee employee, LocalDate date);

}
