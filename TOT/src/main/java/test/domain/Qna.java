package test.domain;

import java.sql.Timestamp;

public class Qna {
	
	private int QNAID;
	private String MEMID;
	private String QNA_001;
	private String QNA_002;
	private String COMMENTSTATUS;
	private String QNATITLE;
	private String QNATEXT;
	private Timestamp QNAREGDATE;
	private Timestamp QNAUPDATE;
	
	public Qna() {
	}

	public Qna(int qNAID, String mEMID, String qNA_001, String qNA_002, String cOMMENTSTATUS, String qNATITLE,
			String qNATEXT, Timestamp qNAREGDATE, Timestamp qNAUPDATE) {
		super();
		QNAID = qNAID;
		MEMID = mEMID;
		QNA_001 = qNA_001;
		QNA_002 = qNA_002;
		COMMENTSTATUS = cOMMENTSTATUS;
		QNATITLE = qNATITLE;
		QNATEXT = qNATEXT;
		QNAREGDATE = qNAREGDATE;
		QNAUPDATE = qNAUPDATE;
	}

	public int getQNAID() {
		return QNAID;
	}

	public void setQNAID(int qNAID) {
		QNAID = qNAID;
	}

	public String getMEMID() {
		return MEMID;
	}

	public void setMEMID(String mEMID) {
		MEMID = mEMID;
	}

	public String getQNA_001() {
		return QNA_001;
	}

	public void setQNA_001(String qNA_001) {
		QNA_001 = qNA_001;
	}

	public String getQNA_002() {
		return QNA_002;
	}

	public void setQNA_002(String qNA_002) {
		QNA_002 = qNA_002;
	}

	public String getCOMMENTSTATUS() {
		return COMMENTSTATUS;
	}

	public void setCOMMENTSTATUS(String cOMMENTSTATUS) {
		COMMENTSTATUS = cOMMENTSTATUS;
	}

	public String getQNATITLE() {
		return QNATITLE;
	}

	public void setQNATITLE(String qNATITLE) {
		QNATITLE = qNATITLE;
	}

	public String getQNATEXT() {
		return QNATEXT;
	}

	public void setQNATEXT(String qNATEXT) {
		QNATEXT = qNATEXT;
	}

	public Timestamp getQNAREGDATE() {
		return QNAREGDATE;
	}

	public void setQNAREGDATE(Timestamp qNAREGDATE) {
		QNAREGDATE = qNAREGDATE;
	}

	public Timestamp getQNAUPDATE() {
		return QNAUPDATE;
	}

	public void setQNAUPDATE(Timestamp qNAUPDATE) {
		QNAUPDATE = qNAUPDATE;
	}

	@Override
	public String toString() {
		return "QnaBoard [QNAID=" + QNAID + ", MEMID=" + MEMID + ", QNA_001=" + QNA_001 + ", QNA_002=" + QNA_002
				+ ", COMMENTSTATUS=" + COMMENTSTATUS + ", QNATITLE=" + QNATITLE + ", QNATEXT=" + QNATEXT
				+ ", QNAREGDATE=" + QNAREGDATE + ", QNAUPDATE=" + QNAUPDATE + "]";
	}
	
	
	
}
