package com.swc.exam.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.swc.exam.demo.vo.Rq;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class NeedAdminInterceptor implements HandlerInterceptor {

	private Rq rq;

	public NeedAdminInterceptor(Rq rq){
		this.rq = rq;
}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

		if (!rq.isAdmin()) {
			if( rq.isAjax()) {
				resp.setContentType("application/json; charset=UTF-8");
				rq.print("{\"resultCode\":\"F-A\", \"msg\":\"권한이 없습니다.\"}");
			} else {
				String afterLoginUri = rq.getAfterLoginUri();
				rq.printHistoryBackJs("관리자 권한이 필요합니다.");
			}
			
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(req, resp, handler);
	}
}