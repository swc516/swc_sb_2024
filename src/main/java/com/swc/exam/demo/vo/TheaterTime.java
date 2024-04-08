package com.swc.exam.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterTime {
	private int id;
	private int cinemaId;
	private int theaterInfoId;
	private int theaterTimeId;
	private int theaterTime;
	private String date;
	private String startTime;
	private String endTime;
	private int movieId;
	private char seatRow;
	private String seatCol;
	private String seatStatus;
	private boolean seatSell;
	private int buyMemberId;
	private String buyDate;
	
	private String extra__movieTitle;
	private int extra__sellSeatCount;
	private int extra__maxSeatCount;
	
	private String extra__theater;
	private String extra__cinemaRegion;
	private String extra__cinemaBranch;
	
	
	public String getForPrintType1StartTime() {
		return startTime.substring(11, 16);
	}
	
	public String getForPrintType1EndTime() {
		return endTime.substring(11, 16);
	}
}
