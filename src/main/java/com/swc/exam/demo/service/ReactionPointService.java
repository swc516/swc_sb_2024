package com.swc.exam.demo.service;

import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.ReactionPointRepository;
import com.swc.exam.demo.vo.ResultData;

@Service
public class ReactionPointService {
	private ReactionPointRepository reactionPointRepository;
	private ArticleService articleService;
	
	public ReactionPointService(ReactionPointRepository reactionPointRepository, ArticleService articleService) {
		this.reactionPointRepository = reactionPointRepository;
		this.articleService = articleService;
		
	}

	public boolean actorCanMakeReactionPoint(int actorId, String relTypeCode, int relId) {
		if (actorId ==0) {
			return false;
		}
		return reactionPointRepository.getSumReactionPointMyMemberId(actorId, relTypeCode, relId) == 0;
	}

	public ResultData addGoodReactionPoint(int actorId, String relTypeCode, int relId) {
		reactionPointRepository.addGoodReactionPoint(actorId, relTypeCode, relId);
		
		switch(relTypeCode) { 
		case "article":
			articleService.increaseGoodReactionPoint(relId);
			break;
		}
		
		return ResultData.from("S-1", "좋아요 처리 되었습니다.");
	}

	public ResultData addBadReactionPoint(int actorId, String relTypeCode, int relId) {
		reactionPointRepository.addBadReactionPoint(actorId, relTypeCode, relId);

		switch(relTypeCode) { 
		case "article":
			articleService.increaseBadReactionPoint(relId);
			break;
		}
		
		return ResultData.from("S-1", "싫어요 처리 되었습니다.");
	}
}
