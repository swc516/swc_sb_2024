package com.swc.exam.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.swc.exam.demo.vo.Article;

@Mapper
public interface ArticleRepository {
	
	public void writeArticle(@Param("memberId") int memberId, @Param("title") String title, @Param("body") String body);

/*	@Select("""
			SELECT A.*,
			M.nickname AS extra__writerName
			FROM article AS A
			LEFT JOIN member AS M
			ON A.memberId = M.id
			WHERE 1
			AND A.id = #{id}
			ORDER BY id DESC
			""")*/
	@Select("""
			<script>
			SELECT A.*,
			M.nickname AS extra__writerName
			FROM article AS A
			LEFT JOIN member AS M
			ON A.memberId = M.id
			WHERE 1
			<if test="boardId !=0">
				AND A.boardId = #{boardId}
			</if>
			ORDER BY id DESC
			</script>
			""")
	public Article getForPrintArticle(@Param("id") int id);

	public void deleteArticle(@Param("id") int id);

	public void modifyArticle(@Param("id") int id, @Param("title") String title, @Param("body") String body);

	@Select("""
			SELECT A.*,
			M.nickname AS extra__writerName
			FROM article AS A
			LEFT JOIN MEMBER AS M
			ON A.memberId = M.id
			WHERE A.boardId = #{boardId}
			ORDER BY id DESC
			""")
	public List<Article> getForPrintArticles(@Param("boardId")int boardId);

	public int getLastInsertId();
}
