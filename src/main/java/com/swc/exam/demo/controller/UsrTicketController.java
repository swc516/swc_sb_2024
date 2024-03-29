package com.swc.exam.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.swc.exam.demo.service.CinemaService;
import com.swc.exam.demo.service.MovieService;
import com.swc.exam.demo.service.TheaterService;
import com.swc.exam.demo.service.TicketService;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.Rq;
import com.swc.exam.demo.vo.Theater;
import com.swc.exam.demo.vo.TheaterTime;

@Controller
public class UsrTicketController {

	private TicketService ticketService;
	private CinemaService cinemaService;
	private TheaterService theaterService;
	private MovieService movieService;

	private Rq rq;

	public UsrTicketController(TicketService ticketService, CinemaService cinemaService, TheaterService theaterService,
			MovieService movieService, Rq rq) {
		this.ticketService = ticketService;
		this.cinemaService = cinemaService;
		this.theaterService = theaterService;
		this.movieService = movieService;
		this.rq = rq;
	}

	@RequestMapping("/usr/ticket/main")
	public String showMain(Model model, @RequestParam(defaultValue = "0") int movieId, @RequestParam(defaultValue = "") String region, @RequestParam(defaultValue = "1")String date) {

		System.out.println("movieId:" + movieId);
		System.out.println("region:" + region);
		System.out.println("date:" + date);
		
		List<Movie> movies = ticketService.getMovieList();
		List<Cinema> cinemas = ticketService.getCinemaList();

		List<String> week = ticketService.getForPrintWeek();

		model.addAttribute("week", week);
		model.addAttribute("movies", movies);
		model.addAttribute("cinemas", cinemas);
		
		List<TheaterTime> theaterTimes = ticketService.getTheaterTimeList(movieId, region, date);
		model.addAttribute("theaterTimes", theaterTimes);
		
		return "usr/ticket/main";
	}

	@RequestMapping("/usr/ticket/ticketing")
	public String showTicketing(Model model, String region, String theaterName, String date, int time) {
		List<TheaterTime> theaterTime = ticketService.getForPrintTheaterTimes(region, theaterName, date, time);
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

		return "usr/ticket/ticketing";
	}

	@RequestMapping("/usr/ticket/doTicketing")
	public String showMain(String region, String theaterName, String date, int time, String[] seats) {
		ticketService.doTicketing(region, theaterName, date, time, seats, rq.getLoginedMemberId());
		return "/";
	}

	@RequestMapping("/usr/ticket/seatLocation")
	public String showSeatLocation(Model model, String region, String theaterName, String mySeatId, String mySeatNo) {
		List<Theater> theaters = theaterService.getForPrintTheater(region, theaterName);

		for (Theater theater : theaters) {
			theater.setExtra__seat(theater.getSeatId() + "-" + theater.getSeatNo());
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

		return "usr/ticket/seatLocation";
	}

}
