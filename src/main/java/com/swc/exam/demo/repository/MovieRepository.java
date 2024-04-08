package com.swc.exam.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.swc.exam.demo.vo.Member;
import com.swc.exam.demo.vo.Movie;

@Mapper
public interface MovieRepository {

	@Insert("""
			INSERT INTO movie
			SET regDate = NOW(),
			updateDate = NOW(),
			title = #{title},
			`body` = #{body},
			country = #{country},
			runningTime = #{runningTime},
			director = #{director},
			actor = #{actor},
			genre = #{genre},
			releaseDate = #{releaseDate},
			trailer = #{trailer}
			""")
	void add(String title, String body, String country, int runningTime, String director, String actor, String genre, String releaseDate, String trailer);

	@Select("SELECT LAST_INSERT_ID()")
	int getLastInsertId();

	
	@Select("""
			<script>
			SELECT COUNT(*) AS cnt
			FROM `movie` AS M
			WHERE 1
			AND delStatus = 0
			<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'title'">
			    		AND M.title LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'body'">
			    		AND M.body LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'director'">
			    		AND M.director LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'actor'">
			    		AND M.actor LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'genre'">
			    		AND M.genre LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'country'">
			    		AND M.country LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
					<otherwise>
						AND(
							M.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.body LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.director LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.actor LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.genre LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.country LIKE CONCAT('%', #{searchKeyword}, '%')
							)
					</otherwise>
				</choose>
			</if>
			</script>
			""")
	int getMoviesCount(String searchKeywordTypeCode, String searchKeyword);

	
	@Select("""
			<script>
			SELECT M.*
			FROM `movie` AS M
			WHERE 1
			AND delStatus = 0
			<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'title'">
			    		AND M.title LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'body'">
			    		AND M.body LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'director'">
			    		AND M.director LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'actor'">
			    		AND M.actor LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'genre'">
			    		AND M.genre LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'country'">
			    		AND M.country LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
					<otherwise>
						AND(
							M.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.body LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.director LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.actor LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.genre LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.country LIKE CONCAT('%', #{searchKeyword}, '%')
							)
					</otherwise>
				</choose>
			</if>
			ORDER BY M.id DESC
			<if test="limitStart != -1">
			LIMIT #{limitStart}, #{limitTake}
			</if>
			</script>
			""")
	List<Movie> getForPrintMovies(String searchKeywordTypeCode, String searchKeyword, int limitStart, int limitTake);
	
	
	@Select("""
			<script>
			SELECT COUNT(*) AS cnt
			FROM `movie` AS M
			WHERE 1
			<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'title'">
			    		AND M.title LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'body'">
			    		AND M.body LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'director'">
			    		AND M.director LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'actor'">
			    		AND M.actor LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'genre'">
			    		AND M.genre LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'country'">
			    		AND M.country LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
					<otherwise>
						AND(
							M.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.body LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.director LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.actor LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.genre LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.country LIKE CONCAT('%', #{searchKeyword}, '%')
							)
					</otherwise>
				</choose>
			</if>
			</script>
			""")
	int getAllMoviesCount(String searchKeywordTypeCode, String searchKeyword);
	
	
	@Select("""
			<script>
			SELECT M.*
			FROM `movie` AS M
			WHERE 1
			<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'title'">
			    		AND M.title LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'body'">
			    		AND M.body LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'director'">
			    		AND M.director LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'actor'">
			    		AND M.actor LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'genre'">
			    		AND M.genre LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'country'">
			    		AND M.country LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
					<otherwise>
						AND(
							M.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.body LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.director LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.actor LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.genre LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.country LIKE CONCAT('%', #{searchKeyword}, '%')
							)
					</otherwise>
				</choose>
			</if>
			ORDER BY M.id DESC
			<if test="limitStart != -1">
			LIMIT #{limitStart}, #{limitTake}
			</if>
			</script>
			""")
	List<Movie> getForPrintAllMovies(String searchKeywordTypeCode, String searchKeyword, int limitStart, int limitTake);

	
	@Update("""
			<script>
			UPDATE movie
			<set>
				delStatus = 1,
				delDate = NOW()
			</set>
			WHERE id = #{id}
			</script>
			""" )
	void deleteMovie(int id);

	@Select("""
			SELECT *
			FROM movie AS M
			WHERE M.id = #{id}
			AND delStatus = 0
						""")
	Movie getMovieById(int id);

	@Select("""
			<script>
			SELECT *
			FROM movie
			WHERE id = #{id}
			</script>
			""")
	Movie getForPrintMovie(int id);

	@Update("""
			<script>
			UPDATE movie
			<set>
				updateDate = NOW(),
				<if test="title != null">
					title = #{title},
				</if>
				<if test="body != null">
					body = #{body},
				</if>
				<if test="country != null">
					country = #{country},
				</if>
				<if test="runningTime != null">
					runningTime = #{runningTime},
				</if>
				<if test="director != null">
					director = #{director},
				</if>
				<if test="actor != null">
					actor = #{actor},
				</if>
				<if test="genre != null">
					genre = #{genre},
				</if>
				<if test="releaseDate != null">
					releaseDate = #{releaseDate},
				</if>
				<if test="trailer != null">
					trailer = #{trailer},
				</if>
			</set>
			WHERE id = #{id}
			</script>
			""")
	void modify(int id, String title, String body, String country, int runningTime, String director, String actor, String genre, String releaseDate, String trailer);

	@Select("""
			SELECT *
			FROM movie
			WHERE delStatus = 0; 
			""")
	List<Movie> getPlayingMovies();

	@Select("""
			SELECT title
			FROM movie
			WHERE id = #{movieId}
			""")
	String getMovieTitleById(int movieId);

	@Update("""
			UPDATE movie
			SET delStatus = 0
			WHERE id = #{id}
			""")
	void doDeleteCancel(int id);
	
	
}
