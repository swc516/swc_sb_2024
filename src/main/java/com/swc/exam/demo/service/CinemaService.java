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
import com.swc.exam.demo.vo.Theater;
import com.swc.exam.demo.vo.Cinema;

@Service
public class CinemaService {

	private CinemaRepository cinemaRepository;
	private TheaterService theaterService;
	
	
	public CinemaService(CinemaRepository cinemaRepository, TheaterService theaterService) {
		this.cinemaRepository = cinemaRepository;
		this.theaterService = theaterService;
	}

	public ResultData add(String region) {

		cinemaRepository.add(region);

		int id = cinemaRepository.getLastInsertId();

		return new ResultData("S-1", "영화관 추가가 완료되었습니다.", "id", id);
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

	public ResultData modify(int id, String region) {
		cinemaRepository.modify(id, region);
		theaterService.modify(id, region);

		return ResultData.from("S-1", "영화관 정보가 수정되었습니다.");
	}

	public List<Theater> getForPrintTheaters(String relTypeCode) {
		List<Theater> theater = theaterService.getForPrintTheaters(relTypeCode);
		return theater;
	}
	
}
