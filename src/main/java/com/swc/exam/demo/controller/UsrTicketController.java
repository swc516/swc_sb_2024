package com.swc.exam.demo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swc.exam.demo.service.CinemaService;
import com.swc.exam.demo.service.MovieService;
import com.swc.exam.demo.service.TicketService;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Member;
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
	public String showMain(Model model, @RequestParam(defaultValue = "0") int movieId,
			@RequestParam(defaultValue = "0") int cinemaId, String date) {

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
		model.addAttribute("playingTime", theaterTimes.get(0).getForPrintStartTime() + " ~ "
				+ theaterTimes.get(0).getForPrintEndTime());

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

		Member loginedMember = rq.getLoginedMember();

		model.addAttribute("email", loginedMember.getEmail());
		model.addAttribute("name", loginedMember.getName());
		model.addAttribute("cellphoneNo", loginedMember.getCellphoneNo());

		return "/usr/ticket/ticketing";
	}

	@RequestMapping("/usr/ticket/doTicketing")
	@ResponseBody
	public String doTicketing(int theaterInfoId, int theaterTimeId, String[] seats, String movieTitle, String cinema,
			String theater, int time, String startTime, String endTime) {
		ResultData doTicketingRd = ticketService.doTicketing(theaterInfoId, theaterTimeId, seats,
				rq.getLoginedMemberId(), movieTitle, cinema, theater, time, startTime, endTime);
		if (doTicketingRd.isSuccess()) {
			return rq.jsReplace("예매가 완료되었습니다.", "../member/myTicketList?id=" + rq.getLoginedMemberId());
		}
		return rq.jsReplace("예매에 실패했습니다.", "usr/ticket/main");
	}

	@RequestMapping("/usr/ticket/seatLocation")
	public String showSeatLocation(Model model, String cinema, String theater, String mySeats) {
		String[] seatSplit = mySeats.split(",");
		List<String> mySeatList = new ArrayList<String>();
		// E-7-일반
		for (String seats : seatSplit) {
			mySeatList.add(seats.trim());

		}

		List<TheaterInfo> theaterInfos = ticketService.getForPrintTheaterInfo(cinema, theater);

		for (TheaterInfo theaterInfo : theaterInfos) {
			String seat = theaterInfo.getSeatRow() + "" + theaterInfo.getSeatCol();
			for (String mySeat : mySeatList) {
				if (seat.equals(mySeat))
					theaterInfo.setExtra__mySeat(true);
			}
		}

		model.addAttribute("theaterInfos", theaterInfos);
		model.addAttribute("theater", theaterInfos.get(0).getTheater());
		model.addAttribute("mySeat", "A-7, A-8");

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

	@RequestMapping("/usr/ticket/writeReviewForm")
	public String writeReviewForm(Model model, String movieTitle) {
		int check = ticketService.hasReviewWrite(rq.getLoginedMemberId(), movieTitle);
		
		if(check == 0) {
			model.addAttribute("closeWindow", false);
		}
		
		return "/usr/ticket/writeReview";
	}
	
	@RequestMapping("/usr/ticket/writeReview")
	@ResponseBody
	public String writeReview(Model model, String movieTitle, String body, double rate) {
		ticketService.writeReview(rq.getLoginedMemberId(), movieTitle, body, rate);
		model.addAttribute("closeWindow", false);
		return "<script>alert('리뷰가 작성되었습니다.'); window.close();</script>";
	}

}
