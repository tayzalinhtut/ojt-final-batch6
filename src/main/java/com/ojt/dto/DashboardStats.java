package com.ojt.dto;

import lombok.Data;

@Data
public class DashboardStats {
	
	private int totalMembers;
	private double passRate;
	private double offerAcceptanceRate;
	private int totalPendingResults;

}