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
			runDate = #{runDate}
			""")
	void add(@Param("title") String title, @Param("body") String body, @Param("runDate") String runDate);

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
					<otherwise>
						AND(
							M.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.body LIKE CONCAT('%', #{searchKeyword}, '%')
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
					<otherwise>
						AND(
							M.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							M.body LIKE CONCAT('%', #{searchKeyword}, '%')
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
				<if test="runDate != null">
					runDate = #{runDate},
				</if>
			</set>
			WHERE id = #{id}
			</script>
			""")
	void modify(int id, String title, String body, String runDate);

	@Select("""
			SELECT *
			FROM movie
			WHERE runDate > NOW(); 
			""")
	List<Movie> getForPrintPlayingMovies();
	
	
}
