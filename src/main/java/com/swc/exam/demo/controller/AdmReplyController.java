package com.swc.exam.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swc.exam.demo.service.ArticleService;
import com.swc.exam.demo.service.ReplyService;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Reply;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;

@Controller
public class AdmReplyController {

	// @Autowired
	private ReplyService replyService;
	private ArticleService articleService;
	private Rq rq;

	public AdmReplyController(ReplyService replyService, ArticleService articleService, Rq rq) {
		this.replyService = replyService;
		this.articleService = articleService;
		this.rq = rq;
	}

	@RequestMapping("/adm/reply/list")
	public String showList(Model model,	@RequestParam(defaultValue = "memberId, body") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "1") int page) {
		
		if(searchKeywordTypeCode.equals("memberId")) {
			searchKeyword = replyService.getMemberId(searchKeyword);
		}
		
		
		int replysCount = replyService.getReplysCount(searchKeywordTypeCode, searchKeyword);

		int itemsCountInAPage = 10;
		int pagesCount = (int) Math.ceil((double) replysCount / itemsCountInAPage);
		List<Reply> replys = replyService.getForPrintReplys(searchKeyword,
				searchKeywordTypeCode, itemsCountInAPage, page);

		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("replys", replys);
		model.addAttribute("replysCount", replysCount);

		return "adm/reply/list";
	}


	@RequestMapping("/adm/reply/doDelete")
	@ResponseBody
	public String doDelete(@RequestParam(defaultValue = "") String ids,
			@RequestParam(defaultValue = "/adm/member/list") String replaceUri) {
		List<Integer> replyIds = new ArrayList<>();

		for (String idStr : ids.split(",")) {
			replyIds.add(Integer.parseInt(idStr));
		}
		
		replyService.deleteReplys(replyIds);
		

		return rq.jsReplace("해당 댓글이 삭제되었습니다.", replaceUri);
	}

}
