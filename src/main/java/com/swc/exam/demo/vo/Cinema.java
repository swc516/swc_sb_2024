package com.swc.exam.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cinema {
	private int id;
	private String regDate;
	private String updateDate;
	private String region;
	private boolean delStatus;
	private String delDate;

	public String getForPrintType1RegDate() {
		return regDate.substring(2,16).replace(" ", "<br>");
	}
	
	public String getForPrintType1UpdateDate() {
		return updateDate.substring(2,16).replace(" ", "<br>");
	}

	public String getForPrintType2RegDate() {
		return regDate.substring(2, 16);
	}

	public String getForPrintType2UpdateDate() {
		return updateDate.substring(2, 16);
	}


}
