package test.dao;

import java.util.List;

import test.domain.Qna;
import test.domain.Restaurant;

public interface QnaDao {
	public abstract List<Qna> getQnaList() ;
	public abstract Qna selectQna(int QNAID) ;
	public abstract int insertQna(Qna qna) ;
	public abstract int updateQna(Qna qna) ;
	public abstract int deleteQna(int QNAID) ;
}
