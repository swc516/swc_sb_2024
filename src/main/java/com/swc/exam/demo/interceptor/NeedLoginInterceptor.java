package com.swc.exam.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.swc.exam.demo.vo.Rq;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class NeedLoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		Rq rq = (Rq) req.getAttribute("rq"); // MyWebMvcConfigurer 순서 상 밑에 있기 때문에 가능
		
		if ( !rq.isLogined()) {
			rq.printHistoryBackJs("로그인 후 이용해주세요.");
			return false;
		}

		return HandlerInterceptor.super.preHandle(req, resp, handler);
	}
}