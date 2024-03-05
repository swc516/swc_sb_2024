package com.swc.exam.demo.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.swc.exam.vo.Article;

@Mapper
public interface ArticleRepository {
	public Article writeArticle(String title, String body);
	
	@Select("SELECT * FROM article WHERE id = #{id}")
	public Article getArticle(@Param("id") int id);

	public void deleteArticle(int id);

	public void modifyArticle(int id, String title, String body);
	
	public List<Article> getArticles();
}
