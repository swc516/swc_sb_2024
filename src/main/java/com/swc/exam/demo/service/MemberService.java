package com.swc.exam.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.MemberRepository;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Member;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.TheaterTime;
import com.swc.exam.demo.vo.Ticket;

@Service
public class MemberService {
	@Value("${custom.siteMainUri}")
	private String siteMainUri;
	@Value("${custom.siteName}")
	private String siteName;

	private MemberRepository memberRepository;
	private TicketService ticketService;
	private AttrService attrService;
	private MailService mailService;
	private CinemaService cinemaService;

	public MemberService(AttrService attrService, TicketService ticketService, MemberRepository memberRepository, MailService mailService, CinemaService cinemaService) {
		this.memberRepository = memberRepository;
		this.ticketService = ticketService;
		this.attrService = attrService;
		this.mailService = mailService;
		this.cinemaService = cinemaService;
	}

	public ResultData join(String loginId, String loginPw, String name, String nickname, String cellphoneNo,
			String email, int favoriteCinema) {
		// 로그인아이디 중복체크
		Member oldMember = getMemberByLoginId(loginId);

		if (oldMember != null) {
			return ResultData.from("F-7", Ut.f("해당 로그인아이디(%s)는 이미 사용중입니다.", loginId));
		}
		// 이름+이메일 중복체크
		oldMember = getMemberByNameAndEmail(name, email);

		if (oldMember != null) {
			return ResultData.from("F-8", Ut.f("해당 이름(%s)과 이메일(%s)은 이미 사용중입니다.", name, email));
		}

		memberRepository.join(loginId, loginPw, name, nickname, cellphoneNo, email, favoriteCinema);
		int id = memberRepository.getLastInsertId();

		return new ResultData("S-1", "회원가입이 완료되었습니다.", "id", id);
	}

	public Member getMemberByNameAndEmail(String name, String email) {
		return memberRepository.getMemberByNameAndEmail(name, email);

	}

	public Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);

	}

	public Member getMemberById(int id) {
		return memberRepository.getMemberById(id);
	}

	public ResultData modify(int actorId, String loginPw, String name, String nickname, String cellphoneNo, String email,
			int favoriteCinema) {

		memberRepository.modify(actorId, loginPw, name, nickname, cellphoneNo, email, favoriteCinema);

		if (loginPw != null) {
			attrService.remove("member", actorId, "extra", "useTempPassword");
		}

		return ResultData.from("S-1", "회원정보가 수정되었습니다.");
	}

	public String genMemberModifyAuthKey(int actorId) {
		String memberModifyAuthKey = Ut.getTempPassword(10);
		attrService.setValue("member", actorId, "extra", "memberModifyAuthKey", memberModifyAuthKey,
				Ut.getDateStrLater(60 * 5));
		return memberModifyAuthKey;
	}

	public ResultData checkMemberModifyAuthKey(int actorId, String memberModifyAuthKey) {
		String saved = attrService.getValue("member", actorId, "extra", "memberModifyAuthKey");
		if (!saved.equals(memberModifyAuthKey)) {
			return ResultData.from("F-1", "일치하지 않거나 만료되었습니다.");
		}

		return ResultData.from("S-1", "정상적인 코드입니다.");
	}

	public ResultData notifyTempLoginPwByEmail(Member actor) {
		String title = "[" + siteName + "] 임시 패스워드 발송";
		String tempPassword = Ut.getTempPassword(6);
		String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
		body += "<a href=\"" + siteMainUri + "/usr/member/login\" target=\"_blank\">로그인 하러가기</a>";

		ResultData sendResultData = mailService.send(actor.getEmail(), title, body);

		if (sendResultData.isFail()) {
			return sendResultData;
		}

		setTempPassword(actor, tempPassword);

		return ResultData.from("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다.");
	}

	private void setTempPassword(Member actor, String tempPassword) {
		attrService.setValue("member", actor.getId(), "extra", "useTempPassword", "1", null);
		memberRepository.modify(actor.getId(), Ut.sha256(tempPassword), null, null, null, null, 0);
	}

	public boolean isUsingTempPassword(int actorId) {
		return attrService.getValue("member", actorId, "extra", "useTempPassword").equals("1");
	}

	public int getMembersCount(int authLevel, String searchKeywordTypeCode, String searchKeyword) {
		return memberRepository.getMembersCount(authLevel, searchKeywordTypeCode, searchKeyword);

	}

	public List<Member> getForPrintMembers(int authLevel, String searchKeywordTypeCode, String searchKeyword,
			int itemsCountInAPage, int page) {

		int limitStart = (page - 1) * itemsCountInAPage;
		int limitTake = itemsCountInAPage;

		List<Member> members = memberRepository.getForPrintMembers(authLevel, searchKeywordTypeCode, searchKeyword,
				limitStart, limitTake);

		return members;
	}

	public void deleteMembers(List<Integer> memberIds) {
		for (int memberId : memberIds) {
			Member member = getMemberById(memberId);

			if (member != null) {
				deleteMember(member);
			}
		}
	}

	private void deleteMember(Member member) {
		memberRepository.deleteMember(member.getId());
	}

	public List<Ticket> getMyTicketingList(int id) {
		List<Ticket> lists = ticketService.getMyTicketingList(id);
		return lists;
	}

	public void doTicketCancel(int ticketId, String seatIds) {
		ticketService.doTicketCancel(ticketId);
		
		String[] seatIdSplit = seatIds.split(", ");

		for(String seatId : seatIdSplit) {
			cinemaService.doTicketCancel(Integer.parseInt(seatId));
		}
		
		
	}

	public List<Cinema> getCinemaList() {
		List<Cinema> cinemas = cinemaService.getCinemaList();
		return cinemas;
	}

	public int getMemberFavoriteCinema(int memberId) {
		int loginedMemberFavoriteCiname = memberRepository.getMemberFavoriteCinema(memberId);
		return loginedMemberFavoriteCiname;
	}
	
}
