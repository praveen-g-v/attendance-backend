package net.in.skylink.attendance.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import net.in.skylink.attendance.exceptions.ServiceException;
import net.in.skylink.attendance.model.Employee;
import net.in.skylink.attendance.model.TrackwickAttendance;
import net.in.skylink.attendance.repository.EmployeeRepository;
import net.in.skylink.attendance.repository.TrackwickAttendanceRepository;

@Service
public class TrackwickAttendanceservice {
	
	
	private TrackwickAttendanceRepository trackwickAttendanceRepository;
	
	private EmployeeRepository employeeRepository;
	
	private static final DataFormatter FORMATTER = new DataFormatter();
	
	
	public TrackwickAttendanceservice(TrackwickAttendanceRepository trackwickAttendanceRepository,EmployeeRepository employeeRepository) {
		this.trackwickAttendanceRepository=trackwickAttendanceRepository;
		this.employeeRepository=employeeRepository;
	}

	private String getString(Cell cell) {
	    if (cell == null) return null;
	    return FORMATTER.formatCellValue(cell).trim();
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
	
	
	
	

	private Workbook readExcel(MultipartFile file) throws Exception {
		Workbook resultWorkbook = new XSSFWorkbook();
		Sheet resultSheet = resultWorkbook.createSheet("Import Report");
		
		Row resultHeaderRow = resultSheet.createRow(0);

        String[] resultHeaders = {
            "S.No",
            "Employee Code",
            "Employee Name",
            "Status",
            "Date : "+LocalDate.now()+""
        };
        CellStyle headerStyle = resultWorkbook.createCellStyle();
        Font headerFont = resultWorkbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 0; i < resultHeaders.length; i++) {
            Cell cell = resultHeaderRow.createCell(i);
            cell.setCellValue(resultHeaders[i]);
            cell.setCellStyle(headerStyle);
        }
		
		
	    try (InputStream is = file.getInputStream();
	         Workbook workbook = WorkbookFactory.create(is)) {
	        Sheet sheet = workbook.getSheetAt(0);
	        Sheet resultsheet=workbook.createSheet();
	        List<String> headers=new ArrayList<String>();
	        Row rowHeader=sheet.getRow(0);
	        for(int i=0;i<rowHeader.getLastCellNum();i++) {
	        	headers.add(getString(rowHeader.getCell(i)));
	        }

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	        	Row resultRow = resultsheet.createRow(i);
	        	
	        	resultRow.createCell(0).setCellValue(i);                     // S.No
	        	
	            Row row = sheet.getRow(i);
	            if (row == null) continue;
	            
	            System.out.println(sheet.getLastRowNum());
	            
	            Employee tempEmployee=new Employee();
	            try {
	            	for(int j=0;j<headers.size();j++) {
		            	if(headers.get(j).equalsIgnoreCase("identity")) {
		            		String tempEmployeeId=getString(row.getCell(j));
		            		
		            		System.out.println("Employee Id "+tempEmployeeId+"  "+j );
		            		resultRow.createCell(1).setCellValue(tempEmployeeId);        // Emp Code
		            		tempEmployee=employeeRepository.findByTrackwickId(tempEmployeeId).orElseThrow(() -> new ServiceException("No Employee Found"));
		            	}
		            	else if(headers.get(j).equalsIgnoreCase("Name")) {
		            		String tempName=getString(row.getCell(j));
		            		resultRow.createCell(2).setCellValue(tempName);                // Name
		            	}
		            	else if(headers.get(j).equalsIgnoreCase("Status")) {
		            		if(tempEmployee!=null) {
		            			if(trackwickAttendanceRepository.existsByEmployeeAndAttendanceDate(tempEmployee, LocalDate.now())) {
		            				//Will figure it out whether to update the feild or not
		            				resultRow.createCell(3).setCellValue("Data Already Exists");  
		            			}
		            			else {
		            				String tempStatus=getString(row.getCell(j));
			            			TrackwickAttendance tempAttendance=new TrackwickAttendance();
			            			tempAttendance.setAttendanceDate(LocalDate.now());
			            			tempAttendance.setEmployeeId(tempEmployee);
			            			tempAttendance.setAttendanceStatus(true);
			            			trackwickAttendanceRepository.save(tempAttendance);
			            			resultRow.createCell(3).setCellValue("Data Imported Successfully");  
			            	
		            				
		            			}
		            			
		            		}
		            		String tempEmployeeId=getString(row.getCell(j));
		            	}
		            }
	            	
	            }
	            catch(ServiceException e) {
	            	//need to handle employee id not present just printing for now
	            	resultRow.createCell(3).setCellValue("Employee id Unavailable");  
	            	System.out.println(e);
	            }
	        }
	    }
return resultWorkbook;
	}
	
	public byte[] importTrackWickAttendance(MultipartFile file)throws ServiceException {
		if (file == null || file.isEmpty()) {
	        throw new IllegalArgumentException("File is empty");
	    }

	    String filename = file.getOriginalFilename();

	    if (filename == null) {
	        throw new IllegalArgumentException("Invalid file");
	    }
	    
	    try {
	        if (filename.endsWith(".csv")) {
	        	throw new IllegalArgumentException("Unsupported file format");
	        	
	        } else if (filename.endsWith(".xlsx") || filename.endsWith(".xls")) {
	        		Workbook result=readExcel(file);
	        		ByteArrayOutputStream out = new ByteArrayOutputStream() ;
	        			result.write(out);
	        	            return out.toByteArray();
	        		
	        		
//	        			result.write(out)
//	            return ;
	        } else {
	            throw new IllegalArgumentException("Unsupported file format");
	        }
	        
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new ServiceException("Something Went wrong");
	    }

	}

}
