package net.in.skylink.attendance.controller;

import java.io.ByteArrayOutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.in.skylink.attendance.exceptions.ServiceException;
import net.in.skylink.attendance.service.EssLAttendanceService;

@RestController
public class ESSLAttendanceController {
	
	private EssLAttendanceService esslAttendanceService;
	
	public ESSLAttendanceController(EssLAttendanceService esslAttendanceService) {
		this.esslAttendanceService=esslAttendanceService;
	}
	
	
	@PostMapping("/api/public/essl")
	public ResponseEntity<byte[]> importEsslAttendance(MultipartFile file) throws ServiceException {
		
		
		byte[] excel = esslAttendanceService.importEsslAttendance(file);

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    "attachment; filename=attendance.xlsx")
	            .contentType(
	                    MediaType.parseMediaType(
	                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	            .body(excel);
		
	}

}
