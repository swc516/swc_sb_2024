package com.swc.exam.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swc.exam.demo.service.ArticleService;
import com.swc.exam.demo.service.BoardService;
import com.swc.exam.demo.service.ReactionPointService;
import com.swc.exam.demo.service.ReplyService;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Rq;

@Controller
public class AdmArticleController {
	private ArticleService articleService;
	private BoardService boardService;
	private ReactionPointService reactionPointService;
	private ReplyService replyService;
	private Rq rq;

	public AdmArticleController(ArticleService articleService, BoardService boardService,
			ReactionPointService reactionPointService, ReplyService replyService, Rq rq) {
		this.articleService = articleService;
		this.boardService = boardService;
		this.reactionPointService = reactionPointService;
		this.replyService = replyService;
		this.rq = rq;

	}

	@RequestMapping("/adm/article/list")
	public String showList(Model model,	@RequestParam(defaultValue = "boardId, memberId, title, body") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "1") int page) {
		if(searchKeywordTypeCode.equals("boardId")) {
			if(searchKeyword.equals("공지사항")) {
				searchKeyword = "1";
			}
			
			if(searchKeyword.equals("자유게시판")) {
				searchKeyword = "2";
			}
		}
		
		
		if(searchKeywordTypeCode.equals("memberId")) {
			searchKeyword = articleService.getMemberId(searchKeyword);
		}
		
		
		int articlesCount = articleService.getArticlesCount(0, searchKeywordTypeCode, searchKeyword);

		int itemsCountInAPage = 10;
		int pagesCount = (int) Math.ceil((double) articlesCount / itemsCountInAPage);
		List<Article> articles = articleService.getForPrintArticles(rq.getLoginedMemberId(), 0, searchKeyword,
				searchKeywordTypeCode, itemsCountInAPage, page);

		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("articles", articles);
		model.addAttribute("articlesCount", articlesCount);

		return "adm/article/list";
	}


	@RequestMapping("/adm/article/doDelete")
	@ResponseBody
	public String doDelete(@RequestParam(defaultValue = "") String ids,
			@RequestParam(defaultValue = "/adm/member/list") String replaceUri) {
		List<Integer> articleIds = new ArrayList<>();

		for (String idStr : ids.split(",")) {
			articleIds.add(Integer.parseInt(idStr));
		}
		
		articleService.deleteArticles(articleIds);
		

		return rq.jsReplace("해당 게시글이 삭제되었습니다.", replaceUri);
	}


}
