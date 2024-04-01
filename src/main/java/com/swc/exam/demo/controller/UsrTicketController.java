package com.swc.exam.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.swc.exam.demo.service.CinemaService;
import com.swc.exam.demo.service.MovieService;
import com.swc.exam.demo.service.TicketService;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.Rq;
import com.swc.exam.demo.vo.TheaterInfo;
import com.swc.exam.demo.vo.TheaterTime;

@Controller
public class UsrTicketController {

	private TicketService ticketService;

	private Rq rq;

	public UsrTicketController(TicketService ticketService, Rq rq) {
		this.ticketService = ticketService;
		this.rq = rq;
	}

	@RequestMapping("/usr/ticket/main")
	public String showMain(Model model, @RequestParam(defaultValue = "0") int movieId, @RequestParam(defaultValue = "0") int cinemaId,  @RequestParam(defaultValue = "1")String date) {

		List<Movie> movies = ticketService.getMovieList();
		List<Cinema> cinemas = ticketService.getCinemaList();
		List<String> week = ticketService.getForPrintWeek();

		model.addAttribute("week", week);
		model.addAttribute("movies", movies);
		model.addAttribute("cinemas", cinemas);
		
		List<TheaterTime> theaterTimes = ticketService.getTheaterTimeList(movieId, cinemaId, date);
		model.addAttribute("theaterTimes", theaterTimes);
		
		return "usr/ticket/main";
	}

	@RequestMapping("/usr/ticket/ticketing")
	public String showTicketing(Model model, int movieId, int cinemaId, int theaterInfoId, String date, int theaterTime) {
		List<TheaterTime> theaterTimes = ticketService.getForPrintTheaterTimes(cinemaId, theaterInfoId, date, theaterTime);
		model.addAttribute("theaterTimes", theaterTimes);
		model.addAttribute("playingTime", theaterTimes.get(0).getForPrintType1StartTime() + "~" + theaterTimes.get(0).getForPrintType1EndTime());
		
		String seatRows = "";
		int lastSeatRow = theaterTimes.get(theaterTimes.size() - 1).getSeatRow();
		for (int i = 'A'; i <= lastSeatRow; i++) {
			char seatId = (char) i;
			seatRows += seatId;
		}

		char[] seatRowArr = seatRows.toCharArray();
		model.addAttribute("seatRowArr", seatRowArr);

		int lastSeatCol = Integer.parseInt(theaterTimes.get(theaterTimes.size() - 1).getSeatCol());
		int[] seatCols = new int[lastSeatCol];
		for (int i = 0; i < lastSeatCol; i++) {
			seatCols[i] = i + 1;
		}
		model.addAttribute("seatColArr", seatCols);

		String movieTitle = ticketService.getMovieTitleById(movieId);
		String cinemaRegion = ticketService.getCinemaRegionById(cinemaId);
		String cinemaBranch = ticketService.getCinemaBranchById(cinemaId);
		String theater = ticketService.getTheaterById(theaterInfoId);
		
		model.addAttribute("movieTitle", movieTitle);
		model.addAttribute("cinemaRegion", cinemaRegion);
		model.addAttribute("cinemaBranch", cinemaBranch);
		model.addAttribute("theater", theater);

		return "usr/ticket/ticketing";
	}

	@RequestMapping("/usr/ticket/doTicketing")
	public String showMain(String region, String theaterName, String date, int time, String[] seats) {
		ticketService.doTicketing(region, theaterName, date, time, seats, rq.getLoginedMemberId());
		return "/";
	}

	@RequestMapping("/usr/ticket/seatLocation")
	public String showSeatLocation(Model model, String region, String theaterName, String mySeatId, String mySeatNo) {
		List<TheaterInfo> theaters = theaterService.getForPrintTheater(region, theaterName);

		for (TheaterInfo theater : theaters) {
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
