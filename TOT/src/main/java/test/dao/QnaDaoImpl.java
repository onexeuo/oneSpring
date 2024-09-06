package test.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import test.domain.Qna;
import test.domain.Restaurant;

@Repository
public class QnaDaoImpl implements QnaDao {
	
	@Autowired
	public SqlSessionFactory sqlSessionFactory;

	@Override
	public List<Qna> getQnaList() {
		return sqlSessionFactory.openSession().selectList("Qna.getQnaList");
	}

	@Override
	public Qna selectQna(int QNAID) {
		return (Qna)sqlSessionFactory.openSession().selectOne("Qna.getQna", QNAID);
	}

	@Override
	public int insertQna(Qna qna) {
		return sqlSessionFactory.openSession().insert("Qna.insertQna", qna);
	}

	@Override
	public int updateQna(Qna qna) {
		return sqlSessionFactory.openSession().update("Qna.updateQna", qna);
	}

	@Override
	public int deleteQna(int QNAID) {
		return sqlSessionFactory.openSession().delete("Qna.deleteQna", QNAID);
	}

}
