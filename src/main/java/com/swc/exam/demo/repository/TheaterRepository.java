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
import com.swc.exam.demo.vo.Theater;
import com.swc.exam.demo.vo.Cinema;

@Mapper
public interface TheaterRepository {

	@Insert("""
			INSERT INTO theater
			SET relTypeCode = #{relTypeCode},
			relId = #{relId},
			theater = #{theater},
			seatId = #{seatId},
			seatNo = #{seatNo}
			""")
	void add(String relTypeCode, int relId, String theater, char seatId, int seatNo);

	@Select("SELECT LAST_INSERT_ID()")
	int getLastInsertId();

	
	@Select("""
			<script>
			SELECT COUNT(*)
			FROM theater
			</script>
			""")
	int getTheatersCount();

	
	@Select("""
			<script>
			SELECT *
			FROM theater
			ORDER BY id DESC
			<if test="limitStart != -1">
			LIMIT #{limitStart}, #{limitTake}
			</if>
			</script>
			""")
	List<Theater> getForPrintTheaters(int limitStart, int limitTake);

	@Delete("""
			<script>
			DELETE FROM theater
			WHERE theater = #{theater}
			</script>
			""" )
	void deleteTheater(String theater);

	@Select("""
			<script>
			SELECT *
			FROM theater
			WHERE  
			theater = #{theater}
			</script>
			""")
	Theater getForPrintTheater(String theater);

	@Update("""
			<script>
			UPDATE theater
			SET theater = #{theater}
			WHERE id = #{id}
			</script>
			""")
	void modify(int id, String theater);
}
