package net.in.skylink.attendance.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.in.skylink.attendance.model.ApiResponse;
import net.in.skylink.attendance.service.LeaveService;
import net.in.skylink.attendance.service.PdfReportService;
import net.in.skylink.attendance.service.ZohoLeaveSyncService;

@RestController
@RequestMapping("/api/public/leave")
public class LeaveController {
	
	private LeaveService leaveService;
	
	private ZohoLeaveSyncService zohoLeaveSyncService;
	
	private PdfReportService pdfReportService;
	
	public LeaveController(LeaveService leaveService,ZohoLeaveSyncService  zohoLeaveSyncService,PdfReportService pdfReportService) {
		this.leaveService=leaveService;
		this.zohoLeaveSyncService=zohoLeaveSyncService;
		this.pdfReportService=pdfReportService;
	}
	
	
	
	@PostMapping("/synczoho")
	public ResponseEntity<ApiResponse<Map<String,List<String>>>> getLeaveFromZoho() throws Exception{
		List<String> failedWithoutZohoId= this.zohoLeaveSyncService.fetchAndSaveLeaveApplications();
		LocalDate today = LocalDate.now();
		LocalDate oneMonthAgo = today.minusMonths(1);
		List<String> failedWithZohoId=this.zohoLeaveSyncService.syncLeaves(oneMonthAgo, today);
		
		Map<String,List<String>> map=new HashMap<>();
		map.put("Skylink Leave Staff Management",failedWithoutZohoId);
		map.put("Zoho People Leave", failedWithZohoId);
		
		return new ResponseEntity<ApiResponse<Map<String,List<String>>>>(new ApiResponse<Map<String,List<String>>>(true, "success", map),HttpStatus.ACCEPTED);
		
	}
	
	@GetMapping("/attendance-report")
	public ResponseEntity<byte[]> downloadReport() throws Exception {
	    byte[] pdfBytes = pdfReportService.generateAttendanceReport();
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Attendance_Report.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);
	}


}
