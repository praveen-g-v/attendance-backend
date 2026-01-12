package net.in.skylink.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.in.skylink.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Long>{
	
	Boolean existsByEmployeeId(String employeeId);
	
	Optional<Employee> findByEmployeeId(String employeeId);
	
	Optional<Employee> findByEmailId(String emailId);
	
	Boolean existsByIsZohoIdAvailable(String employeeId);
	
	@Query("select e.zohoIdAvailable from Employee e where e.employeeId = :employeeId")
	Boolean findZohoIdAvailabilityByEmployeeId(@Param("employeeId") String employeeId);

	 

}
