package net.in.skylink.attendance.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.in.skylink.attendance.exceptions.ServiceException;
import net.in.skylink.attendance.model.ApiResponse;
import net.in.skylink.attendance.service.TrackwickAttendanceservice;

@RestController
public class TrackwickAttendanceController {
	
	private TrackwickAttendanceservice trackwickAttendanceservice;
	
	
	public TrackwickAttendanceController (TrackwickAttendanceservice trackwickAttendanceservice) {
		this.trackwickAttendanceservice=trackwickAttendanceservice;
	}
	
	
	@PostMapping("/api/public/trackwick")
	public ResponseEntity<byte[]> importEsslAttendance(MultipartFile file) throws ServiceException {
		
		
		byte[] excel = trackwickAttendanceservice.importTrackWickAttendance(file);

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    "attachment; filename=attendance.xlsx")
	            .contentType(
	                    MediaType.parseMediaType(
	                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	            .body(excel);
		
	}

}
