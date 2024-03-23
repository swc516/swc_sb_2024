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

import com.swc.exam.demo.service.GenFileService;
import com.swc.exam.demo.service.MemberService;
import com.swc.exam.demo.service.MovieService;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Member;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;

@Controller
public class AdmMovieController {
	
	private MovieService movieService;
	private GenFileService genFileService;
	private Rq rq;
	
	public AdmMovieController(MovieService movieService, GenFileService genFileService, Rq rq) {
		this.movieService = movieService;
		this.genFileService = genFileService;
		this.rq = rq;
	}
	
	@RequestMapping("/adm/movie/list")
	public String showList(Model model,	@RequestParam(defaultValue = "title, body") 
	String searchKeywordTypeCode, @RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "1") int page) {
		
		int moviesCount = movieService.getMoviesCount(searchKeywordTypeCode, searchKeyword);

		int itemsCountInAPage = 10;
		int pagesCount = (int) Math.ceil((double) moviesCount / itemsCountInAPage);
		List<Movie> movies = movieService.getForPrintMovies(searchKeywordTypeCode, searchKeyword,
				itemsCountInAPage, page);
		
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);

		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);

		model.addAttribute("movies", movies);
		model.addAttribute("moviesCount", moviesCount);

		
		return "adm/movie/list";
	}
	
	
	@RequestMapping("/adm/movie/add")
	public String showAdd() {
		return "adm/movie/add";
	}
	
	@RequestMapping("/adm/movie/doAdd")
	@ResponseBody
	public String doAdd(String title, String body, String runDate, MultipartRequest multipartRequest) {
		ResultData addRd = movieService.add(title, body, runDate);
		
		if (addRd.isFail()) {
			return rq.jsHistoryBack(addRd.getResultCode(), addRd.getMsg());
		}
		
		int newMovieId = (int) addRd.getBody().get("id");

		
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, newMovieId);
			}
		}
		
		return rq.jsReplace(addRd.getMsg(), "/adm/movie/list");
	}
	
	
	@RequestMapping("/adm/movie/doDelete")
	@ResponseBody
	public String doDelete(@RequestParam(defaultValue = "") String ids,
			@RequestParam(defaultValue = "/adm/member/list") String replaceUri) {
		List<Integer> movieIds = new ArrayList<>();

		for (String idStr : ids.split(",")) {
			movieIds.add(Integer.parseInt(idStr));
		}
		
		movieService.deleteMovies(movieIds);
		

		return rq.jsReplace("해당 영화가 삭제되었습니다.", replaceUri);
	}

}
