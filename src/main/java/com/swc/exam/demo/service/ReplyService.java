package com.swc.exam.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swc.exam.demo.repository.ReplyRepository;
import com.swc.exam.demo.util.Ut;
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

	public List<Reply> getForPrintReplies(int actorId, String relTypeCode, int relId) {
		return replyRepository.getForPrintReplies(actorId, relTypeCode, relId);
	}

}
