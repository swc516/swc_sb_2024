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
		System.out.println("onec|" + cinemaId + "*" + theaterInfoId + "*" +movieId + "*" +date + "*" +theaterTime + "*" +startTime + "*" +endTime );
		ResultData addRd = cinemaService.addTime(cinemaId, theaterInfoId, movieId, date, theaterTime, startTime, endTime);

		if (addRd.isFail()) {
			return rq.jsHistoryBack(addRd.getResultCode(), addRd.getMsg());
		}

		return rq.jsReplace(addRd.getMsg(),
				"/adm/cinema/theater/addTime?cinemaId=" + cinemaId);

	}

}
