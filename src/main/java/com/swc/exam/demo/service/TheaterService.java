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

	public ResultData add(String relTypeCode, int relId, String theaterName, char seatId, String seatNo,
			String seatStatus) {
		int A = 65;
		int seatIdtoInt = seatId;
		for (int i = A; i <= seatIdtoInt; i++) {

			for (int j = 1; j <= Integer.parseInt(seatNo); j++) {
				theaterRepository.add(relTypeCode, relId, theaterName, (char) i, j, seatStatus);
			}

		}

		return new ResultData("S-1", "영화관 추가가 완료되었습니다.");
	}

	public int getTheaterCount() {
		return theaterRepository.getTheatersCount();

	}

	public void deleteTheater(String relTypeCode, String theaterName) {
		theaterRepository.deleteTheater(relTypeCode, theaterName);
	}

	public List<Theater> getForPrintTheaters(String relTypeCode) {
		List<Theater> theaters = theaterRepository.getForPrintTheaters(relTypeCode);
		return theaters;
	}

	public List<Theater> getForPrintTheater(String relTypeCode, String theaterName) {
		List<Theater> theater = theaterRepository.getForPrintTheater(relTypeCode, theaterName);
		return theater;
	}

	public void modifySeat(String theaterName, String relTypeCode, String[] seats, String seatStatus) {
		for (String seat : seats) {
			String[] seatSplit = seat.split("-");
			char seatId = seatSplit[0].trim().charAt(0);
			int seatNo = Integer.parseInt(seatSplit[1]);
			String newSeatStatus = seatStatus;
			theaterRepository.modifySeat(theaterName, relTypeCode, seatId, seatNo, newSeatStatus);
		}
	}

	public void modify(int id, String region) {
		theaterRepository.modify(id, region);
	}

	public ResultData addTime(String theaterName, String region, int movieId, String date, int time, String startTime,
			String endTime) {
		List<Theater> theaters = theaterRepository.getForPrintTheater(region, theaterName);
		char seatId;
		String seatNo;
		String seatStatus;
		int relId;

		for (Theater theater : theaters) {
			seatId = theater.getSeatId();
			seatNo = theater.getSeatNo();
			seatStatus = theater.getSeatStatus();
			relId = theater.getRelId();
			theaterRepository.addTime(theaterName, region, relId, movieId, date, time, startTime, endTime, seatId, seatNo,
					seatStatus);
		}

		return new ResultData("S-1", "상영회차 추가가 완료되었습니다.");
	}

	public int getTheaterId(String relTypeCode, String theaterName) {
		int id = theaterRepository.getTheaterId(relTypeCode, theaterName);
		return id;

	}

	public List<Theater> getTheaterList() {
		List<Theater> theaters = theaterRepository.getTheaterList();
		return theaters;
	}

}
