package com.swc.exam.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.ReplyRepository;
import com.swc.exam.demo.util.Ut;
import com.swc.exam.demo.vo.Article;
import com.swc.exam.demo.vo.Reply;
import com.swc.exam.demo.vo.ResultData;
import com.swc.exam.demo.vo.Rq;

@Service
public class ReplyService {
	private ReplyRepository replyRepository;
	private Rq rq;

	public ReplyService(ReplyRepository replyRepository, Rq rq) {
		this.replyRepository = replyRepository;
		this.rq = rq;
	}

	public ResultData<Integer> writeReply(int actorId, String relTypeCode, int relId, String body) {
		replyRepository.writeReply(actorId, relTypeCode, relId, body);
		int id = replyRepository.getLastInsertId();

		return ResultData.from("S-1", Ut.f("%d번 댓글이 생성되었습니다.", id), "id", id);
	}
	
	public ResultData<Integer> writeReview(int actorId, String relTypeCode, int relId, String body, double rate) {
		replyRepository.writeReview(actorId, relTypeCode, relId, body, rate);
		int id = replyRepository.getLastInsertId();
		
		return ResultData.from("S-1", Ut.f("%d번 리뷰가 생성되었습니다.", id), "id", id);
	}

	public List<Reply> getForPrintReplies(int actorId, String relTypeCode, int relId) {
		List<Reply> replies = replyRepository.getForPrintReplies(actorId, relTypeCode, relId);
		for (Reply reply : replies) {
			updateForPrintData(actorId, reply);
		}

		return replies;
	}

	private void updateForPrintData(int actorId, Reply reply) {
		if (reply == null) {
			return;
		}

		ResultData actorCanDeleteRd = actorCanDelete(actorId, reply);
		reply.setExtra__actorCanDelete(actorCanDeleteRd.isSuccess());

		ResultData actorCanModifyRd = actorCanModify(actorId, reply);
		reply.setExtra__actorCanModify(actorCanModifyRd.isSuccess());

	}

	private ResultData actorCanModify(int actorId, Reply reply) {
		if (reply == null) {
			return ResultData.from("F-1", "댓글이 존재하지 않습니다.");
		}
		if (reply.getMemberId() != actorId) {
			return ResultData.from("F-2", "권한이 없습니다.");
		}
		return ResultData.from("S-1", "댓글 수정이 가능합니다.");
	}

	private ResultData actorCanDelete(int actorId, Reply reply) {
		if (reply == null) {
			return ResultData.from("F-1", "댓글이 존재하지 않습니다.");
		}
		if (reply.getMemberId() != actorId) {
			return ResultData.from("F-2", "권한이 없습니다.");
		}
		return ResultData.from("S-1", "댓글 삭제가 가능합니다.");
	}

	public Reply getReply(int id) {
		return replyRepository.getReply(id);
	}

	public Reply getForPrintReply(int actorId, int id) {
		Reply reply = replyRepository.getForPrintReply(actorId, id);
		updateForPrintData(actorId, reply);

		return reply;
	}

	public ResultData<Integer> deleteReply(int id) {
		replyRepository.deleteReply(id);
		return ResultData.from("S-1", Ut.f("%d번 댓글을 삭제하였습니다.", id));
	}

	public ResultData modifyReply(int id, String body) {
		replyRepository.modifyReply(id, body);
		return ResultData.from("S-1", Ut.f("%d번 댓글을 수정하였습니다.", id));
	}

	public ResultData increaseGoodReactionPoint(int relId) {
		int affectedRowsCount = replyRepository.increaseGoodReactionPoint(relId);

		if (affectedRowsCount == 0) {
			return ResultData.from("F-1", "해당 댓글이 존재하지 않습니다.", "affectedRowsCount", affectedRowsCount);
		}
		return ResultData.from("S-1", "좋아요 수가 증가되었습니다.", "affectedRowsCount", affectedRowsCount);
	}

	public ResultData increaseBadReactionPoint(int relId) {
		int affectedRowsCount = replyRepository.increaseBadReactionPoint(relId);

		if (affectedRowsCount == 0) {
			return ResultData.from("F-1", "해당 댓글이 존재하지 않습니다.", "affectedRowsCount", affectedRowsCount);
		}
		return ResultData.from("S-1", "싫어요 수가 증가되었습니다.", "affectedRowsCount", affectedRowsCount);
	}

	public ResultData decreaseGoodReactionPoint(int relId) {
		int affectedRowsCount = replyRepository.decreaseGoodReactionPoint(relId);

		if (affectedRowsCount == 0) {
			return ResultData.from("F-1", "해당 댓글이 존재하지 않습니다.", "affectedRowsCount", affectedRowsCount);
		}
		return ResultData.from("S-1", "좋아요 수가 감소되었습니다.", "affectedRowsCount", affectedRowsCount);

	}

	public ResultData decreaseBadReactionPoint(int relId) {
		int affectedRowsCount = replyRepository.decreaseBadReactionPoint(relId);

		if (affectedRowsCount == 0) {
			return ResultData.from("F-1", "해당 댓글이 존재하지 않습니다.", "affectedRowsCount", affectedRowsCount);
		}
		return ResultData.from("S-1", "싫어요 수가 감소되었습니다.", "affectedRowsCount", affectedRowsCount);

	}

	public String getMemberId(String searchKeyword) {
		String memberId = replyRepository.getMemberId(searchKeyword);
		return memberId;
	}

	public int getReplysCount(String searchKeywordTypeCode, String searchKeyword) {
		return replyRepository.getReplysCount(searchKeywordTypeCode, searchKeyword);
	}

	public List<Reply> getForPrintReplys(String searchKeyword, String searchKeywordTypeCode, int itemsCountInAPage,
			int page) {
		int limitStart = (page - 1) * itemsCountInAPage;
		int limitTake = itemsCountInAPage;

		List<Reply> replys = replyRepository.getForPrintReplys(limitStart, limitTake, searchKeyword,
				searchKeywordTypeCode);

		return replys;
	}

	public void deleteReplys(List<Integer> replyIds) {
		for (int replyId : replyIds) {
			deleteReply(replyId);
	
		}
	}

	public double getRateAvg(String relTypeCode, int relId) {
		double rateAvg = replyRepository.getRateAvg(relTypeCode, relId);
		return rateAvg;
	}

	public List<Reply> getForPrintReviews(int id) {
		List<Reply> review = replyRepository.getForPrintReviews(id);
		return review;
	}

	public int hasReviewWrite(int loginedMemberId, int movieId) {
		int check = replyRepository.hasReviewWrite(loginedMemberId, movieId);
		return check;
	}
	
}
