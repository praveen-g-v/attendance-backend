package net.in.skylink.attendance.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.in.skylink.attendance.exceptions.ServiceException;
import net.in.skylink.attendance.model.ApiResponse;
import net.in.skylink.attendance.model.Employee;
import net.in.skylink.attendance.service.EmployeeService;


@RestController

public class EmployeeController {
	private EmployeeService employeeService;
	
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService=employeeService;
	}
	
	@PostMapping("/api/public/employee")
    public Employee  createEmployee(@RequestBody Employee employee) throws ServiceException {
        return employeeService.saveEmployee(employee);
    }
	
	@PostMapping("/api/public/employees")
	public List<Employee> importEployees(@RequestParam("file") MultipartFile file ){
		return this.employeeService.importEmployee(file);
	}
	
	
	@GetMapping("/api/public/employees")
	public ApiResponse<List<Employee> >getEmployees( ){
		
		try {
			List<Employee> employeesList=this.employeeService.getEmployees();
			return new ApiResponse(true, "success", employeesList);
		}
		catch(Exception err) {
			return new ApiResponse(false, "failure", new ArrayList<>());
		}
	}
}
