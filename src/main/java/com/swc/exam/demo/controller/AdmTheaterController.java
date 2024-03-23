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
import com.swc.exam.demo.service.TheaterService;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdmTheaterController {

	private TheaterService theaterService;
	private CinemaService cinemaService;
	private Rq rq;
	
	
	public AdmTheaterController(TheaterService theaterService, CinemaService cinemaService, Rq rq) {
		this.theaterService = theaterService;
		this.cinemaService = cinemaService;
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
	public String doAdd(String region, int id, String theater, char seatId, int seatNo) {
		
		ResultData addRd = theaterService.add(region, id, theater, seatId, seatNo);
		
		if (addRd.isFail()) {
			return rq.jsHistoryBack(addRd.getResultCode(), addRd.getMsg());
		}

		
		return rq.jsReplace(addRd.getMsg(), "/adm/cinema/detail?id="+id);
	}

	@RequestMapping("/adm/theater/doDelete")
	@ResponseBody
	public String doDelete(String Theater, @RequestParam(defaultValue = "/adm/cinema/list") String replaceUri) {
		
		theaterService.deleteTheater(Theater);

		return rq.jsReplace("해당 상영관이 삭제되었습니다.", replaceUri);
	}
	
	/*
	@RequestMapping("/adm/theater/modify")
	public String showModify(Model model, int id) {
		return "/adm/theater/modify";
	}

	@RequestMapping("/adm/theater/doModify")
	@ResponseBody
	public String doModify(int id, String region) {
		ResultData modifyRd = theaterService.modify(id, region);
		return rq.jsReplace(modifyRd.getMsg(), "/adm/theater/list");
	}
	
	@RequestMapping("/adm/theater/detail")
	public String doModify(Model model, int id) {
		Cinema cinema = cinemaService.getForPrintCinema(id);
		model.addAttribute("cinema", cinema);
		
		return "/adm/theater/detail";
	}
	
	*/
	
}
