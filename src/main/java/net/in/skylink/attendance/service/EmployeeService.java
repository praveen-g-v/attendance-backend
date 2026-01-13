package net.in.skylink.attendance.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.in.skylink.attendance.model.Employee;
import net.in.skylink.attendance.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private EmployeeRepository employeeRepository;
	
	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository =employeeRepository;
	}
	
	
	public Employee saveEmployee(Employee employee) {
		return this.employeeRepository.save(employee);
	}
	
	private List<Employee>  readCsv(MultipartFile file) throws Exception {
		
		List<Employee> employees=new ArrayList();

	    try (BufferedReader reader = new BufferedReader(
	    		
	            new InputStreamReader(file.getInputStream()))) {

	        String line;
	        List<String> rowHeader=new ArrayList<String>();
	        int rowNumber = 0;

	        while ((line = reader.readLine()) != null) {
	            rowNumber++;

	            // Skip header
	            if (rowNumber == 1) {
	            	rowHeader.addAll(Arrays.asList(line.split(",")));
	                continue;
	            }
	            

	            String[] columns = line.split(",");
	            try {
	            	
		            Employee newEmployee=new Employee();
		            for(int i=0;i<rowHeader.size()&&i<columns.length;i++) {
		            	
		            	if(rowHeader.get(i).equalsIgnoreCase("EMP ID")||rowHeader.get(i).equalsIgnoreCase("Employee Id")||rowHeader.get(i).equalsIgnoreCase("Id")) {
		            		columns[i]=columns[i].trim();
		            		columns[i]=columns[i].toUpperCase();
		            		newEmployee.setEmployeeId(columns[i]);
		            	}
		            	else if(rowHeader.get(i).equalsIgnoreCase("name")||rowHeader.get(i).equalsIgnoreCase("firstname")||rowHeader.get(i).equalsIgnoreCase("first name")) {
		            		newEmployee.setFullName(columns[i]);
		            	}
		            	else if(rowHeader.get(i).equalsIgnoreCase("Department")||rowHeader.get(i).equalsIgnoreCase("dept")) {
		            		newEmployee.setDepartment(columns[i]);
		            	}
		            	else if(rowHeader.get(i).equalsIgnoreCase("Designation")||rowHeader.get(i).equalsIgnoreCase("dest")) {
		            		newEmployee.setDestination(columns[i]);
		            	}
		            	else if(rowHeader.get(i).equalsIgnoreCase("email")||rowHeader.get(i).equalsIgnoreCase("email id")||rowHeader.get(i).equalsIgnoreCase("zoho mail id")||rowHeader.get(i).equalsIgnoreCase("mailId")||rowHeader.get(i).equalsIgnoreCase("Mail Id")) {
		            		columns[i]=columns[i].trim();
		            		if(!(columns[i].contains("@")&&columns[i].contains("."))) {
		            			continue;
		            		}
		            		newEmployee.setEmailId(columns[i]);
		            	}
		            	else if(rowHeader.get(i).equalsIgnoreCase("is zoho")||rowHeader.get(i).equalsIgnoreCase("zoho id")) {
		            		columns[i]=columns[i].trim();
		            		if(!(columns[i].contains("true")||columns[i].contains("false"))) {
		            			continue; 
		            		}
		            		newEmployee.setZohoIdAvailable(Boolean.parseBoolean(columns[i]));
		            	}
		            	else if(rowHeader.get(i).equalsIgnoreCase("mobile no")||rowHeader.get(i).equalsIgnoreCase("mobile")||rowHeader.get(i).equalsIgnoreCase("phone no")||rowHeader.get(i).equalsIgnoreCase("phone")||rowHeader.get(i).equalsIgnoreCase("contact")||rowHeader.get(i).equalsIgnoreCase("CONTACT NO")) {
		            		columns[i]=columns[i].trim();
		            		if(columns[i].contains("+")||columns[i].length()>10) {
		            			columns[i]=columns[i].substring(columns[i].length()-10, columns[i].length());
		            		}  
		            		if(columns[i].length()<10) {
		            			continue;
		            		}
		            		newEmployee.setContactNo(Long.parseLong(columns[i]));
		            	}
		            }
		            if(newEmployee.getEmployeeId()==null) {
		            	continue;
		            }
		            employees.add(newEmployee);

	            }
	            catch(Exception e) {
	            	System.err.println("Error on id"+ line);
	            	
	            }
	            
	        }
	        return employees;
	    }
	}
	
	
	private String getString(Cell cell) {
	    if (cell == null) return null;

	    if (cell.getCellType() == CellType.STRING) {
	        return cell.getStringCellValue().trim();
	    }
	    if (cell.getCellType() == CellType.NUMERIC) {
	        return String.valueOf((long) cell.getNumericCellValue());
	    }
	    return null;
	}

	private long getLong(Cell cell) {
	    if (cell == null) return 0L;

	    if (cell.getCellType() == CellType.NUMERIC) {
	        return (long) cell.getNumericCellValue();
	    }
	    if (cell.getCellType() == CellType.STRING) {
	        return Long.parseLong(cell.getStringCellValue().trim());
	    }
	    return 0L;
	}

	private Boolean getBoolean(Cell cell) {
	    if (cell == null) return false;

	    if (cell.getCellType() == CellType.BOOLEAN) {
	        return cell.getBooleanCellValue();
	    }
	    if (cell.getCellType() == CellType.STRING) {
	        return Boolean.parseBoolean(cell.getStringCellValue().trim());
	    }
	    return false;
	}

	
	private void readExcel(MultipartFile file) throws Exception {

	    try (InputStream is = file.getInputStream();
	         Workbook workbook = WorkbookFactory.create(is)) {

	        Sheet sheet = workbook.getSheetAt(0);

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            String employeeId = getString(row.getCell(0));
	            String fullName = getString(row.getCell(1));
	            long contactNo = getLong(row.getCell(2));
	            String department = getString(row.getCell(3));
	            String destination = getString(row.getCell(4));
	            long reportingToId = getLong(row.getCell(5));
	            String emailId = getString(row.getCell(6));
	            Boolean zohoIdAvailable = getBoolean(row.getCell(7));

	            // Map → validate → save
	        }
	    }
	}


	
	public List<Employee> importEmployee(MultipartFile file) {
		
		
		List<Employee> employeeslist=new ArrayList<>();
		if (file == null || file.isEmpty()) {
	        throw new IllegalArgumentException("File is empty");
	    }

	    String filename = file.getOriginalFilename();

	    if (filename == null) {
	        throw new IllegalArgumentException("Invalid file");
	    }
	    
	    try {
	        if (filename.endsWith(".csv")) {
	        	employeeslist=readCsv(file);
	        } else if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {
	            readExcel(file);
	        } else {
	            throw new IllegalArgumentException("Unsupported file format");
	        }
	        
	        for (Employee emp : employeeslist) {
	            Optional<Employee> existing = employeeRepository.findByEmployeeId(emp.getEmployeeId());
	            if (existing.isPresent()) {
	                Employee dbEmp = existing.get();
	                dbEmp.setFullName(emp.getFullName());
	                dbEmp.setContactNo(emp.getContactNo());
	                dbEmp.setDepartment(emp.getDepartment());
	                dbEmp.setDestination(emp.getDestination());
	                dbEmp.setReportingToId(emp.getReportingToId());
	                dbEmp.setEmailId(emp.getEmailId());
	                dbEmp.setZohoIdAvailable(emp.getZohoIdAvailable());
	                employeeRepository.save(dbEmp); // update
	            } else {
	                employeeRepository.save(emp); // insert
	            }
	        }
	        return employeeslist;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }

	}
	
	
	private void writeExcel(List<Employee> employees) {
		
		
		
	}
	
	
}
