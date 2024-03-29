package com.swc.exam.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.swc.exam.demo.vo.Reply;
import com.swc.exam.demo.vo.ResultData;

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

	@Select("""
			SELECT R.*,
			M.nickname AS extra__writerName
			FROM reply AS R
			LEFT JOIN `member` AS M
			ON R.memberId = M.id
			WHERE R.relTypeCode = #{relTypeCode}
			AND R.relId = #{relId}
			ORDER BY R.id DESC
			""")
	public List<Reply> getForPrintReplies(@Param("memberId")int memberId, @Param("relTypeCode")String relTypeCode, @Param("relId")int relId);


	@Select("""
			SELECT R.*,
			M.nickname AS extra__writerName
			FROM reply AS R
			LEFT JOIN `member` AS M
			ON R.memberId = M.id
			WHERE R.id = #{id}
			""")
	public Reply getForPrintReply(@Param("memberId")int memberId, @Param("id")int id);

	@Select("""
			SELECT R.*
			FROM reply AS R
			WHERE R.id = #{id}
			""")
	public Reply getReply(@Param("id")int id);
	
	@Delete("""
			DELETE FROM reply
			WHERE id = #{id}
			""")
	public void deleteReply(@Param("id")int id);

	@Update("""
			UPDATE reply
			SET updateDate = NOW(), 
			`body` = #{body}			
			WHERE id = #{id}
			""")
	public void modifyReply(@Param("id")int id, @Param("body")String body);
	
	@Update("""
			<script>
			UPDATE reply
			SET goodReactionPoint = goodReactionPoint + 1
			WHERE id = #{id}
			</script>
			""")
	public int increaseGoodReactionPoint(@Param("id")int id);

	
	@Update("""
			<script>
			UPDATE reply
			SET badReactionPoint = badReactionPoint + 1
			WHERE id = #{id}
			</script>
			""")
	public int increaseBadReactionPoint(@Param("id")int id);

	
	@Update("""
			<script>
			UPDATE reply
			SET goodReactionPoint = goodReactionPoint - 1
			WHERE id = #{id}
			</script>
			""")
	public int decreaseGoodReactionPoint(@Param("id")int id);
	
	@Update("""
			<script>
			UPDATE reply
			SET badReactionPoint = badReactionPoint - 1
			WHERE id = #{id}
			</script>
			""")
	public int decreaseBadReactionPoint(@Param("id")int id);
	


}
