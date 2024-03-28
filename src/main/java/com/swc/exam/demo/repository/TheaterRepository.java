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
			theaterName = #{theaterName},
			seatId = #{seatId},
			seatNo = #{seatNo},
			seatStatus = #{seatStatus}
			""")
	void add(String relTypeCode, int relId, String theaterName, char seatId, int seatNo, String seatStatus);

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
			SELECT theaterName
			FROM theater
			WHERE relTypeCode = #{relTypeCode}
			GROUP BY theaterName
			</script>
			""")
	List<Theater> getForPrintTheaters(String relTypeCode);

	@Delete("""
			<script>
			DELETE FROM theater
			WHERE theaterName = #{theaterName}
			AND relTypeCode = #{relTypeCode}
			</script>
			""" )
	void deleteTheater(String relTypeCode, String theaterName);

	@Select("""
			<script>
			SELECT *
			FROM theater
			WHERE relTypeCode= #{relTypeCode}
			AND theaterName = #{theaterName}
			</script>
			""")
	List<Theater> getForPrintTheater(String relTypeCode, String theaterName);

	
	@Update("""
			<script>
			UPDATE theater
			SET seatStatus = #{seatStatus}
			WHERE theaterName = #{theaterName}
			AND relTypeCode = #{relTypeCode}
			AND seatId = #{seatId}
			AND seatNo = #{seatNo}
			</script>
			""")
	void modifySeat(String theaterName, String relTypeCode, char seatId, int seatNo, String seatStatus);

	@Update("""
			<script>
			UPDATE theater
			SET relTypeCode = #{region}
			WHERE relId = #{id}
			</script>
			""")
	void modify(int id, String region);

	@Insert("""
			INSERT INTO theaterTime
			SET theaterName = #{theaterName},
			relTypeCode = #{region},
			relId = #{relId},
			movieId = #{movieId},
			date = #{date},
			time = #{time},
			startTime = #{startTime},
			endTime = #{endTime},
			seatId = #{seatId},
			seatNo = #{seatNo},
			seatStatus = #{seatStatus}
			""")
	void addTime(String theaterName, String region, int relId, int movieId, String date, int time, String startTime,
			String endTime, char seatId, String seatNo, String seatStatus);

	@Select("""
			SELECT relId
			FROM theater
			WHERE relTypeCode = #{relTypeCode}
			AND theaterName = #{theaterName}
			GROUP BY relTypeCode = #{relTypeCode}
			AND theaterName = #{theaterName};
			""")
	int getTheaterId(String relTypeCode, String theaterName);

}
