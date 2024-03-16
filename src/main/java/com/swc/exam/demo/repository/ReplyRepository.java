package com.swc.exam.demo.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReplyRepository {

	@Insert("""
			INSERT INTO reply
			SET regDate = NOW(),
			updateDate = NOW(),
			memberId= #{memberId},
			relTypeCode = #{relTypeCode},
			relId = #{relId},
			body = #{body}
			""")
	public void writeReply(@Param("memberId") int memberId, @Param("relTypeCode") String relTypeCode, @Param("relId") int relId, @Param("body") String body);

	
	@Select("""
			SELECT LAST_INSERT_ID()
			""")
	public int getLastInsertId();

}
