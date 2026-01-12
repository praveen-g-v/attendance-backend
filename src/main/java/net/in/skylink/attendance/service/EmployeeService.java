package net.in.skylink.attendance.service;

import org.springframework.stereotype.Service;

import net.in.skylink.attendance.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private EmployeeRepository empoloyeeRepository;
	
	public EmployeeService(EmployeeRepository employeeRepository) {
		this.empoloyeeRepository =employeeRepository;
	}
	
	
	
	
}
