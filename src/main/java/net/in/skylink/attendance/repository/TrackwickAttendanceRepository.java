package net.in.skylink.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.in.skylink.attendance.model.TrackwickAttendance;

public interface TrackwickAttendanceRepository extends JpaRepository<TrackwickAttendance,Long> {
	
	List<TrackwickAttendance> findByEmployeeId(Long employeeId);
	
	
	List<TrackwickAttendance> findByAttendanceDate(LocalDate date);
}
