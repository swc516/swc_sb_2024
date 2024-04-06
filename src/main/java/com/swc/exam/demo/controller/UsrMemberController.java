package com.swc.exam.demo.controller;

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
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Member;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;
import com.swc.exam.demo.vo.TheaterTime;
import com.swc.exam.demo.vo.Ticket;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UsrMemberController {

	// @Autowired
	private MemberService memberService;
	private GenFileService genFileService;

	private Rq rq;

	public UsrMemberController(MemberService memberService, GenFileService genFileService, Rq rq) {
		this.memberService = memberService;
		this.genFileService = genFileService;
		this.rq = rq;
	}

	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String name, String nickname, String cellphoneNo, String email,
			int favoriteCinema, @RequestParam(defaultValue = "/") String afterLoginUri,
			MultipartRequest multipartRequest) {

		ResultData<Integer> joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email,
				favoriteCinema);

		if (joinRd.isFail()) {
			return rq.jsHistoryBack(joinRd.getResultCode(), joinRd.getMsg());
		}

		int newMemberId = (int) joinRd.getBody().get("id");

		String afterJoinUri = "../member/login?afterLoginUri=" + Ut.getUriEncoded(afterLoginUri);

		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, newMemberId);
			}
		}

		return rq.jsReplace("회원가입이 완료되었습니다. 로그인 후 이용해주세요.", afterJoinUri);
	}

	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, @RequestParam(defaultValue = "/") String afterLoginUri) {

		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			return rq.jsHistoryBack("존재하지 않는 로그인아이디 입니다.");
		}

		if (member.getLoginPw().equals(Ut.sha256(loginPw)) == false) {
			return rq.jsHistoryBack("비밀번호가 일치하지 않습니다.");
		}

		rq.login(member);

		String msg = Ut.f("%s님 환영합니다", member.getNickname());

		boolean isUsingTempPassword = memberService.isUsingTempPassword(member.getId());

		if (isUsingTempPassword) {
			msg = "임시 비밀번호를 변경해주세요";
			afterLoginUri = "/usr/member/myPage";
		}

		return rq.jsReplace(msg, afterLoginUri);
	}

	@RequestMapping("/usr/member/login")
	public String showLogin() {
		return "usr/member/login";
	}

	@RequestMapping("/usr/member/findLoginId")
	public String showFindLoginId() {
		return "usr/member/findLoginId";
	}

	@RequestMapping("/usr/member/doFindLoginId")
	@ResponseBody
	public String doFindLoginId(String name, String email,
			@RequestParam(defaultValue = "/") String afterFindLoginIdUri) {

		Member member = memberService.getMemberByNameAndEmail(name, email);

		if (member == null) {
			return rq.jsHistoryBack("가입된 정보가 없습니다.");
		}

		return rq.jsReplace(Ut.f("회원님의 아이디는 [%s]입니다.", member.getLoginId()), afterFindLoginIdUri);

	}

	@RequestMapping("/usr/member/findLoginPw")
	public String showFindLoginPw() {
		return "usr/member/findLoginPw";
	}

	@RequestMapping("/usr/member/doFindLoginPw")
	@ResponseBody
	public String doFindLoginPw(String loginId, String email,
			@RequestParam(defaultValue = "/") String afterFindLoginPwUri) {

		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			return rq.jsHistoryBack("가입된 정보가 없습니다.");
		}

		if (member.getEmail().equals(email) == false) {
			return rq.jsHistoryBack("가입된 정보가 없습니다.");
		}

		ResultData notifyTempLoginPwByEmailRs = memberService.notifyTempLoginPwByEmail(member);

		return rq.jsReplace(notifyTempLoginPwByEmailRs.getMsg(), afterFindLoginPwUri);
	}

	@RequestMapping("/usr/member/join")
	public String showJoin(Model model) {
		List<Cinema> cinemas = memberService.getCinemaList();
		model.addAttribute("cinemas", cinemas);

		return "usr/member/join";
	}

	@RequestMapping("/usr/member/getLoginIdDup")
	@ResponseBody
	public ResultData getLoginIdDup(String loginId) {

		Member oldMember = memberService.getMemberByLoginId(loginId);

		if (oldMember != null) {
			return ResultData.from("F-A", "해당 아이디는 이미 사용중입니다.", "loginId", loginId);
		}

		return ResultData.from("S-A", "사용가능한 아이디입니다.", "loginId", loginId);

	}

	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public String doLogout(@RequestParam(defaultValue = "/") String afterLogoutUri) {
		rq.logout();

		return rq.jsReplace("로그아웃 되었습니다.", afterLogoutUri);
	}

	@RequestMapping("/usr/member/myPage")
	public String showMyPage() {
		return "usr/member/myPage";
	}

	@RequestMapping("/usr/member/checkPassword")
	public String showCheckPassword() {
		return "usr/member/checkPassword";
	}

	@RequestMapping("/usr/member/doCheckPassword")
	@ResponseBody
	public String doCheckPassword(String loginPw, String replaceUri) {

		if (rq.getLoginedMember().getLoginPw().equals(loginPw) == false) {
			return rq.jsHistoryBack("비밀번호가 일치하지 않습니다.");
		}

		if (replaceUri.equals("../member/modify")) {
			String memberModifyAuthKey = memberService.genMemberModifyAuthKey(rq.getLoginedMemberId());

			replaceUri += "?memberModifyAuthKey=" + memberModifyAuthKey;
		}

		return rq.jsReplace("", replaceUri);
	}

	@RequestMapping("/usr/member/modify")
	public String showModify(String memberModifyAuthKey, Model model) {
		if (Ut.empty(memberModifyAuthKey)) {
			return rq.historyBackJsOnview("memberModifyAuthKey(이)가 필요합니다.");
		}

		ResultData checkMemberModifyAuthKeyRd = memberService.checkMemberModifyAuthKey(rq.getLoginedMemberId(),
				memberModifyAuthKey);

		if (checkMemberModifyAuthKeyRd.isFail()) {
			return rq.historyBackJsOnview(checkMemberModifyAuthKeyRd.getMsg());
		}

		boolean hasImg = genFileService.hasImg("profileImg", rq.getLoginedMemberId());
		model.addAttribute("hasImg", hasImg);

		List<Cinema> cinemas = memberService.getCinemaList();
		model.addAttribute("cinemas", cinemas);

		return "usr/member/modify";
	}

	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public String doModify(HttpServletRequest request, String memberModifyAuthKey, String loginPw, String name,
			String nickname, String cellphoneNo, String email, int favoriteCinema, MultipartRequest multipartRequest,
			@RequestParam(defaultValue = "/") String afterLogoutUri) {

		if (Ut.empty(memberModifyAuthKey)) {
			return rq.jsHistoryBack("memberModifyAuthKey(이)가 필요합니다.");
		}

		ResultData checkMemberModifyAuthKeyRd = memberService.checkMemberModifyAuthKey(rq.getLoginedMemberId(),
				memberModifyAuthKey);

		if (checkMemberModifyAuthKeyRd.isFail()) {
			return rq.jsHistoryBack(checkMemberModifyAuthKeyRd.getMsg());
		}

		if (Ut.empty(loginPw)) {
			loginPw = null;
		}

		ResultData modifyRd = memberService.modify(rq.getLoginedMemberId(), loginPw, name, nickname, cellphoneNo, email,
				favoriteCinema);

		if (request.getParameter("deleteFileMemberExtraProfileImg") != null) {
			genFileService.deleteGenFiles("member", rq.getLoginedMemberId(), "extra", "profileImg", 1);
		}

		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, rq.getLoginedMemberId());
			}
		}

		return rq.jsReplace(modifyRd.getMsg(), "/");
	}

	@RequestMapping("/usr/member/myTicketList")
	public String showMyTicketingList(Model model, int id) {
		if (rq.getLoginedMemberId() != id) {
			return "usr/member/myPage";
		}

		List<Ticket> lists = memberService.getMyTicketingList(id);

		for (Ticket list : lists) {
			String[] split1 = list.getSeat().split(", ");
			String seatId = "";
			String seatInfo = "";
			for (int i = 0; i < split1.length; i++) {
				String[] split2 = split1[i].split("_");
				if (i == split1.length-1) {
					seatId += split2[0];
					seatInfo += split2[1];
				} else {
					seatId += split2[0] + ", ";
					seatInfo += split2[1] + ", ";
				}
			}
			list.setExtra__seatId(seatId);
			list.setExtra__seatInfo(seatInfo);
		}

		model.addAttribute("lists", lists);

		return "usr/member/myTicketList";
	}

	@RequestMapping("/usr/member/doTicketCancel")
	@ResponseBody
	public String doTicketCencle(Model model, int id, String seatIds) {

		memberService.doTicketCancel(id, seatIds);

		return rq.jsReplace("취소가 완료되었습니다.", "/usr/member/myTicketList?id=" + rq.getLoginedMemberId());
	}

}
