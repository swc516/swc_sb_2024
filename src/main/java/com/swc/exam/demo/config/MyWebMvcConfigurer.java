package com.swc.exam.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.swc.exam.demo.interceptor.BeforeActionInterceptor;
import com.swc.exam.demo.interceptor.NeedLoginInterceptor;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
	// beforeActionInterceptor 인터셉터 불러오기
	@Autowired
	BeforeActionInterceptor beforeActionInterceptor;

	// needLoginInterceptor 인터셉터 불러오기
	@Autowired
	NeedLoginInterceptor needLoginInterceptor;

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
		;
	}
}
