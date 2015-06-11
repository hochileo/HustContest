package vn.hust.edu.model;

public class InfoHistories {
	private String id;
	private String subject_id;
	private String subject;
	private String contest;
	private String sub_subject;
	private String exam;
	private String semeter;
	private String score;
	private String score_text;

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSub_subject() {
		return sub_subject;
	}

	public void setSub_subject(String sub_subject) {
		this.sub_subject = sub_subject;
	}

	public String getExam() {
		return exam;
	}

	public void setExam(String exam) {
		this.exam = exam;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getScore_text() {
		return score_text;
	}

	public void setScore_text(String score_text) {
		this.score_text = score_text;
	}

	public String getSemeter() {
		return semeter;
	}

	public void setSemeter(String semeter) {
		this.semeter = semeter;
	}

	public String getContest() {
		return contest;
	}

	public void setContest(String contest) {
		this.contest = contest;
	}



}
