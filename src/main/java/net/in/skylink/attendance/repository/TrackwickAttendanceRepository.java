package net.in.skylink.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.in.skylink.attendance.model.ESSLAttendance;
import net.in.skylink.attendance.model.Employee;
import net.in.skylink.attendance.model.TrackwickAttendance;

public interface TrackwickAttendanceRepository extends JpaRepository<TrackwickAttendance,Long> {
	
	List<TrackwickAttendance> findByEmployeeId(String employeeId);
	
	
	List<TrackwickAttendance> findByAttendanceDate(LocalDate date);
	
	boolean existsByEmployeeAndAttendanceDate(Employee employee, LocalDate date);
}
