package com.swc.exam.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.ArticleRepository;
import com.swc.exam.vo.Article;

@Service
public class ArticleService {
	private ArticleRepository articleRepository;

	public ArticleService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	public Article writeArticle(String title, String body) {
		return articleRepository.writeArticle(title, body);
	}
	public List<Article> getArticles() {
		return articleRepository.getArticles();
	}
	public Article getArticle(int id) {
		return articleRepository.getArticle(id);
	}
	public void modifyArticle(int id, String title, String body) {
			articleRepository.modifyArticle(id, title, body);
	}
	public void deleteArticle(int id) {
		articleRepository.deleteArticle(id);
	}
}
