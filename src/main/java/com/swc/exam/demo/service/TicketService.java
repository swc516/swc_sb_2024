package com.swc.exam.demo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.TicketRepository;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;
import com.swc.exam.demo.vo.TheaterInfo;
import com.swc.exam.demo.vo.TheaterTime;
import com.swc.exam.demo.vo.Ticket;

@Service
public class TicketService {

	private TicketRepository ticketRepository;
	private MovieService movieService;
	private CinemaService cinemaService;
	private Rq rq;

	public TicketService(TicketRepository ticketRepository, MovieService movieService, CinemaService cinemaService,  Rq rq) {
		this.ticketRepository = ticketRepository;
		this.movieService = movieService;
		this.cinemaService = cinemaService;
		this.rq = rq;
	}


	public List<TheaterTime> getForPrintTheaterTimes(int cinemaId, int theaterInfoId, int theaterTimeId) {
		List<TheaterTime> theaterTimes = cinemaService.getForPrintTheaterTime(cinemaId, theaterInfoId, theaterTimeId);
		return theaterTimes;
	}

	
	public ResultData doTicketing(int theaterInfoId, int theaterTimeId, String[] seats, int memberId, String movieTitle, String cinema, String theater, int time, String playingTime) {
		for (String seat : seats) {
			String[] seatSplit1 = seat.split("_");
			String seatInfo = seatSplit1[1];
			String[] seatSplit2 = seatInfo.split("-");
			char seatRow = seatSplit2[0].trim().charAt(0);
			int seatCol = Integer.parseInt(seatSplit2[1]);
			cinemaService.doTicketing(theaterInfoId, theaterTimeId, seatRow, seatCol, memberId);
		}
		String totalSeat = Arrays.toString(seats).substring(1,Arrays.toString(seats).trim().length()-1);
		ticketRepository.doTicketing(memberId, movieTitle, cinema, theater, time, playingTime, totalSeat);
		
		
		return ResultData.from("S-1", "예매가 완료되었습니다.");
	}


	public List<Ticket> getMyTicketingList(int id) {
		List<Ticket> lists = ticketRepository.getMyTicketingList(id);
		return lists;
	}


	public void doTicketCancel(int id) {
		ticketRepository.doTicketCancel(id);
	}


	public List<Movie> getMovieList() {
		List<Movie> movies = movieService.getPlayingMovies();
		return movies;
	}


	public List<Cinema> getCinemaList() {
		List<Cinema> cinemas = cinemaService.getCinemaList();
		return cinemas;
	}


	public List<TheaterInfo> getTheaterList() {
		List<TheaterInfo> theaters = cinemaService.getCinemaTheaterList(5);
		return theaters;
	}


	public List<TheaterTime> getTheaterTimeList(int movieId, int cinemaId, String date) {
		List<TheaterTime> theaterTimes = cinemaService.getTheaterTimeList(movieId, cinemaId, date);
		for(TheaterTime theaterTime : theaterTimes) {
			theaterTime.setExtra__sellSeatCount(cinemaService.getSellSeatCount(theaterTime.getTheaterTimeId(), date, theaterTime.getTheaterTime()));
			theaterTime.setExtra__maxSeatCount(cinemaService.getMaxSeatCount(theaterTime.getTheaterTimeId(), date, theaterTime.getTheaterTime()));
		}
		return theaterTimes;
	}


	public List<String> getForPrintWeek() {
		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat dFormat = new SimpleDateFormat("YY-MM-dd");
		List<String> week = new ArrayList<>();
		for(int i = 1; i<=7;i++) {
		String date = dFormat.format(cal.getTime());
		week.add(date);
		cal.add(Calendar.DATE, 1);
		}
		
		return week;
	}


	public String getMovieTitleById(int movieId) {
		String movieTitle = movieService.getMovieTitleById(movieId);
		return movieTitle;
	}


	public String getCinemaRegionById(int cinemaId) {
		String cinemaRegion = cinemaService.getCinemaRegionById(cinemaId);
		return cinemaRegion;
	}


	public String getCinemaBranchById(int cinemaId) {
		String cinemaBranch = cinemaService.getCinemaBranchById(cinemaId);
		return cinemaBranch;
	}


	public String getTheaterById(int theaterInfoId) {
		String theater = cinemaService.getTheaterById(theaterInfoId);
		return theater;
	}


	public List<TheaterInfo> getForPrintTheaterInfo(String cinema, String theater) {
		List<TheaterInfo> theaterInfos = cinemaService.getForPrintTheaterInfo(cinema, theater);
		return theaterInfos;
	}


	public Ticket getTicketById(int ticketId) {
		Ticket ticket = ticketRepository.getTicketById(ticketId);
		return ticket;
	}

}
