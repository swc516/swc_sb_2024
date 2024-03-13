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
			SELECT A.*,
			M.nickname AS extra__writerName
			FROM article AS A
			LEFT JOIN member AS M
			ON A.memberId = M.id
			WHERE 1
			AND A.id = #{id}
			""")
	public Article getForPrintArticle(@Param("id") int id);

	public void deleteArticle(@Param("id") int id);

	public void modifyArticle(@Param("id") int id, @Param("title") String title, @Param("body") String body);

	@Select("""
			<script>
			SELECT A.*,
			M.nickname AS extra__writerName
			FROM article AS A
			LEFT JOIN member AS M
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
			ORDER BY id DESC
			<if test="limitStart != -1">
				LIMIT #{limitStart}, #{limitTake}
			</if>
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
}
