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
public class AdmCinemaController {

	private CinemaService cinemaService;
	private Rq rq;
	
	
	public AdmCinemaController(CinemaService cinemaService, Rq rq) {
		this.cinemaService = cinemaService;
		this.rq = rq;
	}

	@RequestMapping("/adm/cinema/list")
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
		
		return "adm/cinema/list";
	}
	
	
	
	@RequestMapping("/adm/cinema/add")
	public String showAdd() {
		return "adm/cinema/add";
	}
	
	@RequestMapping("/adm/cinema/doAdd")
	@ResponseBody
	public String doAdd(String region) {
		ResultData addRd = cinemaService.add(region);
		
		if (addRd.isFail()) {
			return rq.jsHistoryBack(addRd.getResultCode(), addRd.getMsg());
		}

		
		return rq.jsReplace(addRd.getMsg(), "/adm/cinema/list");
	}

	@RequestMapping("/adm/cinema/doDelete")
	@ResponseBody
	public String doDelete(@RequestParam(defaultValue = "") String ids,
			@RequestParam(defaultValue = "/adm/cinema/list") String replaceUri) {
		
		List<Integer> cinemaIds = new ArrayList<>();

		for (String idStr : ids.split(",")) {
			cinemaIds.add(Integer.parseInt(idStr));
		}
		
		cinemaService.deleteCinemas(cinemaIds);

		return rq.jsReplace("해당 영화관이 삭제되었습니다.", replaceUri);
	}
	
	
	@RequestMapping("/adm/cinema/modify")
	public String showModify(Model model, int id) {
		Cinema cinema = cinemaService.getForPrintCinema(id);

		if (cinema == null) {
			return rq.historyBackJsOnview(Ut.f("%d번 영화관이 존재하지 않습니다.", id));
		}
		
		model.addAttribute("cinema", cinema);

		return "/adm/cinema/modify";
	}

	@RequestMapping("/adm/cinema/doModify")
	@ResponseBody
	public String doModify(int id, String region) {

		ResultData modifyRd = cinemaService.modify(id, region);

		return rq.jsReplace(modifyRd.getMsg(), "/adm/cinema/list");
	}
	
	@RequestMapping("/adm/cinema/detail")
	public String showDetail(Model model, int id) {
		Cinema cinema = cinemaService.getForPrintCinema(id);
		model.addAttribute("cinema", cinema);
		
		String relTypeCode = cinema.getRegion();
		
		List<Theater> theaters = cinemaService.getForPrintTheaters(relTypeCode);
		model.addAttribute("theaters", theaters);
		
		return "/adm/cinema/detail";
	}
	
}
