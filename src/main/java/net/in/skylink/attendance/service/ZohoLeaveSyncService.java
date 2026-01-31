package net.in.skylink.attendance.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import net.in.skylink.attendance.exceptions.ServiceException;
import net.in.skylink.attendance.model.Employee;
import net.in.skylink.attendance.model.Leave;
import net.in.skylink.attendance.repository.LeaveRepository;
import net.in.skylink.attendance.repository.ZohoOAuthTokenRepository;


@Service
public class ZohoLeaveSyncService {

    private  RestTemplate restTemplate=new RestTemplate();
    private LeaveService leaveService;
    private  ObjectMapper objectMapper=new ObjectMapper();
    private ZohoTokenService zohoTokenService;
    private ZohoOAuthService zohoOAuthTokenService;
    private EmployeeService employeeService;
    
    
    public ZohoLeaveSyncService(LeaveService leaveService,EmployeeService employeeService,ZohoTokenService zohoTokenService,ZohoOAuthService zohoOAuthTokenService ) {
    	this.leaveService=leaveService;
    	this.zohoTokenService=zohoTokenService;
    	this.zohoOAuthTokenService=zohoOAuthTokenService;
    	this.employeeService=employeeService;
    }


    private static final String ZOHO_SKLINK_LEAVE_URL =
        "https://people.zoho.in/people/api/forms/" +
        "skylink_staff_leave_application/getRecords";

    @Transactional
    public List<String> fetchAndSaveLeaveApplications() throws Exception {
    	List<String> failedList=new ArrayList<String>();
    	final String accessToken=zohoTokenService.getAccessToken();
    	System.out.println(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Zoho-oauthtoken " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
        	ResponseEntity<String> response =
                    restTemplate.exchange(ZOHO_SKLINK_LEAVE_URL, HttpMethod.GET, request, String.class);
        	JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode resultArray = root.path("response").path("result");

            for (JsonNode recordNode : resultArray) {

                Iterator<String> recordIds = recordNode.fieldNames();

                while (recordIds.hasNext()) {
                    String recordId = recordIds.next();
                    JsonNode data = recordNode.get(recordId).get(0);

                    Long zohoId = data.path("Zoho_ID").asLong();
                    String leaveApprovalStatus=data.path("ApprovalStatus").asText(null);
                
                    if(leaveApprovalStatus.equalsIgnoreCase("rejected")) {
                    	continue;
                    }
                    Leave newLeave=new Leave();
                    String tempEmployeeId=data.path("Employee_Id").asText(null);
                    try{
                    	Employee testEmployee=employeeService.getEmployee(tempEmployeeId);
                    	LocalDate fromDate=parseDate(data.path("Start_Date").asText(null));
                    	LocalDate toDate=parseDate(data.path("End_Date").asText(null));
        
                    	newLeave.setEmployee(testEmployee);
                        newLeave.setFromDate(fromDate);
                        newLeave.setToDate(toDate);
                        newLeave.setLeaveType(data.path("Type_of_Leave").asText(null));
                        newLeave.setNumberOfDays(data.path("No_of_Days_Leave").asDouble(0.0));
                        newLeave.setReason(data.path("Reason_for_Leave").asText(null));
                        newLeave.setSource("Zoho");
                        newLeave.setStatus(leaveApprovalStatus);
                        newLeave.setZohoLeaveId(zohoId+"");
                        if(leaveService.getLeaveByZohoId(newLeave.getZohoLeaveId())) {
                        	//Duplicate Record can be Handled here
                        }
                        else {
                        	leaveService.saveLeave(newLeave);
                        }
                        
                    }
                    catch(Exception e) {
                    	System.out.println(e);
                    	failedList.add(tempEmployeeId);
                    	
                    }
                    
        
                }
            }
        }
        catch (HttpClientErrorException e) {
			// TODO: handle exception
        	 System.out.println(e.getStatusCode());
        	 
        	 if(e.getStatusCode().equals(HttpStatusCode.valueOf(401))) {
        		 zohoOAuthTokenService.getAccessToken();
        		 
        		 System.out.println("Unauthorized"+zohoOAuthTokenService.getAccessToken());
        	 }
        	 else {
        		 throw new ServiceException("Unknown Error Contact Administrator");
        	 }
		}
        catch (Exception e) {
			// TODO: handle exception
        	System.out.println(e.getMessage());
		}
        return failedList;
        
       

        
    }

    private LocalDate parseDate(String value) {
        if (value == null) return null;
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
    }
    
    
    		private static final String LEAVE_URL =
            "https://people.zoho.in/api/v2/leavetracker/leaves/records";

        public List<String> syncLeaves(LocalDate from, LocalDate to) throws Exception {
        	List<String> failedList=new ArrayList<String>();
        	final String accessToken=zohoTokenService.getAccessToken();

            String apiUrl = LEAVE_URL +
                    "?from=" + formatDate(from) +
                    "&to=" + formatDate(to);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Zoho-oauthtoken " + accessToken);

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            apiUrl,
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            String.class);

            JsonNode recordsNode =
                    objectMapper.readTree(response.getBody())
                                .path("records");

            Iterator<String> recordIds = recordsNode.fieldNames();

            while (recordIds.hasNext()) {
                String recordId = recordIds.next();
                JsonNode data = recordsNode.get(recordId);

                Long zohoLeaveId = data.path("Zoho.ID").asLong();
                String leaveApprovalStatus=data.path("ApprovalStatus").asText(null);
                
                if(leaveApprovalStatus.equalsIgnoreCase("rejected")||leaveApprovalStatus.equalsIgnoreCase("cancelled")) {
                	continue;
                }
                Leave newLeave=new Leave();
                String tempEmployeeId=data.path("EmployeeId").asText(null).trim().toUpperCase();
                
                try{
                	Employee testEmployee=employeeService.getEmployee(tempEmployeeId);
                	LocalDate fromDate=parseDate(data.path("From").asText(null));
                	LocalDate toDate=parseDate(data.path("To").asText(null));
    
                	newLeave.setEmployee(testEmployee);
                    newLeave.setFromDate(fromDate);
                    newLeave.setToDate(toDate);
                    newLeave.setLeaveType(data.path("Type").asText(null));
                    newLeave.setNumberOfDays(countInclusiveDays(fromDate,toDate));
                    newLeave.setReason(data.path("Reason").asText(null));
                    newLeave.setSource("Zoho");
                    newLeave.setStatus(leaveApprovalStatus);
                    newLeave.setZohoLeaveId(zohoLeaveId+"");
                    if(leaveService.getLeaveByZohoId(newLeave.getZohoLeaveId())) {
                    	//Duplicate Record can be Handled here
                    }
                    else {
                    	leaveService.saveLeave(newLeave);
                    }
                }
                catch(Exception e) {
                	System.out.println(e);
                	failedList.add(tempEmployeeId);
                	
                }
            }
                
                return failedList;
        }

        

        private String formatDate(LocalDate date) {
            return date.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
        }
        
        public static Double countInclusiveDays(LocalDate from, LocalDate to) {
            if (from == null || to == null) return 0.0;
            if (to.isBefore(from)) return 0.0;
            return (double) (ChronoUnit.DAYS.between(from, to) + 1);
        }

    
    
}

