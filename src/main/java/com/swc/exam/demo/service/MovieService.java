package com.swc.exam.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.MemberRepository;
import com.swc.exam.demo.repository.MovieRepository;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Member;
import com.swc.exam.demo.vo.Movie;
import com.swc.exam.demo.vo.ResultData;

@Service
public class MovieService {

	private MovieRepository movieRepository;

	public MovieService(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public ResultData add(String title, String body, String runDate) {

		movieRepository.add(title, body, runDate);

		int id = movieRepository.getLastInsertId();

		return new ResultData("S-1", "영화 추가가 완료되었습니다.", "id", id);
	}

	public int getMoviesCount(String searchKeywordTypeCode, String searchKeyword) {
		return movieRepository.getMoviesCount(searchKeywordTypeCode, searchKeyword);

	}

	public List<Movie> getForPrintMovies(String searchKeywordTypeCode, String searchKeyword, int itemsCountInAPage,
			int page) {

		int limitStart = (page - 1) * itemsCountInAPage;
		int limitTake = itemsCountInAPage;

		List<Movie> movies = movieRepository.getForPrintMovies(searchKeywordTypeCode, searchKeyword, limitStart,
				limitTake);

		return movies;
	}

	public void deleteMovies(List<Integer> movieIds) {
		for (int movieId : movieIds) {
			Movie movie = getMovieById(movieId);

			if (movie != null) {
				deleteMovie(movie);
			}
		}
	}

	private void deleteMovie(Movie movie) {
		movieRepository.deleteMovie(movie.getId());
	}

	public Movie getMovieById(int id) {
		return movieRepository.getMovieById(id);
	}

	public Movie getForPrintMovie(int id) {
		Movie movie = movieRepository.getForPrintMovie(id);
		return movie;
	}

	public ResultData modify(int id, String title, String body, String runDate) {
		movieRepository.modify(id, title, body, runDate);

		return ResultData.from("S-1", "영화정보가 수정되었습니다.");
	}

	public List<Movie> getForPrintPlayingMovies() {
		List<Movie> movies = movieRepository.getForPrintPlayingMovies();
		
		return movies;
	}


}
