package com.swc.exam.demo.vo;

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
	private String playingTime;
	private String seat;
	
	private String extra__seatInfo;
	private String extra__seatId;


}
