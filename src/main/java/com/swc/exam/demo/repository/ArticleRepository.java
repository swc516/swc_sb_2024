package com.swc.exam.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.swc.exam.demo.vo.Article;

@Mapper
public interface ArticleRepository {
	public void writeArticle(@Param("memberId") int memberId, @Param("boardId") int boardId, @Param("title") String title, @Param("body") String body);
	
	@Select("""
			<script>
			SELECT A.*,
			M.nickname AS extra__writerName,
			IFNULL(SUM(RP.point), 0) AS extra__sumReactionPoint,
			IFNULL(SUM(IF(RP.point &gt; 0, RP.point, 0)), 0) AS extra__goodReactionPoint,
			IFNULL(SUM(IF(RP.point &lt; 0, RP.point, 0)), 0) AS extra__badReactionPoint
			FROM article AS A
			LEFT JOIN `member` AS M
			ON A.memberId = M.id
			LEFT JOIN reactionPoint AS Rp
			ON Rp.relTypeCode = 'article'
			AND A.id = RP.relId			
			WHERE 1
			AND A.id = #{id}
			GROUP BY A.id
			</script>
			""")
	public Article getForPrintArticle(@Param("id") int id);

	public void deleteArticle(@Param("id") int id);

	public void modifyArticle(@Param("id") int id, @Param("title") String title, @Param("body") String body);

	@Select("""
			<script>
			SELECT A.*,
			IFNULL(SUM(RP.point), 0) AS extra__sumReactionPoint,
			IFNULL(SUM(IF(RP.point &gt; 0, RP.point, 0)), 0) AS extra__goodReactionPoint,
			IFNULL(SUM(IF(RP.point &lt; 0, RP.point, 0)), 0) AS extra__badReactionPoint
			FROM (
				SELECT A.*,
				M.nickname AS extra__writerName
				FROM article AS A
				LEFT JOIN `member` AS M
				ON A.memberId = M.id
				WHERE 1
				<if test="boardId != 0">
					AND A.boardId = #{boardId}
				</if>
				<if test="searchKeyword != ''">
					<choose>
						<when test="searchKeywordTypeCode == 'title'">
				    		AND A.title LIKE CONCAT('%', #{searchKeyword}, '%')
				    	</when>
				    	<when test="searchKeywordTypeCode == 'body'">
				    		AND A.body LIKE CONCAT('%', #{searchKeyword}, '%')
				    	</when>
						<otherwise>
							AND(
								A.title LIKE CONCAT('%', #{searchKeyword}, '%')
								OR
								A.body LIKE CONCAT('%', #{searchKeyword}, '%')
								)
						</otherwise>
					</choose>
				</if>
				<if test="limitStart != -1">
					LIMIT #{limitStart}, #{limitTake}
				</if>
			) AS A
			LEFT JOIN reactionPoint AS RP
			ON RP.relTypeCode = 'article'
			AND A.id = RP.relId
			GROUP BY A.id
			ORDER BY id DESC
			</script>
			""")
	public List<Article> getForPrintArticles(@Param("boardId") int boardId, @Param("limitStart") int limitStart, @Param("limitTake") int limitTake, @Param("searchKeyword") String searchKeyword, @Param("searchKeywordTypeCode") String searchKeywordTypeCode);

	public int getLastInsertId();

	@Select("""
			<script>
			SELECT COUNT(*) AS cnt
			FROM article AS A
			WHERE 1
			<if test="boardId != 0">
				AND A.boardId = #{boardId}
			</if>
			<if test="searchKeyword != ''">
				<choose>
					<when test="searchKeywordTypeCode == 'title'">
			    		AND A.title LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
			    	<when test="searchKeywordTypeCode == 'body'">
			    		AND A.body LIKE CONCAT('%', #{searchKeyword}, '%')
			    	</when>
					<otherwise>
						AND(
							A.title LIKE CONCAT('%', #{searchKeyword}, '%')
							OR
							A.body LIKE CONCAT('%', #{searchKeyword}, '%')
							)
					</otherwise>
				</choose>
			</if>
			</script>
			""") // AND A.title Like #{searchKeywordTypeCode} 로 사용시 에러, concat사용
	public int getArticlesCount(@Param("boardId") int boardId, @Param("searchKeywordTypeCode") String searchKeywordTypeCode, @Param("searchKeyword") String searchKeyword);
	
	
	@Update("""
			<script>
			UPDATE article
			SET hitCount = hitCount + 1
			WHERE id = #{id}
			</script>
			""")
	public int increaseHitCount(@Param("id") int id);

	
	@Select("""
			<script>
			SELECT hitCount
			FROM article
			WHERE id = #{id}
			</script>
			""")
	public int getArticleHitCount(@Param("id") int id);

	@Select("""
			<script>
			SELECT IFNULL(SUM(RP.point), 0) AS s
			FROM reactionPoint AS Rp
			WHERE Rp.relTypeCode = 'article'
			AND id = #{id}
			AND Rp.memberId = #{memberId}
			</script>
			""")
	public int actorCanMakeReactionPoint(@Param("memberId") int memberId, @Param("id") int id);
}
