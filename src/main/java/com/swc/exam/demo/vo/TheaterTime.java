package com.swc.exam.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterTime {
	private int id;
	private int theaterTimeId;
	private String relTypeCode;
	private int relId;
	private String theaterName;
	private int time;
	private int movieId;
	private String date;
	private String startTime;
	private String endTime;
	private char seatId;
	private String seatNo;
	private String seatStatus;
	private boolean seatSell;
	private int memberId;
	private String ticketingDate;
	
	private String extra__movieTitle;
	private int extra__sellSeatCount;
	private int extra__maxSeatCount;
	
	
	public String getForPrintType1StartTime() {
		return startTime.substring(0, 5);
	}
	
	public String getForPrintType1EndTime() {
		return endTime.substring(0, 5);
	}
}
