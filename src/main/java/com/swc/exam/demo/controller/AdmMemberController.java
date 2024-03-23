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
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Board;
import com.swc.exam.demo.vo.Member;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdmMemberController {

	// @Autowired
	private MemberService memberService;
	private GenFileService genFileService;

	private Rq rq;

	public AdmMemberController(MemberService memberService, GenFileService genFileService, Rq rq) {
		this.memberService = memberService;
		this.genFileService = genFileService;
		this.rq = rq;
	}

	@RequestMapping("/adm/member/list")
	public String showList(Model model, @RequestParam(defaultValue = "3") int authLevel,
			@RequestParam(defaultValue = "loginId, name, nickname") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "1") int page) {

		int membersCount = memberService.getMembersCount(authLevel, searchKeywordTypeCode, searchKeyword);

		int itemsCountInAPage = 10;
		int pagesCount = (int) Math.ceil((double) membersCount / itemsCountInAPage);
		List<Member> members = memberService.getForPrintMembers(authLevel, searchKeywordTypeCode, searchKeyword,
				itemsCountInAPage, page);

		model.addAttribute("authLevel", authLevel);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);

		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);

		model.addAttribute("members", members);
		model.addAttribute("membersCount", membersCount);

		return "adm/member/list";
	}

	@RequestMapping("/adm/member/doDelete")
	@ResponseBody
	public String doDelete(@RequestParam(defaultValue = "") String ids,
			@RequestParam(defaultValue = "/adm/member/list") String replaceUri) {
		List<Integer> memberIds = new ArrayList<>();

		for (String idStr : ids.split(",")) {
			memberIds.add(Integer.parseInt(idStr));
		}
		
		memberService.deleteMembers(memberIds);
		

		return rq.jsReplace("해당 회원이 삭제되었습니다.", replaceUri);
	}

}
