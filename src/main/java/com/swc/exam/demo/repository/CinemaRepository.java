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
import com.swc.exam.demo.vo.TheaterInfo;
import com.swc.exam.demo.vo.TheaterTime;
import com.swc.exam.demo.vo.Cinema;

@Mapper
public interface CinemaRepository {

	@Insert("""
			INSERT INTO cinema
			SET regDate = NOW(),
			updateDate = NOW(),
			region = #{region},
			branch = #{branch}
			""")
	void add(String region, String branch);

	@Select("SELECT LAST_INSERT_ID()")
	int getLastInsertId();

	@Select("""
			<script>
			SELECT COUNT(*) AS cnt
			FROM cinema
			WHERE 1
			AND delStatus = 0
			 		<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'region'">
			    		AND region LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'branch'">
			    		AND branch LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
					<otherwise>
						AND(
							region LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							branch LIKE CONCAT('%', #{searchKeyword}, '%')
							)
					</otherwise>
				</choose>
			</if>
			</script>
			""")
	int getCinemasCount(String searchKeywordTypeCode, String searchKeyword);

	@Select("""
			<script>
			SELECT C.*,
			GROUP_CONCAT(DISTINCT T.theaterInfoId) AS extra__theaterInfoId,
			GROUP_CONCAT(DISTINCT T.theater) AS extra__theater
			FROM cinema AS C
			LEFT JOIN theaterInfo AS T
			ON C.id = T.cinemaId
			WHERE 1
			AND delStatus = 0
			 		<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'region'">
			    		AND region LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'branch'">
			    		AND branch LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
					<otherwise>
						AND(
							region LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							branch LIKE CONCAT('%', #{searchKeyword}, '%')
							)
					</otherwise>
				</choose>
			</if>
			GROUP BY C.id
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
			""")
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
			region = #{region},
			branch = #{branch}
			WHERE id = #{id}
			</script>
			""")
	void modify(int id, String region, String branch);

	@Select("""
			<script>
			SELECT *
			FROM cinema
			WHERE delStatus = 0
			ORDER BY region
			</script>
			""")
	List<Cinema> getCinemaList();

	@Select("""
			SELECT region, branch
			FROM cinema
			WHERE region = #{region}
			AND branch = #{branch}
			""")
	Cinema cinemaDupCheck(String region, String branch);

	//////////

	@Select("""
			SELECT IFNULL(MAX(theaterInfoId),0)
			FROM theaterInfo
			""")
	int getLastTheaterInfoId();

	@Select("""
			SELECT IFNULL(MAX(theaterTimeId),0)
			FROM theaterTime
			""")
	int getLastTheaterTimeId();

	@Insert("""
			INSERT INTO theaterInfo
			SET theaterInfoId = #{theaterInfoId},
			cinemaId = #{cinemaId},
			theater = #{theater},
			seatRow = #{seatRow},
			seatCol = #{seatCol},
			seatStatus = #{seatStatus}
			""")
	void addTheaterInfo(int theaterInfoId, int cinemaId, String theater, char seatRow, int seatCol, String seatStatus);

	@Select("""
			<script>
			SELECT theater, theaterInfoId
			FROM theaterInfo
			WHERE cinemaId = #{cinemaId}
			GROUP BY theater
			</script>
			""")
	List<TheaterInfo> getTheaterInfoByCinemaId(int cinemaId);

	@Select("""
			<script>
			SELECT T.*,
			C.region AS extra__region,
			C.branch AS extra__branch
			FROM theaterInfo AS T
			LEFT JOIN cinema AS C
			ON T.cinemaId = C.id
			WHERE cinemaId = #{cinemaId}
			AND theaterInfoId = #{theaterInfoId}
			</script>
			""")
	List<TheaterInfo> getTheaterInfos(int cinemaId, int theaterInfoId);

	@Update("""
			<script>
			UPDATE theaterInfo
			SET seatStatus = #{seatStatus}
			WHERE theaterInfoId = #{theaterInfoId}
			AND seatRow = #{seatRow}
			AND seatCol = #{seatCol}
			</script>
			""")
	void modifySeat(int theaterInfoId, char seatRow, int seatCol, String seatStatus);

	@Select("""
			SELECT cinemaId
			FROM theaterInfo
			WHERE theaterInfoId = #{theaterInfoId}
			GROUP BY cinemaId
			""")
	int getCinemaIdByTheaterInfoId(int theaterInfoId);

	@Delete("""
			<script>
			DELETE FROM theaterInfo
			WHERE theaterInfoId = #{theaterInfoId}
			</script>
			""")
	void deleteTheaterInfo(int theaterInfoId);

	@Select("""
			<script>
			SELECT region
			FROM cinema
			GROUP BY region
			</script>
			""")
	List<Cinema> getCinemaRegionList();

	@Select("""
			<script>
			SELECT branch
			FROM cinema
			WHERE region = #{region}
			AND delStatus = 0;
			</script>
			""")
	List<Cinema> getCinemaBranchList(String region);

	@Select("""
			<script>
			SELECT theater, theaterInfoId
			FROM theaterInfo
			WHERE cinemaId = #{cinemaId}
			GROUP BY theater
			</script>
			""")
	List<TheaterInfo> getCinemaTheaterList(int cinemaId);

	@Select("""
			<script>
			SELECT id
			FROM cinema
			WHERE region = #{region}
			AND branch = #{branch}
			</script>
			""")
	int findIdByRegionAndBranch(String region, String branch);

	@Insert("""
			INSERT INTO theaterTime
			SET cinemaId = #{cinemaId},
			theaterInfoId = #{theaterInfoId},
			theaterTimeId = #{theaterTimeId},
			theaterTime = #{theaterTime},
			date = #{date},
			startTime = #{startTime},
			endTime = #{endTime},
			movieId = #{movieId},
			seatRow = #{seatRow},
			seatCol = #{seatCol},
			seatStatus = #{seatStatus}
			""")
	void addTime(int cinemaId, int theaterInfoId, int theaterTimeId, int movieId, String date, int theaterTime,
			String startTime, String endTime, char seatRow, int seatCol, String seatStatus);

	@Select("""
			SELECT *
			FROM theaterTime
			WHERE theaterTimeId = #{theaterTimeId}
			AND theaterInfoId = #{theaterInfoId}
			AND cinemaId = #{cinemaId};
						""")
	List<TheaterTime> getForPrintTheaterTime(int cinemaId, int theaterInfoId, int theaterTimeId);

	@Select("""
			SELECT TT.*,
			TI.theater AS extra__theater
			FROM theaterTime AS TT
			LEFT JOIN theaterInfo AS TI
			ON TT.theaterInfoId = TI.theaterInfoId
			WHERE TT.movieId = #{movieId}
			AND TT.cinemaId = #{cinemaId}
			AND TT.`date` = #{date}
			GROUP BY TT.startTime
			ORDER BY TT.startTime DESC
						""")
	List<TheaterTime> getTheaterTimeList(int movieId, int cinemaId, String date);

	@Select("""
			SELECT COUNT(*)
			FROM theaterTime
			WHERE seatStatus != '없음'
			AND seatSell = 1
			AND theaterTimeId = #{theaterTimeId}
			AND `date` = #{date}
			AND theaterTime = #{theaterTime}
									""")
	int getSellSeatCount(int theaterTimeId, String date, int theaterTime);

	@Select("""
			SELECT COUNT(*)
			FROM theaterTime
			WHERE seatStatus != '없음'
			AND theaterTimeId = #{theaterTimeId}
			AND `date` = #{date}
			AND theaterTime = #{theaterTime}
										""")
	int getMaxSeatCount(int theaterTimeId, String date, int theaterTime);

	@Select("""
			SELECT region
			FROM cinema
			WHERE id = #{cinemaId}
			""")
	String getCinemaRegionById(int cinemaId);

	@Select("""
			SELECT branch
			FROM cinema
			WHERE id = #{cinemaId}
			""")
	String getCinemaBranchById(int cinemaId);

	
	@Select("""
			SELECT theater
			FROM theaterInfo
			WHERE theaterInfoId = #{theaterInfoId}
			GROUP BY theater
			""")
	String getTheaterById(int theaterInfoId);

	
	@Update("""
			<script>
			UPDATE theaterTime
			SET seatSell = 1, buyMemberId = #{memberId}, buyDate = NOW()
			WHERE theaterInfoId = #{theaterInfoId}
			AND theaterTimeId = #{theaterTimeId}
			AND seatRow = #{seatRow}
			AND seatCol = #{seatCol}
			</script>
			""")
	void doTicketing(int theaterInfoId, int theaterTimeId, char seatRow,
			int seatCol, int memberId);

	@Select("""
			SELECT TT.*,
			M.title AS extra__movieTitle,
			C.region AS extra__cinemaRegion,
			C.branch AS extra__cinemabranch,
			TI.theater AS extra__theater
			FROM theaterTime AS TT
			LEFT JOIN movie AS M
			ON TT.movieId = M.id
			LEFT JOIN cinema AS C
			ON TT.cinemaId = C.id
			LEFT JOIN theaterInfo AS TI
			ON TT.theaterInfoId = TI.theaterInfoId
			WHERE TT.buyMemberId = #{id}
			GROUP BY TT.id
						""")
	List<TheaterTime> getMyTicketingList(int id);

	@Update("""
			UPDATE theaterTime
			SET seatSell = 0, buyMemberId = 0, buyDate = null
			WHERE id = #{id}
			""")
	void doTicketCancel(int id);

	@Select("""
			SELECT *
			FROM theaterInfo
			WHERE cinemaId = #{cinemaId}
			AND theaterInfoId = #{theaterInfoId}
			""")
	List<TheaterInfo> getForPrintTheaterInfo(int cinemaId, int theaterInfoId);
}
