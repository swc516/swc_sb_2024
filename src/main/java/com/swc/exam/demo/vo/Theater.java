package com.swc.exam.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theater {
	private int id;
	private String relTypeCode;
	private int relId;
	private String theaterName;
	private char seatId;
	private String seatNo;
	private String seatStatus;
	
	private String extra__seat;
}
