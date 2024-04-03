package com.swc.exam.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swc.exam.demo.service.CinemaService;
import com.swc.exam.demo.service.MovieService;
import com.swc.exam.demo.service.TicketService;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
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
	public String showMain(Model model, @RequestParam(defaultValue = "0") int movieId, @RequestParam(defaultValue = "0") int cinemaId, String date) {

		List<Movie> movies = ticketService.getMovieList();
		List<Cinema> cinemas = ticketService.getCinemaList();
		List<String> week = Ut.getForPrintWeek();
		
		
		model.addAttribute("week", week);
		model.addAttribute("movies", movies);
		model.addAttribute("cinemas", cinemas);
		
		List<TheaterTime> theaterTimes = ticketService.getTheaterTimeList(movieId, cinemaId, date);
		model.addAttribute("theaterTimes", theaterTimes);
		
		
		return "usr/ticket/main";
	}

	@RequestMapping("/usr/ticket/ticketing")
	public String showTicketing(Model model, int movieId, int cinemaId, int theaterInfoId, int theaterTimeId) {
		List<TheaterTime> theaterTimes = ticketService.getForPrintTheaterTimes(cinemaId, theaterInfoId, theaterTimeId);
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
	@ResponseBody
	public String showMain(int theaterInfoId, int theaterTimeId, String[] seats) {
		ResultData doTicketingRd = ticketService.doTicketing(theaterInfoId, theaterTimeId, seats, rq.getLoginedMemberId());
		if (doTicketingRd.isSuccess()) {
			return rq.jsReplace("예매가 완료되었습니다.", "../member/myTicketList?id="+rq.getLoginedMemberId());
		}
		return rq.jsReplace("예매에 실패했습니다.", "usr/ticket/main");
	}

	@RequestMapping("/usr/ticket/seatLocation")
	public String showSeatLocation(Model model, int cinemaId, int theaterInfoId, char mySeatRow, int mySeatCol) {
		List<TheaterInfo> theaterInfos = ticketService.getForPrintTheaterInfo(cinemaId, theaterInfoId);

		for (TheaterInfo theaterInfo : theaterInfos) {
			theaterInfo.setExtra__seat(theaterInfo.getSeatRow() + "-" + theaterInfo.getSeatCol());
		}

		model.addAttribute("theaterInfos", theaterInfos);
		model.addAttribute("theater", theaterInfos.get(0).getTheater());
		model.addAttribute("mySeat", mySeatRow + "-" + mySeatCol);

		String seatRows = "";
		int lastSeatRow = theaterInfos.get(theaterInfos.size() - 1).getSeatRow();
		for (int i = 'A'; i <= lastSeatRow; i++) {
			char seatRow = (char) i;
			seatRows += seatRow;
		}

		char[] seatRowArr = seatRows.toCharArray();
		model.addAttribute("seatRowArr", seatRowArr);

		int lastSeatCol = theaterInfos.get(theaterInfos.size() - 1).getSeatCol();
		int[] seatCols = new int[lastSeatCol];
		for (int i = 0; i < lastSeatCol; i++) {
			seatCols[i] = i + 1;
		}

		model.addAttribute("seatColArr", seatCols);

		return "usr/ticket/seatLocation";
	}

}
