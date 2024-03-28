package com.swc.exam.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.TheaterRepository;
import com.swc.exam.demo.repository.TheaterTimeRepository;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Member;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Theater;
import com.swc.exam.demo.vo.TheaterTime;
import com.swc.exam.demo.vo.Cinema;

@Service
public class TicketingService {

	private TheaterTimeRepository theaterTimeRepository;

	public TicketingService(TheaterTimeRepository theaterTimeRepository) {
		this.theaterTimeRepository = theaterTimeRepository;
	}


	public List<TheaterTime> getForPrintTheaterTimes(String region, String theaterName, String date, int time) {
		List<TheaterTime> theaterTime = theaterTimeRepository.getForPrintTheaterTime(region, theaterName, date, time);
		return theaterTime;
	}


	public void doTicketing(String region, String theaterName, String date, int time, String[] seats, int memberId) {
		for (String seat : seats) {
			String[] seatSplit = seat.split("-");
			char seatId = seatSplit[0].trim().charAt(0);
			int seatNo = Integer.parseInt(seatSplit[1]);
			theaterTimeRepository.doTicketing(region, theaterName, date, time, seatId, seatNo, memberId);
		}
	}

}
