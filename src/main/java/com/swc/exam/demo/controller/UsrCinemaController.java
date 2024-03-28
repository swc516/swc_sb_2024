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
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;
import com.swc.exam.demo.vo.Theater;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UsrCinemaController {

	private CinemaService cinemaService;
	private Rq rq;
	
	
	public UsrCinemaController(CinemaService cinemaService, Rq rq) {
		this.cinemaService = cinemaService;
		this.rq = rq;
	}

	@RequestMapping("/usr/cinema/list")
	public String showList(Model model, @RequestParam(defaultValue = "region") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "1") int page) {

		int cinemasCount = cinemaService.getCinemasCount(searchKeywordTypeCode, searchKeyword);

		int itemsCountInAPage = 10;
		int pagesCount = (int) Math.ceil((double) cinemasCount / itemsCountInAPage);
		List<Cinema> cinemas = cinemaService.getForPrintCinemas(searchKeywordTypeCode, searchKeyword,
				itemsCountInAPage, page);

		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);

		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);

		model.addAttribute("cinemas", cinemas);
		model.addAttribute("cinemasCount", cinemasCount);
		
		return "usr/cinema/list";
	}
	
	
	
}
