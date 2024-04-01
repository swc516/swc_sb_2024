package com.swc.exam.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterInfo {
	private int id;
	private int cinemaId;
	private int theaterInfoId;
	private String theater;
	private char seatRow;
	private int seatCol;
	private String seatStatus;
	
	private String extra__seat;
	private String extra__region;
	private String extra__branch;
	
}
