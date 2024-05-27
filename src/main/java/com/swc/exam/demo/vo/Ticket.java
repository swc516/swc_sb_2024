package com.swc.exam.demo.vo;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
	private int id;
	private String buyDate;
	private int buyMemberid;
	private String movieTitle;
	private String cinema;
	private String theater;
	private int time;
	private String startTime;
	private String endTime;
	private String seat;
	
	private String extra__seatInfo;
	private String extra__seatId;
	private String extra__dateAndStartTime;
	
	private String extra__movieId;
	
	public String getForPrintPlayingTime() {
		return startTime.substring(11,16) + " ~ " + endTime.substring(11,16);
	}

	public boolean hasReviewWrite() {
		
		String endTime = this.endTime;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.parse(endTime, formatter);
		
		
		return date.isBefore(LocalDateTime.now());
	}
}
