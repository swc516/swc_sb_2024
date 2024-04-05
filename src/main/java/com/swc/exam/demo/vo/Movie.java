package com.swc.exam.demo.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
	private int id;
	private String regDate;
	private String updateDate;
	private String title;
	private String body;
	private int runningTime;
	private String country;
	private String director;
	private String actor;
	private String genre;
	private String releaseDate;
	private String trailer;
	private double rate;
	private int count;
	private boolean delStatus;
	private String delDate;
	
	private String[] extra__directors;
	private List<String> extra__actors;

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
