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
	private String theater;
	private char seatId;
	private int seatNo;
}
