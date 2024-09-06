package test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import test.domain.Qna;
import test.service.QnaService;

@Controller
@RequestMapping("/qna")
public class QnaController {
	
	@Autowired
	private QnaService qnaService;
	
	@RequestMapping("/qna")
	public List<Qna> getQnaList() {
		return qnaService.getQnaList();
	}
	
	@RequestMapping("/qna/{QNAID}")
	public Qna selectQna(@PathVariable int QNAID) {
		return qnaService.selectQna(QNAID);
	}
	
	@RequestMapping("/qna")
	public String insertQna(@RequestBody Qna qna) {
		return "/qna";
	}
	
	@RequestMapping("/qna")
	public String updateQna(@RequestBody Qna qna) {
		return "/qna";
	}
	
	@RequestMapping("/qna")
	public String deleteQna(@RequestBody Qna qna) {
		return "/qna";
	}
	
	
}
