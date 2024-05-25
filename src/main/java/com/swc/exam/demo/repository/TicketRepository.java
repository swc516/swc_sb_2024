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
import com.swc.exam.demo.vo.Ticket;
import com.swc.exam.demo.vo.Cinema;

@Mapper
public interface TicketRepository {

	@Select("""
			SELECT *
			FROM ticket
			WHERE buyMemberId = #{id}
		
						""")
	List<Ticket> getMyTicketingList(int id);

	@Delete("""
			DELETE FROM ticket
			WHERE id = #{id}
			""")
	void doTicketCancel(int id);

	@Insert("""
			INSERT INTO ticket
			set buyDate = now(),
			buyMemberId = #{memberId},
			movieTitle = #{movieTitle},
			cinema = #{cinema},
			theater = #{theater},
			time = #{time},
			startTime = #{startTime},
			playingTime = #{playingTime},
			seat = #{totalSeat}
			""")
	void doTicketing(int memberId, String movieTitle, String cinema, String theater, int time, String startTime, String playingTime, String totalSeat);

	@Select("""
			SELECT *
			FROM ticket
			WHERE id = #{ticketId}
			""")
	Ticket getTicketById(int ticketId);

	
	

}
