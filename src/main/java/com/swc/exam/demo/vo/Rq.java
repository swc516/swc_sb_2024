package com.swc.exam.demo.vo;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.swc.exam.demo.service.MemberService;
import com.swc.exam.demo.util.Ut;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {
	@Getter
	private boolean isAjax;
	
	@Getter
	private boolean isLogined;
	
	@Getter
	private int loginedMemberId;
	
	@Getter
	private Member loginedMember;
	
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private HttpSession session;
	private Map<String, String> paramMap;
	
	public Rq(HttpServletRequest req, HttpServletResponse resp, MemberService memberService) {
		this.req = req;
		this.resp = resp;
		
		paramMap = Ut.getParamMap(req);
		
		this.session = req.getSession();
		
		boolean isLogined = false;
		int loginedMemberId = 0;
		Member loginedMember = null;
		
		if ( session.getAttribute("loginedMemberId") != null ) {
			isLogined = true;
			loginedMemberId = (int) session.getAttribute("loginedMemberId");
			loginedMember = memberService.getMemberById(loginedMemberId);
		}
		
		this.isLogined = isLogined;
		this.loginedMemberId = loginedMemberId;
		this.loginedMember = loginedMember;
			

		String requestUri = req.getRequestURI();

		// 해당 요청이 ajax 요청인지 아닌지 체크
		boolean isAjax = requestUri.endsWith("Ajax");

		if (isAjax == false) {
			if (paramMap.containsKey("ajax") && paramMap.get("ajax").equals("Y")) {
				isAjax = true;
			}
			else if (paramMap.containsKey("isAjax") && paramMap.get("isAjax").equals("Y")) {
				isAjax = true;
			}
		}
		if (isAjax == false) {
			if (requestUri.contains("/get")) {
				isAjax = true;
			}
		}

		this.isAjax = isAjax;
		
	}

	public void printHistoryBackJs(String msg) {
		resp.setContentType("text/html; charset=UTF-8");
		print(Ut.jsHistoryBack(msg));
	}


	public void print(String str) {
		try {
			resp.getWriter().append(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean isNotLogined() {
		return !isLogined;
		
	}
	
	public void println(String str) {
		print(str + "\n");
	}

	public void login(Member member) {
		session.setAttribute("loginedMemberId", member.getId());
	}

	public void logout() {
		session.removeAttribute("loginedMemberId");
		
	}


	public String historyBackJsOnview(String resultCode, String msg) {
		req.setAttribute("msg", String.format("[%s] %s", resultCode, msg));
		req.setAttribute("historyBack", true);
		return "common/js";
	}

	public String jsHistoryBack(String resultCode, String msg) {
		msg = String.format("[%s] %s", resultCode, msg);
		return Ut.jsHistoryBack(msg);
	}
	
	public String historyBackJsOnview(String msg) {
		req.setAttribute("msg", msg);
		req.setAttribute("historyBack", true);
		return "common/js";
	}
	
	public String jsHistoryBack(String msg) {
		return Ut.jsHistoryBack(msg);
	}

	public String jsReplace(String msg, String uri) {
		return Ut.jsReplace(msg, uri);
	}
	
	public String getCurrentUri() {
		String currentUri = req.getRequestURI();
        String queryString = req.getQueryString();

        if (queryString != null && queryString.length() > 0) {
            currentUri += "?" + queryString;
        }
		
		return currentUri;
	}
	
	public String getEncodedCurrentUri() {
		return Ut.getUriEncoded(getCurrentUri());
	}

	public void printReplaceJs(String msg, String uri) {
		resp.setContentType("text/html; charset=UTF-8");
		print(Ut.jsReplace(msg, uri));
	}
	
	public String getJoinUri() {
		return "/usr/member/join?afterJoinUri=" + getAfterJoinUri();
	}
	
	public String getLoginUri() {
		return "/usr/member/login?afterLoginUri=" + getAfterLoginUri();
	}
	
	public String getLogoutUri() {
		return "/usr/member/doLogout?afterLogoutUri=" + getAfterLogoutUri();
	}

	public String getFindLoginIdUri() {	
		return "/usr/member/findLoginId?afterFindLoginIdUri=" + getAfterFindLoginIdUri();
	}

	public String getFindLoginPwUri() {	
		return "/usr/member/findLoginPw?afterFindLoginPwUri=" + getAfterFindLoginPwUri();
	}

	public String getAfterFindLoginIdUri() {
		String requestUri = req.getRequestURI();

		return getEncodedCurrentUri();
	}

	public String getAfterFindLoginPwUri() {
		String requestUri = req.getRequestURI();

		return getEncodedCurrentUri();
	}
	
	
	public String getAfterLoginUri() {
		String requestUri = req.getRequestURI();
		
		// 로그인 후 돌아가면 안되는 URI을 적기
		switch(requestUri) {
		case "/usr/member/login":
		case "/usr/member/join":
		case "/usr/member/findLoginId":
		case "/usr/member/findLoginPw":
		case "/adm/member/list":
			return Ut.getUriEncoded(Ut.getStrAttr(paramMap, "afterLoginUri", ""));
		}
		
		return getEncodedCurrentUri();
	}
	
	public String getAfterLogoutUri() {
		String requestUri = req.getRequestURI();
		
		/* 필요하면 활성화 ex) 작성 -> 로그아웃 -> 로그인 -> 작성을 작성에서 로그아웃하면 메인으로.
		switch(requestUri) {
		case "/usr/article/write":
			return Ut.getUriEncoded(Ut.getStrAttr(paramMap, "afterLogoutUri", ""));
		}
		*/
		
		return getEncodedCurrentUri();
	}
	public String getAfterJoinUri() {
		String requestUri = req.getRequestURI();
		// 회원가입 후 돌아가면 안되는 URI을 적기
		switch(requestUri) {
		case "/usr/member/login":
		case "/usr/member/join":
			return Ut.getUriEncoded(Ut.getStrAttr(paramMap, "afterJoinUri", ""));
		}
		return getEncodedCurrentUri();
	}

	public String getArticleDetailUriFromArticleList(Article article) {
		return "../article/detail?id=" + article.getId() + "&listUri=" + getEncodedCurrentUri();
	}
	
	public String getProfileImgUri(int memberId) {
		return "/common/genFile/file/member/" + memberId + "/extra/profileImg/1";
	}
	
	public String getProfileFallbackImgUri() {
		return "https://via.placeholder.com/300/?text=^_^";
	}
	
	public String getProfileFallbackImgOnErrorHtml() {
		return "this.src='" + getProfileFallbackImgUri() + "'";
	}
	
	public String getRemoveProfileImgIfNotExitOnErrorHtmlAttr() {
		return "$(this).remove()";
	}

	public boolean isAdmin() {
		if (isLogined == false) {
			return false;
		}
		return loginedMember.isAdmin();
	}
	
	public String getMoviePosterImgUri(int movidId) {
		return "/common/genFile/file/movie/" + movidId + "/extra/posterImg/1";
	}
	
	public String getMoviePosterFallbackImgUri() {
		return "https://via.placeholder.com/300/?text=^_^";
	}
	
	public String getMoviePosterFallbackImgOnErrorHtml() {
		return "this.src='" + getMoviePosterFallbackImgUri() + "'";
	}
	


}
