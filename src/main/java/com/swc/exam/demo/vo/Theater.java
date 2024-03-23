package com.swc.exam.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Theater {
	private int id;
	private String regDate;
	private String updateDate;
	private String region;
	private boolean delStatus;
	private String delDate;
}
