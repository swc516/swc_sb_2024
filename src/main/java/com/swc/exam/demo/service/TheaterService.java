package com.swc.exam.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.TheaterRepository;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Member;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Theater;
import com.swc.exam.demo.vo.Cinema;

@Service
public class TheaterService {

	private TheaterRepository theaterRepository;

	public TheaterService(TheaterRepository theaterRepository) {
		this.theaterRepository = theaterRepository;
	}

	public ResultData add(String relTypeCode, int relId, String theater, char seatIdChar, int seatNo) {
		int A = 65;
		int seatId = seatIdChar;
		for(int i = A; i <= seatId; i++) {
			
			for (int j = 1; j <= seatNo; j++) {
				theaterRepository.add(relTypeCode, relId, theater, (char)i, j);
			}
			
		}

		

		return new ResultData("S-1", "영화관 추가가 완료되었습니다.");
	}

	public int getTheaterCount() {
		return theaterRepository.getTheatersCount();

	}

	public List<Theater> getForPrintTheater(String searchKeywordTypeCode, String searchKeyword, int itemsCountInAPage,
			int page) {

		int limitStart = (page - 1) * itemsCountInAPage;
		int limitTake = itemsCountInAPage;

		List<Theater> theater = theaterRepository.getForPrintTheaters(limitStart, limitTake);

		return theater;
	}

	public void deleteTheater(String theater) {
		theaterRepository.deleteTheater(theater);
	}

}
