package com.swc.exam.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swc.exam.demo.vo.Rq;


@Controller
public class AdmHomeController {
	@RequestMapping("/adm/home/main")
	public String showMain() {
		return "/adm/home/main";
	}
	
}

