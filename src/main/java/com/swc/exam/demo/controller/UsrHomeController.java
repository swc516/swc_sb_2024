package com.swc.exam.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swc.exam.demo.service.MovieService;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Movie;

import lombok.Getter;


@Controller
public class UsrHomeController {
	
	MovieService movieService;
	
	public UsrHomeController (MovieService movieService) {
		this.movieService = movieService;
	}
	
	@RequestMapping("/usr/home/main")
	public String showMain(Model model) {
		List<Movie> movieList = movieService.getPlayingMovies();		
		model.addAttribute("movieList", movieList);
		
		return "usr/home/main";
	}
	
	@RequestMapping("/")
	public String showRoot() {
		return "redirect:/usr/home/main";
	}

	
	
}

