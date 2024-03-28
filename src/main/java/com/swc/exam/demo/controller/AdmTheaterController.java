package com.swc.exam.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.swc.exam.demo.service.CinemaService;
import com.swc.exam.demo.service.MovieService;
import com.swc.exam.demo.service.TheaterService;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;
import com.swc.exam.demo.vo.Theater;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdmTheaterController {

	private TheaterService theaterService;
	private CinemaService cinemaService;
	private MovieService movieService;
	private Rq rq;

	public AdmTheaterController(TheaterService theaterService, CinemaService cinemaService, MovieService movieService, Rq rq) {
		this.theaterService = theaterService;
		this.cinemaService = cinemaService;
		this.movieService = movieService;
		this.rq = rq;
	}

	@RequestMapping("/adm/theater/add")
	public String showAdd(Model model, int id) {
		Cinema cinema = cinemaService.getCinemaById(id);
		model.addAttribute("cinema", cinema);

		return "adm/theater/add";
	}

	@RequestMapping("/adm/theater/doAdd")
	@ResponseBody
	public String doAdd(String region, int id, String theaterName, char seatId, String seatNo) {
		String seatStatus = "일반";
		ResultData addRd = theaterService.add(region, id, theaterName, seatId, seatNo, seatStatus);

		if (addRd.isFail()) {
			return rq.jsHistoryBack(addRd.getResultCode(), addRd.getMsg());
		}

		return rq.jsReplace(addRd.getMsg(), "/adm/cinema/detail?id=" + id);
	}

	@RequestMapping("/adm/theater/doDelete")
	@ResponseBody
	public String doDelete(String relTypeCode, String theaterName, @RequestParam(defaultValue = "/adm/cinema/detail") String replaceUri) {
		int id = theaterService.getTheaterId(relTypeCode, theaterName);
		theaterService.deleteTheater(relTypeCode, theaterName);

		return rq.jsReplace("해당 상영관이 삭제되었습니다.", replaceUri+"?id="+id);
	}

	@RequestMapping("/adm/theater/detail")
	public String showDetail(Model model, String relTypeCode, String theaterName) {
		List<Theater> theaters = theaterService.getForPrintTheater(relTypeCode, theaterName);
		model.addAttribute("theaters", theaters);
		model.addAttribute("theaterName", theaters.get(0).getTheaterName());

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
			seatNos[i] = i+1;
		}

		model.addAttribute("seatNoArr", seatNos);
		model.addAttribute("lastSeatNo", lastSeatNo);

		

		return "adm/theater/detail";
	}

	@RequestMapping("/adm/theater/doModify")
	@ResponseBody
	public String doModify(String theaterName, String relTypeCode, String[] seats, String seatStatus,
			String replaceUri) {
		theaterService.modifySeat(theaterName, relTypeCode, seats, seatStatus);

		return rq.jsReplace("좌석정보가 수정되었습니다",
				"/adm/theater/detail?relTypeCode=" + relTypeCode + "&theaterName=" + theaterName);
	}
	

	@RequestMapping("/adm/theater/addTime")
	public String showAddTime(Model model, String relTypeCode, String theaterName) {
		List<Theater> theaters = theaterService.getForPrintTheater(relTypeCode, theaterName);
		model.addAttribute("theaters", theaters);
		model.addAttribute("theaterName", theaters.get(0).getTheaterName());
		model.addAttribute("region", theaters.get(0).getRelTypeCode());
		List<Movie> movies = movieService.getForPrintPlayingMovies();
		model.addAttribute("movies", movies);
		
		return "adm/theater/addTime";
	}
	

	@RequestMapping("/adm/theater/doAddTime")
	@ResponseBody
	public String doAddTime(String replaceUri, String theaterName, String region, int movieId, String date, int time, String startTime, String endTime) {
		int id = theaterService.getTheaterId(region, theaterName);

		ResultData addRd = theaterService.addTime(theaterName, region, movieId, date, time, startTime, endTime);

		if (addRd.isFail()) {
			return rq.jsHistoryBack(addRd.getResultCode(), addRd.getMsg());
		}

		return rq.jsReplace(addRd.getMsg(), "/adm/theater/detail?id="+id+"&relTypeCode="+region+"&theaterName="+theaterName);
		
		
	}
}
