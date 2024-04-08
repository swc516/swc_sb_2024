package com.swc.exam.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.MemberRepository;
import com.swc.exam.demo.repository.MovieRepository;
import com.swc.exam.demo.repository.CinemaRepository;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Member;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.TheaterInfo;
import com.swc.exam.demo.vo.TheaterTime;
import com.swc.exam.demo.vo.Cinema;

@Service
public class CinemaService {

	private CinemaRepository cinemaRepository;
	private MovieService movieService;

	public CinemaService(CinemaRepository cinemaRepository, MovieService movieService) {
		this.cinemaRepository = cinemaRepository;
		this.movieService = movieService;
	}

	public ResultData add(String region, String branch) {

		Cinema oldCinema = cinemaDupCheck(region, branch);

		if (oldCinema != null) {
			return new ResultData("F-1", "같은 지점이 이미 존재합니다.");
		}

		cinemaRepository.add(region, branch);

		int id = cinemaRepository.getLastInsertId();

		return new ResultData("S-1", "영화관 추가가 완료되었습니다.", "id", id);
	}

	private Cinema cinemaDupCheck(String region, String branch) {
		Cinema oldCinema = cinemaRepository.cinemaDupCheck(region, branch);
		return oldCinema;
	}

	public int getCinemasCount(String searchKeywordTypeCode, String searchKeyword) {
		return cinemaRepository.getCinemasCount(searchKeywordTypeCode, searchKeyword);

	}

	public List<Cinema> getForPrintCinemas(String searchKeywordTypeCode, String searchKeyword, int itemsCountInAPage,
			int page) {

		int limitStart = (page - 1) * itemsCountInAPage;
		int limitTake = itemsCountInAPage;

		List<Cinema> cinema = cinemaRepository.getForPrintCinemas(searchKeywordTypeCode, searchKeyword, limitStart,
				limitTake);

		return cinema;
	}

	public void deleteCinemas(List<Integer> cinemasIds) {
		for (int cinemaId : cinemasIds) {
			Cinema cinema = getCinemaById(cinemaId);

			if (cinema != null) {
				deleteCinema(cinema);
			}
		}
	}

	private void deleteCinema(Cinema cinema) {
		cinemaRepository.deleteCinema(cinema.getId());
	}

	public Cinema getCinemaById(int id) {
		return cinemaRepository.getCinemaById(id);
	}

	public Cinema getForPrintCinema(int id) {
		Cinema cinema = cinemaRepository.getForPrintCinema(id);
		return cinema;
	}

	public ResultData modify(int id, String region, String branch) {

		Cinema oldCinema = cinemaDupCheck(region, branch);

		if (oldCinema != null) {
			return ResultData.from("F-1", "같은 지점이 이미 존재합니다.");
		}
		cinemaRepository.modify(id, region, branch);

		return ResultData.from("S-1", "영화관 정보가 수정되었습니다.");
	}

	public List<Cinema> getCinemaList() {
		List<Cinema> cinemas = cinemaRepository.getCinemaList();
		return cinemas;
	}

	///////////////////////////////////////////////////////

	public ResultData addTheaterInfo(int cinemaId, String theater, char seatRow, int seatCol, String seatStatus) {
		int A = 65;
		int theaterId = cinemaRepository.getLastTheaterInfoId() + 1;
		for (int i = A; i <= seatRow; i++) {
			for (int j = 1; j <= seatCol; j++) {
				cinemaRepository.addTheaterInfo(theaterId, cinemaId, theater, (char) i, j, seatStatus);
			}

		}

		return new ResultData("S-1", "영화관 추가가 완료되었습니다.");
	}

	public List<TheaterInfo> getTheaterInfoByCinemaId(int cinemaId) {
		List<TheaterInfo> theaterInfo = cinemaRepository.getTheaterInfoByCinemaId(cinemaId);
		return theaterInfo;
	}

	public List<TheaterInfo> getTheaterInfos(int theaterInfoId) {
		int cinemaId = getCinemaIdByTheaterInfoId(theaterInfoId);
		List<TheaterInfo> theaterInfo = cinemaRepository.getTheaterInfos(cinemaId, theaterInfoId);
		return theaterInfo;
	}

	public int getCinemaIdByTheaterInfoId(int theaterInfoId) {
		return cinemaRepository.getCinemaIdByTheaterInfoId(theaterInfoId);
	}

	public void modifySeat(int theaterInfoId, String[] seats, String seatStatus) {
		for (String seat : seats) {
			String[] seatSplit = seat.split("-");
			char seatId = seatSplit[0].trim().charAt(0);
			int seatNo = Integer.parseInt(seatSplit[1]);
			String newSeatStatus = seatStatus;
			cinemaRepository.modifySeat(theaterInfoId, seatId, seatNo, newSeatStatus);
		}
	}

	public void deleteTheaterInfo(int theaterInfoId) {
		cinemaRepository.deleteTheaterInfo(theaterInfoId);

	}

	public List<Movie> getPlayingMovies() {
		List<Movie> movies = movieService.getPlayingMovies();
		return movies;
	}

	public List<Cinema> getCinemaRegionList() {
		List<Cinema> regions = cinemaRepository.getCinemaRegionList();
		return regions;
	}

	public List<Cinema> getCinemaBranchList(String region) {
		List<Cinema> branchs = cinemaRepository.getCinemaBranchList(region);
		return branchs;
	}

	public List<TheaterInfo> getCinemaTheaterList(int cinemaId) {
		List<TheaterInfo> theaterInfos = cinemaRepository.getCinemaTheaterList(cinemaId);
		return theaterInfos;
	}

	public ResultData addTime(int cinemaId, int theaterInfoId, int movieId, String date, int theaterTime,
			String startTime, String endTime) {
		System.out.println("onec|" + cinemaId + "*" + theaterInfoId + "*" + movieId + "*" + date + "*" + theaterTime
				+ "*" + startTime + "*" + endTime);

		List<TheaterInfo> theaterInfos = cinemaRepository.getTheaterInfos(cinemaId, theaterInfoId);
		char seatRow;
		int seatCol;
		String seatStatus;
		int theaterTimeId = cinemaRepository.getLastTheaterTimeId() + 1;
		for (TheaterInfo theaterInfo : theaterInfos) {
			seatRow = theaterInfo.getSeatRow();
			seatCol = theaterInfo.getSeatCol();
			seatStatus = theaterInfo.getSeatStatus();
			cinemaRepository.addTime(cinemaId, theaterInfoId, theaterTimeId, movieId, date, theaterTime, startTime,
					endTime, seatRow, seatCol, seatStatus);
		}

		return new ResultData("S-1", "상영회차 추가가 완료되었습니다.");
	}

	public List<TheaterTime> getForPrintTheaterTime(int cinemaId, int theaterInfoId, int theaterTimeId) {
		List<TheaterTime> theaterTimes = cinemaRepository.getForPrintTheaterTime(cinemaId, theaterInfoId,
				theaterTimeId);
		return theaterTimes;
	}

	public List<TheaterTime> getTheaterTimeList(int movieId, int cinemaId, String date) {
		List<TheaterTime> theaterTimes = cinemaRepository.getTheaterTimeList(movieId, cinemaId, date);
		return theaterTimes;
	}

	public int getSellSeatCount(int theaterTimeId, String date, int theaterTime) {
		int sellSeatCount = cinemaRepository.getSellSeatCount(theaterTimeId, date, theaterTime);
		return sellSeatCount;
	}

	public int getMaxSeatCount(int theaterTimeId, String date, int theaterTime) {
		int maxSeatCount = cinemaRepository.getMaxSeatCount(theaterTimeId, date, theaterTime);
		return maxSeatCount;
	}

	public String getCinemaRegionById(int cinemaId) {
		String cinemaRegion = cinemaRepository.getCinemaRegionById(cinemaId);
		return cinemaRegion;

	}

	public String getCinemaBranchById(int cinemaId) {
		String cinemaBranch = cinemaRepository.getCinemaBranchById(cinemaId);
		return cinemaBranch;
	}

	public String getTheaterById(int theaterInfoId) {
		String theater = cinemaRepository.getTheaterById(theaterInfoId);
		return theater;
	}

	public void doTicketing(int theaterInfoId, int theaterTimeId, char seatRow, int seatCol, int memberId) {
		cinemaRepository.doTicketing(theaterInfoId, theaterTimeId, seatRow, seatCol, memberId);

	}

	public void doTicketCancel(int id) {
		cinemaRepository.doTicketCancel(id);
	}

	public List<TheaterInfo> getForPrintTheaterInfo(String cinema, String theater) {
		int cinemaId = getCinemaIdByRegionAndBranch(cinema);
		int theaterInfoId = getTheaterInfoIdByCinemaIdAndTheaterInfo(cinemaId, theater);

		List<TheaterInfo> theaterInfos = cinemaRepository.getForPrintTheaterInfo(cinemaId, theaterInfoId);
		return theaterInfos;
	}

	public int getCinemaIdByRegionAndBranch(String cinema) {
		String[] cinemaSplit = cinema.split("_");
		String region = cinemaSplit[0];
		String branch = cinemaSplit[1];

		int cinemaId = cinemaRepository.getCinemaIdByRegionAndBranch(region, branch);
		return cinemaId;
	}

	public int getTheaterInfoIdByCinemaIdAndTheaterInfo(int cinemaId, String theater) {
		int theaterInfoId = cinemaRepository.getTheaterInfoIdByCinemaIdAndTheaterInfo(cinemaId, theater);
		return theaterInfoId;
	}

	public void doDeleteAfterTheaterTime() {
		cinemaRepository.doDeleteAfterTheaterTime();
	}
}
