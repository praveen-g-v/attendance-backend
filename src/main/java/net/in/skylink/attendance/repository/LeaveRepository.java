package net.in.skylink.attendance.repository;



import net.in.skylink.attendance.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    Optional<Leave> findByZohoLeaveId(String zohoLeaveId);
}

