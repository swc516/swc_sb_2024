package com.swc.exam.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterTime {
	private int id;
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
}
