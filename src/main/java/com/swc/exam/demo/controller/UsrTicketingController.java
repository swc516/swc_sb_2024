package com.swc.exam.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swc.exam.demo.service.CinemaService;
import com.swc.exam.demo.service.MovieService;
import com.swc.exam.demo.service.TheaterService;
import com.swc.exam.demo.service.TicketingService;
import com.swc.exam.demo.vo.Rq;
import com.swc.exam.demo.vo.Theater;
import com.swc.exam.demo.vo.TheaterTime;

@Controller
public class UsrTicketingController {

	private TicketingService ticketingService;
	private CinemaService cinemaService;
	private TheaterService theaterService;
	private MovieService movieService;

	private Rq rq;

	public UsrTicketingController(TicketingService ticketingService, CinemaService cinemaService,
			TheaterService theaterService, MovieService movieService, Rq rq) {
		this.ticketingService = ticketingService;
		this.cinemaService = cinemaService;
		this.theaterService = theaterService;
		this.movieService = movieService;
		this.rq = rq;
	}

	@RequestMapping("/usr/ticketing/main")
	public String showMain(Model model, String region, String theaterName, String date, int time) {
		List<TheaterTime> theaterTime = ticketingService.getForPrintTheaterTimes(region, theaterName, date, time);
		model.addAttribute("theaterTime", theaterTime);
		model.addAttribute("playingTime",
				theaterTime.get(0).getForPrintType1StartTime() + "~" + theaterTime.get(0).getForPrintType1EndTime());

		String seatIds = "";
		int lastSeatId = theaterTime.get(theaterTime.size() - 1).getSeatId();
		for (int i = 'A'; i <= lastSeatId; i++) {
			char seatId = (char) i;
			seatIds += seatId;
		}

		char[] seatIdArr = seatIds.toCharArray();
		model.addAttribute("seatIdArr", seatIdArr);

		int lastSeatNo = Integer.parseInt(theaterTime.get(theaterTime.size() - 1).getSeatNo());
		int[] seatNos = new int[lastSeatNo];
		for (int i = 0; i < lastSeatNo; i++) {
			seatNos[i] = i + 1;
		}

		model.addAttribute("seatNoArr", seatNos);

		return "usr/ticketing/main";
	}

	@RequestMapping("/usr/ticketing/doTicketing")
	public String showMain(String region, String theaterName, String date, int time, String[] seats) {
		ticketingService.doTicketing(region, theaterName, date, time, seats, rq.getLoginedMemberId());
		return "/";
	}

	@RequestMapping("/usr/ticketing/seatLocation")
	public String showSeatLocation(Model model, String region, String theaterName, String mySeatId, String mySeatNo) {
		List<Theater> theaters = theaterService.getForPrintTheater(region, theaterName);
		
		for(Theater theater : theaters) {
			theater.setExtra__seat(theater.getSeatId()+"-"+theater.getSeatNo());
		}
		
		
		model.addAttribute("theaters", theaters);
		model.addAttribute("theaterName", theaters.get(0).getTheaterName());
		model.addAttribute("mySeat", mySeatId + "-" + mySeatNo);

		String seatIds = "";
		int lastSeatId = theaters.get(theaters.size() - 1).getSeatId();
		for (int i = 'A'; i <= lastSeatId; i++) {
			char seatId = (char) i;
			seatIds += seatId;
		}

		char[] seatIdArr = seatIds.toCharArray();
		model.addAttribute("seatIdArr", seatIdArr);

		int lastSeatNo = Integer.parseInt(theaters.get(theaters.size() - 1).getSeatNo());
		int[] seatNos = new int[lastSeatNo];
		for (int i = 0; i < lastSeatNo; i++) {
			seatNos[i] = i + 1;
		}

		model.addAttribute("seatNoArr", seatNos);

		return "usr/ticketing/seatLocation";
	}

}
