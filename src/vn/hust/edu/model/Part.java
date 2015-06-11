package vn.hust.edu.model;

import java.util.List;

public class Part {
	private String part;
	private boolean intro;
	private List<Question> groupQuestion;

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public List<Question> getGroupQuestion() {
		return groupQuestion;
	}

	public void setGroupQuestion(List<Question> groupQuestion) {
		this.groupQuestion = groupQuestion;
	}

	public boolean isIntro() {
		return intro;
	}

	public void setIntro(boolean intro) {
		this.intro = intro;
	}

}
