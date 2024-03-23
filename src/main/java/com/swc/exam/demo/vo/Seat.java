package com.swc.exam.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
	private int id;
	private String regDate;
	private String updateDate;
	private String relTypeCode;
	private int relId;
	private String seatId;
	private int seatNo;
	private boolean delStatus;
	private String delDate;
}
