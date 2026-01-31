package net.in.skylink.attendance.service;

import org.springframework.stereotype.Service;

import net.in.skylink.attendance.model.Leave;
import net.in.skylink.attendance.repository.LeaveRepository;

@Service
public class LeaveService {
	
	private LeaveRepository leaveRepository;
	
	public LeaveService(LeaveRepository leaveRepository) {
		this.leaveRepository=leaveRepository;
	}
	
	
	public Leave saveLeave(Leave newLeave) {
		return this.leaveRepository.save(newLeave);
	}
	
	public boolean getLeaveByZohoId(String zohoId) {
		return this.leaveRepository.existsByZohoLeaveId(zohoId);
	}

}
