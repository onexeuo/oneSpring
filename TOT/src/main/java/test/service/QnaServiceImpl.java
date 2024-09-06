package test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.dao.QnaDao;
import test.domain.Qna;
import test.domain.Restaurant;

@Service
public class QnaServiceImpl implements QnaService {

	@Autowired
	private QnaDao qnaDao;
	
	@Override
	public List<Qna> getQnaList() {
		return qnaDao.getQnaList();
	}

	@Override
	public Qna selectQna(int QNAID) {
		return qnaDao.selectQna(QNAID);
	}

	@Override
	public int insertQna(Qna qna) {
		return qnaDao.insertQna(qna);
	}

	@Override
	public int updateQna(Qna qna) {
		return qnaDao.updateQna(qna);
	}

	@Override
	public int deleteQna(int QNAID) {
		return qnaDao.deleteQna(QNAID);
	}

}
