package com.swc.exam.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.swc.exam.demo.interceptor.BeforeActionInterceptor;
import com.swc.exam.demo.interceptor.NeedAdminInterceptor;
import com.swc.exam.demo.interceptor.NeedLoginInterceptor;
import com.swc.exam.demo.interceptor.NeedLogoutInterceptor;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
	// beforeActionInterceptor 인터셉터 불러오기
	@Autowired
	BeforeActionInterceptor beforeActionInterceptor;

	// needLoginInterceptor 인터셉터 불러오기
	@Autowired
	NeedLoginInterceptor needLoginInterceptor;
	
	// needLogoutInterceptor 인터셉터 불러오기
	@Autowired
	NeedLogoutInterceptor needLogoutInterceptor;
	
	// needAdminInterceptor 인터셉터 불러오기
	@Autowired
	NeedAdminInterceptor needAdminInterceptor;

	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/gen/**").addResourceLocations("file:///" + genFileDirPath + "/")
				.setCachePeriod(20);
	}

	
	
	
	// 이 함수는 인터셉터를 적용하는 역할을 합니다.
	// resource/common.css 이런거는 제외
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(beforeActionInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/resource/**")
		.excludePathPatterns("/error")
		;

		registry.addInterceptor(needLoginInterceptor)
		.addPathPatterns("/usr/member/myPage")
		.addPathPatterns("/usr/member/checkPassword")
		.addPathPatterns("/usr/member/doCheckPassword")
		.addPathPatterns("/usr/member/modify")
		.addPathPatterns("/usr/member/doModify")
		.addPathPatterns("/usr/reply/write")
		.addPathPatterns("/usr/reply/doWrite")
		.addPathPatterns("/usr/reply/modify")
		.addPathPatterns("/usr/reply/doModify")
		.addPathPatterns("/usr/reply/doDelete")
		.addPathPatterns("/usr/article/write")
		.addPathPatterns("/usr/article/doWrite")
		.addPathPatterns("/usr/article/modify")
		.addPathPatterns("/usr/article/doModify")
		.addPathPatterns("/usr/reactionPoint/doGoodReaction")
		.addPathPatterns("/usr/reactionPoint/doBadReaction")
		.addPathPatterns("/usr/reactionPoint/doCancelGoodReaction")
		.addPathPatterns("/usr/reactionPoint/doCancelBadReaction")
		.addPathPatterns("/usr/ticket/ticketing")
		.addPathPatterns("/usr/ticket/doTicketing")
		.addPathPatterns("/usr/member/myTicketingList")
		.addPathPatterns("/usr/member/doTicketCancel")
		
		
		.addPathPatterns("/adm/**")

		;
		
		registry.addInterceptor(needLogoutInterceptor)
		.addPathPatterns("/usr/member/join")
		.addPathPatterns("/usr/member/getLoginIdDup")
		.addPathPatterns("/usr/member/doJoin")
		.addPathPatterns("/usr/member/login")
		.addPathPatterns("/usr/member/doLogin")
		.addPathPatterns("/usr/member/findLoginId")
		.addPathPatterns("/usr/member/doFindLoginId")
		.addPathPatterns("/usr/member/findLoginPw")
		.addPathPatterns("/usr/member/doFindLoginPw")
		;
		
		registry.addInterceptor(needAdminInterceptor)
		.addPathPatterns("/adm/home/main")
		.addPathPatterns("/adm/member/list")
		.addPathPatterns("/adm/movie/list")
		.addPathPatterns("/adm/movie/add")
		.addPathPatterns("/adm/movie/doAdd")
		.addPathPatterns("/adm/movie/modify")
		.addPathPatterns("/adm/movie/doModify")
		.addPathPatterns("/adm/movie/doDelete")
		.addPathPatterns("/adm/cinema/add")
		.addPathPatterns("/adm/cinema/doAdd")
		.addPathPatterns("/adm/cinema/detail")
		.addPathPatterns("/adm/cinema/list")
		.addPathPatterns("/adm/cinema/modify")
		.addPathPatterns("/adm/cinema/doModify")
		.addPathPatterns("/adm/cinema/doDelete")
		.addPathPatterns("/adm/theater/add")
		.addPathPatterns("/adm/theater/doAdd")
		.addPathPatterns("/adm/theater/doDelete")
		.addPathPatterns("/adm/theater/detail")
		.addPathPatterns("/adm/theater/doModify")
		;

	}
}
