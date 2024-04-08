package com.swc.exam.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.swc.exam.demo.service.CinemaService;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Cinema;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;
import com.swc.exam.demo.vo.TheaterInfo;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdmCinemaController {

	private CinemaService cinemaService;
	private Rq rq;

	public AdmCinemaController(CinemaService cinemaService, Rq rq) {
		this.cinemaService = cinemaService;
		this.rq = rq;
	}

	@RequestMapping("/adm/cinema/list")
	public String showList(Model model, @RequestParam(defaultValue = "") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "1") int page) {

		int cinemasCount = cinemaService.getCinemasCount(searchKeywordTypeCode, searchKeyword);

		int itemsCountInAPage = 10;
		int pagesCount = (int) Math.ceil((double) cinemasCount / itemsCountInAPage);
		List<Cinema> cinemas = cinemaService.getForPrintCinemas(searchKeywordTypeCode, searchKeyword, itemsCountInAPage,
				page);

		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);

		model.addAttribute("cinemas", cinemas);
		model.addAttribute("cinemasCount", cinemasCount);

		return "adm/cinema/list";
	}

	@RequestMapping("/adm/cinema/add")
	public String showAdd() {
		return "adm/cinema/add";
	}

	@RequestMapping("/adm/cinema/doAdd")
	@ResponseBody
	public String doAdd(String region, String branch) {
		ResultData addRd = cinemaService.add(region, branch);

		if (addRd.isFail()) {
			return rq.jsHistoryBack(addRd.getMsg());
		}

		return rq.jsReplace(addRd.getMsg(), "/adm/cinema/list");
	}

	@RequestMapping("/adm/cinema/doDelete")
	@ResponseBody
	public String doDelete(@RequestParam(defaultValue = "") String ids,
			@RequestParam(defaultValue = "/adm/cinema/list") String replaceUri) {

		List<Integer> cinemaIds = new ArrayList<>();

		for (String idStr : ids.split(",")) {
			cinemaIds.add(Integer.parseInt(idStr));
		}

		cinemaService.deleteCinemas(cinemaIds);

		return rq.jsReplace("해당 영화관이 삭제되었습니다.", replaceUri);
	}

	@RequestMapping("/adm/cinema/detail")
	public String showDetail(Model model, int id) {
		Cinema cinema = cinemaService.getForPrintCinema(id);
		model.addAttribute("cinema", cinema);

		List<TheaterInfo> theaterInfos = cinemaService.getTheaterInfoByCinemaId(id);
		model.addAttribute("theaterInfos", theaterInfos);

		return "/adm/cinema/detail";
	}

	@RequestMapping("/adm/cinema/modify")
	public String showModify(Model model, int id) {
		Cinema cinema = cinemaService.getForPrintCinema(id);

		if (cinema == null) {
			return rq.historyBackJsOnview(Ut.f("%d번 영화관이 존재하지 않습니다.", id));
		}

		model.addAttribute("cinema", cinema);

		return "/adm/cinema/modify";
	}

	@RequestMapping("/adm/cinema/doModify")
	@ResponseBody
	public String doModify(int id, String region, String branch) {

		ResultData modifyRd = cinemaService.modify(id, region, branch);

		if (modifyRd.isFail()) {
			return rq.jsHistoryBack(modifyRd.getMsg());
		}

		return rq.jsReplace(modifyRd.getMsg(), "/adm/cinema/list");
	}

	///////////////////////////////////////////////////////////////////////

	@RequestMapping("/adm/cinema/theater/addInfo")
	public String showAdd(Model model, int id) {
		Cinema cinema = cinemaService.getCinemaById(id);
		model.addAttribute("cinema", cinema);

		return "adm/cinema/theater/addInfo";
	}

	@RequestMapping("/adm/cinema/theater/doAddInfo")
	@ResponseBody
	public String doAdd(int cinemaId, String theater, char seatRow, int seatCol) {
		String seatStatus = "일반";
		ResultData addRd = cinemaService.addTheaterInfo(cinemaId, theater, seatRow, seatCol, seatStatus);

		if (addRd.isFail()) {
			return rq.jsHistoryBack(addRd.getResultCode(), addRd.getMsg());
		}

		return rq.jsReplace(addRd.getMsg(), "/adm/cinema/detail?id=" + cinemaId);
	}

	@RequestMapping("/adm/cinema/theater/detail")
	public String showTheaterInfoDetail(Model model, int theaterInfoId) {

		List<TheaterInfo> theaterInfos = cinemaService.getTheaterInfos(theaterInfoId);
		model.addAttribute("theaterInfos", theaterInfos);

		if (theaterInfos.isEmpty()) {
			return "";
		}

		model.addAttribute("theater", theaterInfos.get(0).getTheater());
		model.addAttribute("region", theaterInfos.get(0).getExtra__region());
		model.addAttribute("branch", theaterInfos.get(0).getExtra__branch());

		String seatRows = "";
		int lastSeatRow = theaterInfos.get(theaterInfos.size() - 1).getSeatRow();
		for (int i = 'A'; i <= lastSeatRow; i++) {
			char seatRow = (char) i;
			seatRows += seatRow;
		}

		char[] seatRowArr = seatRows.toCharArray();
		model.addAttribute("seatRowArr", seatRowArr);

		int lastSeatCol = theaterInfos.get(theaterInfos.size() - 1).getSeatCol();
		int[] seatColArr = new int[lastSeatCol];
		for (int i = 0; i < lastSeatCol; i++) {
			seatColArr[i] = i + 1;
		}

		model.addAttribute("seatColArr", seatColArr);

		return "/adm/cinema/theater/detail";
	}

	@RequestMapping("/adm/cinema/theater/doModify")
	@ResponseBody
	public String doTheaterInfoModify(int theaterInfoId, String[] seats, String seatStatus) {
		cinemaService.modifySeat(theaterInfoId, seats, seatStatus);

		return rq.jsReplace("좌석정보가 수정되었습니다", "/adm/cinema/theater/detail?theaterInfoId=" + theaterInfoId);
	}

	@RequestMapping("/adm/cinema/theater/doTheaterInfoDelete")
	@ResponseBody
	public String doTheaterInfoDelete(int theaterInfoId) {
		int cinemaId = cinemaService.getCinemaIdByTheaterInfoId(theaterInfoId);
		cinemaService.deleteTheaterInfo(theaterInfoId);

		return rq.jsReplace("해당 상영관이 삭제되었습니다.", "/adm/cinema/detail?id=" + cinemaId);
	}

	@RequestMapping("/adm/cinema/theater/addTime")
	public String showAddTime(Model model, @RequestParam(defaultValue = "0")int cinemaId) {
		List<Cinema> cinemas = cinemaService.getCinemaList();
		
		List<TheaterInfo> theaterInfos = cinemaService.getCinemaTheaterList(cinemaId);
		model.addAttribute("theaterInfos", theaterInfos);

		List<Movie> movies = cinemaService.getPlayingMovies();

		model.addAttribute("cinemas", cinemas);
		model.addAttribute("movies", movies);

		return "adm/cinema/theater/addTime";
	}

	@RequestMapping("/adm/cinema/theater/doAddTime")
	@ResponseBody
	public String doAddTime(int cinemaId, int theaterInfoId, int movieId, String date, int theaterTime, String startTime, String endTime) {
		ResultData addRd = cinemaService.addTime(cinemaId, theaterInfoId, movieId, date, theaterTime, startTime, endTime);

		if (addRd.isFail()) {
			return rq.jsHistoryBack(addRd.getResultCode(), addRd.getMsg());
		}

		return rq.jsReplace(addRd.getMsg(),
				"/adm/cinema/theater/addTime?cinemaId=" + cinemaId);

	}
	
	
	@RequestMapping("/adm/cinema/doDeleteAfterTheaterTime")
	@ResponseBody
	public String doDeleteAfterTheaterTime() {
		
		cinemaService.doDeleteAfterTheaterTime();
		

		return rq.jsHistoryBack("완료되었습니다.");
	}
	
	
	
	@RequestMapping("/adm/cinema/theater/insertTestData")
	@ResponseBody
	public String insertTestData() {
		// c id / t id/ m id/ date / theaterTime / sT / eT
		
		//서울 도봉/ 1관 / 댓글부대 / 오늘~ / n회차 / 시작시간 / 종료시간
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(0) , 1, Ut.getAddDay(0) + " 10:00:00" , Ut.getAddDay(0) + " 12:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(0) , 2, Ut.getAddDay(0) + " 13:00:00" , Ut.getAddDay(0) + " 15:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(0) , 3, Ut.getAddDay(0) + " 16:00:00" , Ut.getAddDay(0) + " 18:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(0) , 4, Ut.getAddDay(0) + " 19:00:00" , Ut.getAddDay(0) + " 21:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(0) , 5, Ut.getAddDay(0) + " 22:00:00" , Ut.getAddDay(1) + " 00:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(0) , 6, Ut.getAddDay(1) + " 01:00:00" , Ut.getAddDay(1) + " 03:00:00");
		
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(1) , 1, Ut.getAddDay(1) + " 10:00:00" , Ut.getAddDay(1) + " 12:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(1) , 2, Ut.getAddDay(1) + " 13:00:00" , Ut.getAddDay(1) + " 15:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(1) , 3, Ut.getAddDay(1) + " 16:00:00" , Ut.getAddDay(1) + " 18:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(1) , 4, Ut.getAddDay(1) + " 19:00:00" , Ut.getAddDay(1) + " 21:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(1) , 5, Ut.getAddDay(1) + " 22:00:00" , Ut.getAddDay(2) + " 00:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(1) , 6, Ut.getAddDay(2) + " 01:00:00" , Ut.getAddDay(2) + " 03:00:00");
		
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(2) , 1, Ut.getAddDay(2) + " 10:00:00" , Ut.getAddDay(2) + " 12:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(2) , 2, Ut.getAddDay(2) + " 13:00:00" , Ut.getAddDay(2) + " 15:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(2) , 3, Ut.getAddDay(2) + " 16:00:00" , Ut.getAddDay(2) + " 18:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(2) , 4, Ut.getAddDay(2) + " 19:00:00" , Ut.getAddDay(2) + " 21:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(2) , 5, Ut.getAddDay(2) + " 22:00:00" , Ut.getAddDay(3) + " 00:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(2) , 6, Ut.getAddDay(3) + " 01:00:00" , Ut.getAddDay(3) + " 03:00:00");
		
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(3) , 1, Ut.getAddDay(3) + " 10:00:00" , Ut.getAddDay(3) + " 12:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(3) , 2, Ut.getAddDay(3) + " 13:00:00" , Ut.getAddDay(3) + " 15:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(3) , 3, Ut.getAddDay(3) + " 16:00:00" , Ut.getAddDay(3) + " 18:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(3) , 4, Ut.getAddDay(3) + " 19:00:00" , Ut.getAddDay(3) + " 21:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(3) , 5, Ut.getAddDay(3) + " 22:00:00" , Ut.getAddDay(4) + " 00:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(3) , 6, Ut.getAddDay(4) + " 01:00:00" , Ut.getAddDay(4) + " 03:00:00");
	
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(4) , 1, Ut.getAddDay(4) + " 10:00:00" , Ut.getAddDay(4) + " 12:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(4) , 2, Ut.getAddDay(4) + " 13:00:00" , Ut.getAddDay(4) + " 15:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(4) , 3, Ut.getAddDay(4) + " 16:00:00" , Ut.getAddDay(4) + " 18:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(4) , 4, Ut.getAddDay(4) + " 19:00:00" , Ut.getAddDay(4) + " 21:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(4) , 5, Ut.getAddDay(4) + " 22:00:00" , Ut.getAddDay(5) + " 00:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(4) , 6, Ut.getAddDay(5) + " 01:00:00" , Ut.getAddDay(5) + " 03:00:00");
		
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(5) , 1, Ut.getAddDay(5) + " 10:00:00" , Ut.getAddDay(5) + " 12:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(5) , 2, Ut.getAddDay(5) + " 13:00:00" , Ut.getAddDay(5) + " 15:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(5) , 3, Ut.getAddDay(5) + " 16:00:00" , Ut.getAddDay(5) + " 18:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(5) , 4, Ut.getAddDay(5) + " 19:00:00" , Ut.getAddDay(5) + " 21:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(5) , 5, Ut.getAddDay(5) + " 22:00:00" , Ut.getAddDay(6) + " 00:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(5) , 6, Ut.getAddDay(6) + " 01:00:00" , Ut.getAddDay(6) + " 03:00:00");
		
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(6) , 1, Ut.getAddDay(6) + " 10:00:00" , Ut.getAddDay(6) + " 12:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(6) , 2, Ut.getAddDay(6) + " 13:00:00" , Ut.getAddDay(6) + " 15:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(6) , 3, Ut.getAddDay(6) + " 16:00:00" , Ut.getAddDay(6) + " 18:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(6) , 4, Ut.getAddDay(6) + " 19:00:00" , Ut.getAddDay(6) + " 21:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(6) , 5, Ut.getAddDay(6) + " 22:00:00" , Ut.getAddDay(7) + " 00:00:00");
		cinemaService.addTime(1, 1, 1, Ut.getAddDay(6) , 6, Ut.getAddDay(7) + " 01:00:00" , Ut.getAddDay(7) + " 03:00:00");
			
		
		
		
		
		
		//서울 도봉/ 2관 / 고질라 / 오늘~ / n회차 / 시작시간 / 종료시간	
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(0) , 1, Ut.getAddDay(0) + " 10:00:00" , Ut.getAddDay(0) + " 12:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(0) , 2, Ut.getAddDay(0) + " 13:00:00" , Ut.getAddDay(0) + " 15:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(0) , 3, Ut.getAddDay(0) + " 16:00:00" , Ut.getAddDay(0) + " 18:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(0) , 4, Ut.getAddDay(0) + " 19:00:00" , Ut.getAddDay(0) + " 21:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(0) , 5, Ut.getAddDay(0) + " 22:00:00" , Ut.getAddDay(1) + " 00:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(0) , 6, Ut.getAddDay(1) + " 01:00:00" , Ut.getAddDay(1) + " 03:00:00");
		
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(1) , 1, Ut.getAddDay(1) + " 10:00:00" , Ut.getAddDay(1) + " 12:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(1) , 2, Ut.getAddDay(1) + " 13:00:00" , Ut.getAddDay(1) + " 15:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(1) , 3, Ut.getAddDay(1) + " 16:00:00" , Ut.getAddDay(1) + " 18:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(1) , 4, Ut.getAddDay(1) + " 19:00:00" , Ut.getAddDay(1) + " 21:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(1) , 5, Ut.getAddDay(1) + " 22:00:00" , Ut.getAddDay(2) + " 00:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(1) , 6, Ut.getAddDay(2) + " 01:00:00" , Ut.getAddDay(2) + " 03:00:00");
		
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(2) , 1, Ut.getAddDay(2) + " 10:00:00" , Ut.getAddDay(2) + " 12:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(2) , 2, Ut.getAddDay(2) + " 13:00:00" , Ut.getAddDay(2) + " 15:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(2) , 3, Ut.getAddDay(2) + " 16:00:00" , Ut.getAddDay(2) + " 18:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(2) , 4, Ut.getAddDay(2) + " 19:00:00" , Ut.getAddDay(2) + " 21:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(2) , 5, Ut.getAddDay(2) + " 22:00:00" , Ut.getAddDay(3) + " 00:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(2) , 6, Ut.getAddDay(3) + " 01:00:00" , Ut.getAddDay(3) + " 03:00:00");
		
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(3) , 1, Ut.getAddDay(3) + " 10:00:00" , Ut.getAddDay(3) + " 12:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(3) , 2, Ut.getAddDay(3) + " 13:00:00" , Ut.getAddDay(3) + " 15:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(3) , 3, Ut.getAddDay(3) + " 16:00:00" , Ut.getAddDay(3) + " 18:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(3) , 4, Ut.getAddDay(3) + " 19:00:00" , Ut.getAddDay(3) + " 21:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(3) , 5, Ut.getAddDay(3) + " 22:00:00" , Ut.getAddDay(4) + " 00:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(3) , 6, Ut.getAddDay(4) + " 01:00:00" , Ut.getAddDay(4) + " 03:00:00");
	
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(4) , 1, Ut.getAddDay(4) + " 10:00:00" , Ut.getAddDay(4) + " 12:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(4) , 2, Ut.getAddDay(4) + " 13:00:00" , Ut.getAddDay(4) + " 15:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(4) , 3, Ut.getAddDay(4) + " 16:00:00" , Ut.getAddDay(4) + " 18:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(4) , 4, Ut.getAddDay(4) + " 19:00:00" , Ut.getAddDay(4) + " 21:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(4) , 5, Ut.getAddDay(4) + " 22:00:00" , Ut.getAddDay(5) + " 00:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(4) , 6, Ut.getAddDay(5) + " 01:00:00" , Ut.getAddDay(5) + " 03:00:00");
		
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(5) , 1, Ut.getAddDay(5) + " 10:00:00" , Ut.getAddDay(5) + " 12:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(5) , 2, Ut.getAddDay(5) + " 13:00:00" , Ut.getAddDay(5) + " 15:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(5) , 3, Ut.getAddDay(5) + " 16:00:00" , Ut.getAddDay(5) + " 18:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(5) , 4, Ut.getAddDay(5) + " 19:00:00" , Ut.getAddDay(5) + " 21:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(5) , 5, Ut.getAddDay(5) + " 22:00:00" , Ut.getAddDay(6) + " 00:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(5) , 6, Ut.getAddDay(6) + " 01:00:00" , Ut.getAddDay(6) + " 03:00:00");
		
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(6) , 1, Ut.getAddDay(6) + " 10:00:00" , Ut.getAddDay(6) + " 12:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(6) , 2, Ut.getAddDay(6) + " 13:00:00" , Ut.getAddDay(6) + " 15:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(6) , 3, Ut.getAddDay(6) + " 16:00:00" , Ut.getAddDay(6) + " 18:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(6) , 4, Ut.getAddDay(6) + " 19:00:00" , Ut.getAddDay(6) + " 21:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(6) , 5, Ut.getAddDay(6) + " 22:00:00" , Ut.getAddDay(7) + " 00:00:00");
		cinemaService.addTime(1, 2, 2, Ut.getAddDay(6) , 6, Ut.getAddDay(7) + " 01:00:00" , Ut.getAddDay(7) + " 03:00:00");
		
		
		
		//서울 용산/ 1관 / 댓글부대 / 오늘~ / n회차 / 시작시간 / 종료시간	
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(0) , 1, Ut.getAddDay(0) + " 10:00:00" , Ut.getAddDay(0) + " 12:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(0) , 2, Ut.getAddDay(0) + " 13:00:00" , Ut.getAddDay(0) + " 15:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(0) , 3, Ut.getAddDay(0) + " 16:00:00" , Ut.getAddDay(0) + " 18:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(0) , 4, Ut.getAddDay(0) + " 19:00:00" , Ut.getAddDay(0) + " 21:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(0) , 5, Ut.getAddDay(0) + " 22:00:00" , Ut.getAddDay(1) + " 00:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(0) , 6, Ut.getAddDay(1) + " 01:00:00" , Ut.getAddDay(1) + " 03:00:00");
		
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(1) , 1, Ut.getAddDay(1) + " 10:00:00" , Ut.getAddDay(1) + " 12:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(1) , 2, Ut.getAddDay(1) + " 13:00:00" , Ut.getAddDay(1) + " 15:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(1) , 3, Ut.getAddDay(1) + " 16:00:00" , Ut.getAddDay(1) + " 18:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(1) , 4, Ut.getAddDay(1) + " 19:00:00" , Ut.getAddDay(1) + " 21:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(1) , 5, Ut.getAddDay(1) + " 22:00:00" , Ut.getAddDay(2) + " 00:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(1) , 6, Ut.getAddDay(2) + " 01:00:00" , Ut.getAddDay(2) + " 03:00:00");
		
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(2) , 1, Ut.getAddDay(2) + " 10:00:00" , Ut.getAddDay(2) + " 12:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(2) , 2, Ut.getAddDay(2) + " 13:00:00" , Ut.getAddDay(2) + " 15:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(2) , 3, Ut.getAddDay(2) + " 16:00:00" , Ut.getAddDay(2) + " 18:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(2) , 4, Ut.getAddDay(2) + " 19:00:00" , Ut.getAddDay(2) + " 21:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(2) , 5, Ut.getAddDay(2) + " 22:00:00" , Ut.getAddDay(3) + " 00:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(2) , 6, Ut.getAddDay(3) + " 01:00:00" , Ut.getAddDay(3) + " 03:00:00");
		
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(3) , 1, Ut.getAddDay(3) + " 10:00:00" , Ut.getAddDay(3) + " 12:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(3) , 2, Ut.getAddDay(3) + " 13:00:00" , Ut.getAddDay(3) + " 15:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(3) , 3, Ut.getAddDay(3) + " 16:00:00" , Ut.getAddDay(3) + " 18:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(3) , 4, Ut.getAddDay(3) + " 19:00:00" , Ut.getAddDay(3) + " 21:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(3) , 5, Ut.getAddDay(3) + " 22:00:00" , Ut.getAddDay(4) + " 00:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(3) , 6, Ut.getAddDay(4) + " 01:00:00" , Ut.getAddDay(4) + " 03:00:00");
		
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(4) , 1, Ut.getAddDay(4) + " 10:00:00" , Ut.getAddDay(4) + " 12:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(4) , 2, Ut.getAddDay(4) + " 13:00:00" , Ut.getAddDay(4) + " 15:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(4) , 3, Ut.getAddDay(4) + " 16:00:00" , Ut.getAddDay(4) + " 18:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(4) , 4, Ut.getAddDay(4) + " 19:00:00" , Ut.getAddDay(4) + " 21:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(4) , 5, Ut.getAddDay(4) + " 22:00:00" , Ut.getAddDay(5) + " 00:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(4) , 6, Ut.getAddDay(5) + " 01:00:00" , Ut.getAddDay(5) + " 03:00:00");
		
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(5) , 1, Ut.getAddDay(5) + " 10:00:00" , Ut.getAddDay(5) + " 12:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(5) , 2, Ut.getAddDay(5) + " 13:00:00" , Ut.getAddDay(5) + " 15:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(5) , 3, Ut.getAddDay(5) + " 16:00:00" , Ut.getAddDay(5) + " 18:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(5) , 4, Ut.getAddDay(5) + " 19:00:00" , Ut.getAddDay(5) + " 21:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(5) , 5, Ut.getAddDay(5) + " 22:00:00" , Ut.getAddDay(6) + " 00:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(5) , 6, Ut.getAddDay(6) + " 01:00:00" , Ut.getAddDay(6) + " 03:00:00");
		
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(6) , 1, Ut.getAddDay(6) + " 10:00:00" , Ut.getAddDay(6) + " 12:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(6) , 2, Ut.getAddDay(6) + " 13:00:00" , Ut.getAddDay(6) + " 15:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(6) , 3, Ut.getAddDay(6) + " 16:00:00" , Ut.getAddDay(6) + " 18:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(6) , 4, Ut.getAddDay(6) + " 19:00:00" , Ut.getAddDay(6) + " 21:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(6) , 5, Ut.getAddDay(6) + " 22:00:00" , Ut.getAddDay(7) + " 00:00:00");
		cinemaService.addTime(2, 3, 1, Ut.getAddDay(6) , 6, Ut.getAddDay(7) + " 01:00:00" , Ut.getAddDay(7) + " 03:00:00");
		
		
		
		//서울 용산/ 2관 / 파묘 / 오늘~ / n회차 / 시작시간 / 종료시간	
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(0) , 1, Ut.getAddDay(0) + " 10:00:00" , Ut.getAddDay(0) + " 12:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(0) , 2, Ut.getAddDay(0) + " 13:00:00" , Ut.getAddDay(0) + " 15:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(0) , 3, Ut.getAddDay(0) + " 16:00:00" , Ut.getAddDay(0) + " 18:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(0) , 4, Ut.getAddDay(0) + " 19:00:00" , Ut.getAddDay(0) + " 21:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(0) , 5, Ut.getAddDay(0) + " 22:00:00" , Ut.getAddDay(1) + " 00:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(0) , 6, Ut.getAddDay(1) + " 01:00:00" , Ut.getAddDay(1) + " 03:00:00");
		
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(1) , 1, Ut.getAddDay(1) + " 10:00:00" , Ut.getAddDay(1) + " 12:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(1) , 2, Ut.getAddDay(1) + " 13:00:00" , Ut.getAddDay(1) + " 15:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(1) , 3, Ut.getAddDay(1) + " 16:00:00" , Ut.getAddDay(1) + " 18:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(1) , 4, Ut.getAddDay(1) + " 19:00:00" , Ut.getAddDay(1) + " 21:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(1) , 5, Ut.getAddDay(1) + " 22:00:00" , Ut.getAddDay(2) + " 00:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(1) , 6, Ut.getAddDay(2) + " 01:00:00" , Ut.getAddDay(2) + " 03:00:00");
		
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(2) , 1, Ut.getAddDay(2) + " 10:00:00" , Ut.getAddDay(2) + " 12:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(2) , 2, Ut.getAddDay(2) + " 13:00:00" , Ut.getAddDay(2) + " 15:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(2) , 3, Ut.getAddDay(2) + " 16:00:00" , Ut.getAddDay(2) + " 18:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(2) , 4, Ut.getAddDay(2) + " 19:00:00" , Ut.getAddDay(2) + " 21:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(2) , 5, Ut.getAddDay(2) + " 22:00:00" , Ut.getAddDay(3) + " 00:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(2) , 6, Ut.getAddDay(3) + " 01:00:00" , Ut.getAddDay(3) + " 03:00:00");
		
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(3) , 1, Ut.getAddDay(3) + " 10:00:00" , Ut.getAddDay(3) + " 12:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(3) , 2, Ut.getAddDay(3) + " 13:00:00" , Ut.getAddDay(3) + " 15:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(3) , 3, Ut.getAddDay(3) + " 16:00:00" , Ut.getAddDay(3) + " 18:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(3) , 4, Ut.getAddDay(3) + " 19:00:00" , Ut.getAddDay(3) + " 21:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(3) , 5, Ut.getAddDay(3) + " 22:00:00" , Ut.getAddDay(4) + " 00:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(3) , 6, Ut.getAddDay(4) + " 01:00:00" , Ut.getAddDay(4) + " 03:00:00");
		
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(4) , 1, Ut.getAddDay(4) + " 10:00:00" , Ut.getAddDay(4) + " 12:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(4) , 2, Ut.getAddDay(4) + " 13:00:00" , Ut.getAddDay(4) + " 15:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(4) , 3, Ut.getAddDay(4) + " 16:00:00" , Ut.getAddDay(4) + " 18:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(4) , 4, Ut.getAddDay(4) + " 19:00:00" , Ut.getAddDay(4) + " 21:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(4) , 5, Ut.getAddDay(4) + " 22:00:00" , Ut.getAddDay(5) + " 00:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(4) , 6, Ut.getAddDay(5) + " 01:00:00" , Ut.getAddDay(5) + " 03:00:00");
		
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(5) , 1, Ut.getAddDay(5) + " 10:00:00" , Ut.getAddDay(5) + " 12:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(5) , 2, Ut.getAddDay(5) + " 13:00:00" , Ut.getAddDay(5) + " 15:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(5) , 3, Ut.getAddDay(5) + " 16:00:00" , Ut.getAddDay(5) + " 18:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(5) , 4, Ut.getAddDay(5) + " 19:00:00" , Ut.getAddDay(5) + " 21:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(5) , 5, Ut.getAddDay(5) + " 22:00:00" , Ut.getAddDay(6) + " 00:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(5) , 6, Ut.getAddDay(6) + " 01:00:00" , Ut.getAddDay(6) + " 03:00:00");
		
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(6) , 1, Ut.getAddDay(6) + " 10:00:00" , Ut.getAddDay(6) + " 12:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(6) , 2, Ut.getAddDay(6) + " 13:00:00" , Ut.getAddDay(6) + " 15:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(6) , 3, Ut.getAddDay(6) + " 16:00:00" , Ut.getAddDay(6) + " 18:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(6) , 4, Ut.getAddDay(6) + " 19:00:00" , Ut.getAddDay(6) + " 21:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(6) , 5, Ut.getAddDay(6) + " 22:00:00" , Ut.getAddDay(7) + " 00:00:00");
		cinemaService.addTime(2, 4, 3, Ut.getAddDay(6) , 6, Ut.getAddDay(7) + " 01:00:00" , Ut.getAddDay(7) + " 03:00:00");
		
		
		
		//경기 의정부/ 1관 / 댓글부대 / 오늘~ / n회차 / 시작시간 / 종료시간	
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(0) , 1, Ut.getAddDay(0) + " 10:00:00" , Ut.getAddDay(0) + " 12:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(0) , 2, Ut.getAddDay(0) + " 13:00:00" , Ut.getAddDay(0) + " 15:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(0) , 3, Ut.getAddDay(0) + " 16:00:00" , Ut.getAddDay(0) + " 18:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(0) , 4, Ut.getAddDay(0) + " 19:00:00" , Ut.getAddDay(0) + " 21:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(0) , 5, Ut.getAddDay(0) + " 22:00:00" , Ut.getAddDay(1) + " 00:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(0) , 6, Ut.getAddDay(1) + " 01:00:00" , Ut.getAddDay(1) + " 03:00:00");
		
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(1) , 1, Ut.getAddDay(1) + " 10:00:00" , Ut.getAddDay(1) + " 12:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(1) , 2, Ut.getAddDay(1) + " 13:00:00" , Ut.getAddDay(1) + " 15:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(1) , 3, Ut.getAddDay(1) + " 16:00:00" , Ut.getAddDay(1) + " 18:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(1) , 4, Ut.getAddDay(1) + " 19:00:00" , Ut.getAddDay(1) + " 21:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(1) , 5, Ut.getAddDay(1) + " 22:00:00" , Ut.getAddDay(2) + " 00:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(1) , 6, Ut.getAddDay(2) + " 01:00:00" , Ut.getAddDay(2) + " 03:00:00");
		
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(2) , 1, Ut.getAddDay(2) + " 10:00:00" , Ut.getAddDay(2) + " 12:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(2) , 2, Ut.getAddDay(2) + " 13:00:00" , Ut.getAddDay(2) + " 15:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(2) , 3, Ut.getAddDay(2) + " 16:00:00" , Ut.getAddDay(2) + " 18:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(2) , 4, Ut.getAddDay(2) + " 19:00:00" , Ut.getAddDay(2) + " 21:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(2) , 5, Ut.getAddDay(2) + " 22:00:00" , Ut.getAddDay(3) + " 00:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(2) , 6, Ut.getAddDay(3) + " 01:00:00" , Ut.getAddDay(3) + " 03:00:00");
		
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(3) , 1, Ut.getAddDay(3) + " 10:00:00" , Ut.getAddDay(3) + " 12:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(3) , 2, Ut.getAddDay(3) + " 13:00:00" , Ut.getAddDay(3) + " 15:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(3) , 3, Ut.getAddDay(3) + " 16:00:00" , Ut.getAddDay(3) + " 18:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(3) , 4, Ut.getAddDay(3) + " 19:00:00" , Ut.getAddDay(3) + " 21:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(3) , 5, Ut.getAddDay(3) + " 22:00:00" , Ut.getAddDay(4) + " 00:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(3) , 6, Ut.getAddDay(4) + " 01:00:00" , Ut.getAddDay(4) + " 03:00:00");
		
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(4) , 1, Ut.getAddDay(4) + " 10:00:00" , Ut.getAddDay(4) + " 12:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(4) , 2, Ut.getAddDay(4) + " 13:00:00" , Ut.getAddDay(4) + " 15:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(4) , 3, Ut.getAddDay(4) + " 16:00:00" , Ut.getAddDay(4) + " 18:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(4) , 4, Ut.getAddDay(4) + " 19:00:00" , Ut.getAddDay(4) + " 21:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(4) , 5, Ut.getAddDay(4) + " 22:00:00" , Ut.getAddDay(5) + " 00:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(4) , 6, Ut.getAddDay(5) + " 01:00:00" , Ut.getAddDay(5) + " 03:00:00");
		
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(5) , 1, Ut.getAddDay(5) + " 10:00:00" , Ut.getAddDay(5) + " 12:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(5) , 2, Ut.getAddDay(5) + " 13:00:00" , Ut.getAddDay(5) + " 15:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(5) , 3, Ut.getAddDay(5) + " 16:00:00" , Ut.getAddDay(5) + " 18:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(5) , 4, Ut.getAddDay(5) + " 19:00:00" , Ut.getAddDay(5) + " 21:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(5) , 5, Ut.getAddDay(5) + " 22:00:00" , Ut.getAddDay(6) + " 00:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(5) , 6, Ut.getAddDay(6) + " 01:00:00" , Ut.getAddDay(6) + " 03:00:00");
		
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(6) , 1, Ut.getAddDay(6) + " 10:00:00" , Ut.getAddDay(6) + " 12:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(6) , 2, Ut.getAddDay(6) + " 13:00:00" , Ut.getAddDay(6) + " 15:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(6) , 3, Ut.getAddDay(6) + " 16:00:00" , Ut.getAddDay(6) + " 18:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(6) , 4, Ut.getAddDay(6) + " 19:00:00" , Ut.getAddDay(6) + " 21:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(6) , 5, Ut.getAddDay(6) + " 22:00:00" , Ut.getAddDay(7) + " 00:00:00");
		cinemaService.addTime(3, 5, 1, Ut.getAddDay(6) , 6, Ut.getAddDay(7) + " 01:00:00" , Ut.getAddDay(7) + " 03:00:00");
		
		
		//경기 의정부/ 2관 / 듄2 / 오늘~ / n회차 / 시작시간 / 종료시간	
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(0) , 1, Ut.getAddDay(0) + " 10:00:00" , Ut.getAddDay(0) + " 12:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(0) , 2, Ut.getAddDay(0) + " 13:00:00" , Ut.getAddDay(0) + " 15:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(0) , 3, Ut.getAddDay(0) + " 16:00:00" , Ut.getAddDay(0) + " 18:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(0) , 4, Ut.getAddDay(0) + " 19:00:00" , Ut.getAddDay(0) + " 21:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(0) , 5, Ut.getAddDay(0) + " 22:00:00" , Ut.getAddDay(1) + " 00:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(0) , 6, Ut.getAddDay(1) + " 01:00:00" , Ut.getAddDay(1) + " 03:00:00");
		
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(1) , 1, Ut.getAddDay(1) + " 10:00:00" , Ut.getAddDay(1) + " 12:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(1) , 2, Ut.getAddDay(1) + " 13:00:00" , Ut.getAddDay(1) + " 15:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(1) , 3, Ut.getAddDay(1) + " 16:00:00" , Ut.getAddDay(1) + " 18:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(1) , 4, Ut.getAddDay(1) + " 19:00:00" , Ut.getAddDay(1) + " 21:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(1) , 5, Ut.getAddDay(1) + " 22:00:00" , Ut.getAddDay(2) + " 00:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(1) , 6, Ut.getAddDay(2) + " 01:00:00" , Ut.getAddDay(2) + " 03:00:00");
		
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(2) , 1, Ut.getAddDay(2) + " 10:00:00" , Ut.getAddDay(2) + " 12:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(2) , 2, Ut.getAddDay(2) + " 13:00:00" , Ut.getAddDay(2) + " 15:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(2) , 3, Ut.getAddDay(2) + " 16:00:00" , Ut.getAddDay(2) + " 18:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(2) , 4, Ut.getAddDay(2) + " 19:00:00" , Ut.getAddDay(2) + " 21:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(2) , 5, Ut.getAddDay(2) + " 22:00:00" , Ut.getAddDay(3) + " 00:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(2) , 6, Ut.getAddDay(3) + " 01:00:00" , Ut.getAddDay(3) + " 03:00:00");
		
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(3) , 1, Ut.getAddDay(3) + " 10:00:00" , Ut.getAddDay(3) + " 12:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(3) , 2, Ut.getAddDay(3) + " 13:00:00" , Ut.getAddDay(3) + " 15:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(3) , 3, Ut.getAddDay(3) + " 16:00:00" , Ut.getAddDay(3) + " 18:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(3) , 4, Ut.getAddDay(3) + " 19:00:00" , Ut.getAddDay(3) + " 21:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(3) , 5, Ut.getAddDay(3) + " 22:00:00" , Ut.getAddDay(4) + " 00:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(3) , 6, Ut.getAddDay(4) + " 01:00:00" , Ut.getAddDay(4) + " 03:00:00");
		
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(4) , 1, Ut.getAddDay(4) + " 10:00:00" , Ut.getAddDay(4) + " 12:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(4) , 2, Ut.getAddDay(4) + " 13:00:00" , Ut.getAddDay(4) + " 15:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(4) , 3, Ut.getAddDay(4) + " 16:00:00" , Ut.getAddDay(4) + " 18:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(4) , 4, Ut.getAddDay(4) + " 19:00:00" , Ut.getAddDay(4) + " 21:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(4) , 5, Ut.getAddDay(4) + " 22:00:00" , Ut.getAddDay(5) + " 00:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(4) , 6, Ut.getAddDay(5) + " 01:00:00" , Ut.getAddDay(5) + " 03:00:00");
		
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(5) , 1, Ut.getAddDay(5) + " 10:00:00" , Ut.getAddDay(5) + " 12:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(5) , 2, Ut.getAddDay(5) + " 13:00:00" , Ut.getAddDay(5) + " 15:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(5) , 3, Ut.getAddDay(5) + " 16:00:00" , Ut.getAddDay(5) + " 18:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(5) , 4, Ut.getAddDay(5) + " 19:00:00" , Ut.getAddDay(5) + " 21:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(5) , 5, Ut.getAddDay(5) + " 22:00:00" , Ut.getAddDay(6) + " 00:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(5) , 6, Ut.getAddDay(6) + " 01:00:00" , Ut.getAddDay(6) + " 03:00:00");
		
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(6) , 1, Ut.getAddDay(6) + " 10:00:00" , Ut.getAddDay(6) + " 12:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(6) , 2, Ut.getAddDay(6) + " 13:00:00" , Ut.getAddDay(6) + " 15:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(6) , 3, Ut.getAddDay(6) + " 16:00:00" , Ut.getAddDay(6) + " 18:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(6) , 4, Ut.getAddDay(6) + " 19:00:00" , Ut.getAddDay(6) + " 21:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(6) , 5, Ut.getAddDay(6) + " 22:00:00" , Ut.getAddDay(7) + " 00:00:00");
		cinemaService.addTime(3, 6, 4, Ut.getAddDay(6) , 6, Ut.getAddDay(7) + " 01:00:00" , Ut.getAddDay(7) + " 03:00:00");
		
		
		
		
		
		
		
		
		
		
		
		
		
		return rq.jsHistoryBack("테스트 데이터가 추가되었습니다");
	}

	
	
	
	
	
	
	
	
	@Scheduled(cron="0 0 5 * * ?")
    public void deleteAfterTheaterTime() {
		 cinemaService.doDeleteAfterTheaterTime();
		 }

}
