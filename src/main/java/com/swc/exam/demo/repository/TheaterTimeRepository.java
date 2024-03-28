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
import com.swc.exam.demo.vo.TheaterTime;
import com.swc.exam.demo.vo.Cinema;

@Mapper
public interface TheaterTimeRepository {

	@Select("""
			SELECT relTypeCode, theaterName, `time`, movieId, `date`, startTime, endTime, seatId, seatNo, seatStatus, seatSell
			FROM theaterTime
			WHERE `date` = #{date}
			AND `time` = #{time}
			AND theaterName = #{theaterName}
			AND relTypeCode = #{region};
						""")
	List<TheaterTime> getForPrintTheaterTime(String region, String theaterName, String date, int time);

	@Update("""
			<script>
			UPDATE theaterTime
			SET seatSell = 1, memberId = #{memberId}
			WHERE theaterName = #{theaterName}
			AND relTypeCode = #{region}
			AND `date` = #{date}
			AND `time` = #{time}
			AND seatId = #{seatId}
			AND seatNo = #{seatNo}
			</script>
			""")
	void doTicketing(String region, String theaterName, String date, int time, char seatId, int seatNo, int memberId);

}
