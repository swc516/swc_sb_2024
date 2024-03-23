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
import com.swc.exam.demo.vo.Cinema;

@Mapper
public interface CinemaRepository {

	@Insert("""
			INSERT INTO cinema
			SET regDate = NOW(),
			updateDate = NOW(),
			region = #{region}
			""")
	void add(@Param("region") String region);

	@Select("SELECT LAST_INSERT_ID()")
	int getLastInsertId();

	
	@Select("""
			<script>
			SELECT COUNT(*) AS cnt
			FROM cinema AS C
			WHERE 1
			AND delStatus = 0
			AND C.region LIKE CONCAT('%', #{searchKeyword}, '%')
			</script>
			""")
	int getCinemasCount(String searchKeywordTypeCode, String searchKeyword);

	
	@Select("""
			<script>
			SELECT C.*
			FROM cinema AS C
			WHERE 1
			AND delStatus = 0
    		AND C.region LIKE CONCAT('%', #{searchKeyword}, '%')
			ORDER BY C.id DESC
			<if test="limitStart != -1">
			LIMIT #{limitStart}, #{limitTake}
			</if>
			</script>
			""")
	List<Cinema> getForPrintCinemas(String searchKeywordTypeCode, String searchKeyword, int limitStart, int limitTake);

	
	@Update("""
			<script>
			UPDATE cinema
			<set>
				delStatus = 1,
				delDate = NOW()
			</set>
			WHERE id = #{id}
			</script>
			""" )
	void deleteCinema(int id);

	@Select("""
			SELECT *
			FROM cinema AS C
			WHERE C.id = #{id}
			AND delStatus = 0
						""")
	Cinema getCinemaById(int id);

	@Select("""
			<script>
			SELECT *
			FROM cinema
			WHERE id = #{id}
			</script>
			""")
	Cinema getForPrintCinema(int id);

	@Update("""
			<script>
			UPDATE cinema
			SET updateDate = NOW(),
			region = #{region}
			WHERE id = #{id}
			</script>
			""")
	void modify(int id, String region);
}
