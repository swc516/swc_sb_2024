package com.swc.exam.demo.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.TheaterTimeRepository;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.Rq;
import com.swc.exam.demo.vo.Theater;
import com.swc.exam.demo.vo.TheaterTime;

@Service
public class TicketService {

	private TheaterTimeRepository theaterTimeRepository;
	private MovieService movieService;
	private CinemaService cinemaService;
	private TheaterService theaterService;
	private Rq rq;

	public TicketService(TheaterTimeRepository theaterTimeRepository, MovieService movieService, CinemaService cinemaService, TheaterService theaterService, Rq rq) {
		this.theaterTimeRepository = theaterTimeRepository;
		this.movieService = movieService;
		this.cinemaService = cinemaService;
		this.theaterService = theaterService;
		this.rq = rq;
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


	public List<TheaterTime> getMyTicketingList(int id) {
		List<TheaterTime> lists = theaterTimeRepository.getMyTicketingList(id);
		return lists;
	}


	public void doTicketCancel(int id) {
		
		theaterTimeRepository.doTicketCancel(id);
		
	}


	public List<Movie> getMovieList() {
		List<Movie> movies = movieService.getForPrintPlayingMovies();
		return movies;
	}


	public List<Cinema> getCinemaList() {
		List<Cinema> cinemas = cinemaService.getCinemaList();
		return cinemas;
	}


	public List<Theater> getTheaterList() {
		List<Theater> theaters = theaterService.getTheaterList();
		return theaters;
	}


	public List<TheaterTime> getTheaterTimeList(int movieId, String region, String date) {
		List<TheaterTime> theaterTimes = theaterTimeRepository.getTheaterTimeList(movieId, region, date);
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

}
