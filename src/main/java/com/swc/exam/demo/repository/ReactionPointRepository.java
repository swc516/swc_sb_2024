package com.swc.exam.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReactionPointRepository {

	
	@Select("""
			<script>
			SELECT IFNULL(SUM(RP.point), 0) AS s
			FROM reactionPoint AS Rp
			WHERE Rp.relTypeCode = #{relTypeCode}
			AND id = #{id}
			AND Rp.memberId = #{memberId}
			</script>
			""")
	public int actorCanMakeReactionPoint(@Param("memberId")int memberId, @Param("relTypeCode")String relTypeCode, @Param("id")int id);
}
