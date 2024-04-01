package com.swc.exam.demo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.Rq;
import com.swc.exam.demo.vo.TheaterInfo;
import com.swc.exam.demo.vo.TheaterTime;

@Service
public class TicketService {

	private MovieService movieService;
	private CinemaService cinemaService;
	private Rq rq;

	public TicketService( MovieService movieService, CinemaService cinemaService,  Rq rq) {
		this.movieService = movieService;
		this.cinemaService = cinemaService;
		this.rq = rq;
	}


	public List<TheaterTime> getForPrintTheaterTimes(int cinemaId, int theaterInfoId, String date, int theaterTime) {
		List<TheaterTime> theaterTimes = cinemaService.getForPrintTheaterTime(cinemaId, theaterInfoId, date, theaterTime);
		return theaterTimes;
	}

	/*
	public void doTicketing(String region, String theaterName, String date, int time, String[] seats, int memberId) {
		for (String seat : seats) {
			String[] seatSplit = seat.split("-");
			char seatId = seatSplit[0].trim().charAt(0);
			int seatNo = Integer.parseInt(seatSplit[1]);
			theaterTimeRepository.doTicketing(region, theaterName, date, time, seatId, seatNo, memberId);
		}
	}


	public List<TheaterTime> getMyTicketingList(int id) {
		List<TheaterTime> lists = theaterTimeRepository.getMyTicketingList(id);
		return lists;
	}


	public void doTicketCancel(int id) {
		
		theaterTimeRepository.doTicketCancel(id);
		
	}
*/

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

}
