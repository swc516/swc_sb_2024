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
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Member;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UsrMovieController {
	
	private MovieService movieService;
	private GenFileService genFileService;
	private Rq rq;
	
	public UsrMovieController(MovieService movieService, GenFileService genFileService, Rq rq) {
		this.movieService = movieService;
		this.genFileService = genFileService;
		this.rq = rq;
	}
	
	@RequestMapping("/usr/movie/list")
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

		
		return "usr/movie/list";
	}
	
}
