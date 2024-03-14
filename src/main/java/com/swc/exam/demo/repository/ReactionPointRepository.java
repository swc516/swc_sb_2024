package com.swc.exam.demo.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReactionPointRepository {


	@Select("""
			SELECT IFNULL(SUM(RP.point), 0) AS s
			FROM reactionPoint AS RP
			WHERE RP.relTypeCode = #{relTypeCode}
			AND relId = #{relId}
			AND RP.memberId = #{memberId}
			""")
	public int getSumReactionPointMyMemberId(@Param("memberId")int actorId, @Param("relTypeCode")String relTypeCode, @Param("relId")int relId);
	
	
	@Insert("""
			INSERT INTO reactionPoint
			SET regDate = NOW(),
			updateDate = NOW(),
			relTypeCode = #{relTypeCode},
			relId = #{id},
			memberId= #{memberId},
			`point` = 1			
			""")
	public void addGoodReactionPoint(@Param("memberId")int memberId, @Param("relTypeCode")String relTypeCode, @Param("id")int id);

	@Insert("""
			INSERT INTO reactionPoint
			SET regDate = NOW(),
			updateDate = NOW(),
			relTypeCode = #{relTypeCode},
			relId = #{id},
			memberId= #{memberId},
			`point` = -1			
			""")
	public void addBadReactionPoint(@Param("memberId")int memberId, @Param("relTypeCode")String relTypeCode, @Param("id")int id);

}
